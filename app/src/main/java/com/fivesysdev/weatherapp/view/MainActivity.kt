package com.fivesysdev.weatherapp.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fivesysdev.weatherapp.adapters.HourlyRecyclerAdapter
import com.fivesysdev.weatherapp.databinding.ActivityMainBinding
import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.WeatherData
import com.fivesysdev.weatherapp.utils.LocationUtils
import com.fivesysdev.weatherapp.utils.NetworkUtils
import com.fivesysdev.weatherapp.utils.NetworkUtils.connectivityManager
import com.fivesysdev.weatherapp.utils.NetworkUtils.isInternetAvailable
import com.fivesysdev.weatherapp.utils.PermissionUtils.checkLocationPermission
import com.fivesysdev.weatherapp.utils.TimeUtils
import com.fivesysdev.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val snackbar by lazy {  createSnackbar() }
    private lateinit var hourlyRecyclerAdapter: HourlyRecyclerAdapter

    private val networkCallback by lazy {
        val callback: ((Boolean) -> (Unit)) = { isNetworkConnected ->
            if (!isNetworkConnected) {
                snackbar.show()
            } else {
                if (snackbar.isShown) {
                    snackbar.dismiss()
                }
            }
        }
        NetworkUtils.initNetworkListener(callback)
    }

    private val networkRequest by lazy { NetworkUtils.networkRequest() }
    private val connectivityManager by lazy { this.connectivityManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.weatherLiveData.observe(this, weatherObserver)
        viewModel.errorLiveData.observe(this, errorObserver)
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.cityInfoLiveData.observe(this, cityInfoObserver)

        this.checkLocationPermission {
            LocationUtils.getLocation(this) { latitude, longitude ->
                viewModel.getWeather(latitude, longitude)
                viewModel.getCityName(latitude, longitude)
            }
        }

        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private val weatherObserver = Observer<WeatherData> { weatherData ->
        binding.run {
            currentTemperature.text = "${weatherData.current.temp}°c"
            currentDate.text = TimeUtils.getDate(weatherData.current.dt)
            wind.text = weatherData.current.windSpeed.toString()
            uvIndex.text = weatherData.current.uvi.toString()
            sunrise.text = TimeUtils.getTime(weatherData.current.sunrise)
            sunset.text = TimeUtils.getTime(weatherData.current.sunset)
            humidity.text = weatherData.current.humidity.toString()

            Glide.with(this@MainActivity)
                .load("http://openweathermap.org/img/wn/${weatherData.current.weather.first().icon}@2x.png")
                .into(weatherIcon)


            hourlyRecyclerAdapter.submitList(weatherData.hourly)
        }
    }
    private val cityInfoObserver = Observer<CityInfo> { cityInfo ->
        binding.location.text = "${cityInfo.name}/${cityInfo.country}"
    }

    private fun initRecyclerView() = with(binding) {
        rvWeather.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        hourlyRecyclerAdapter = HourlyRecyclerAdapter()
        rvWeather.adapter = hourlyRecyclerAdapter
    }

    private val loadingObserver = Observer<Boolean> { isLoading ->
        binding.run {
            clMainInfo.isVisible = !isLoading
            clSecondInfo.isVisible = !isLoading
            rvWeather.isVisible = !isLoading
            progressBar.isVisible = isLoading
            switch1.isVisible = !isLoading
        }
    }

    private val errorObserver = Observer<String> { error ->
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun createSnackbar(): Snackbar {
        return Snackbar.make(binding.root, "There is no Internet connection", Snackbar.LENGTH_INDEFINITE)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(Color.parseColor("#006400"))
            .setAction("Retry") {
                if (this.isInternetAvailable() && snackbar.isShown) {
                    snackbar.dismiss()
                }
                networkCallback
            }
    }

}
