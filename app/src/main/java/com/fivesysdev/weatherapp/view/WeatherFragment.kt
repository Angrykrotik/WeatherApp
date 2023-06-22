package com.fivesysdev.weatherapp.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fivesysdev.weatherapp.R
import com.fivesysdev.weatherapp.adapters.HourlyRecyclerAdapter
import com.fivesysdev.weatherapp.databinding.FragmentWeatherBinding
import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.WeatherData
import com.fivesysdev.weatherapp.utils.*
import com.fivesysdev.weatherapp.utils.NetworkUtils.connectivityManager
import com.fivesysdev.weatherapp.utils.NetworkUtils.isInternetAvailable
import com.fivesysdev.weatherapp.utils.PermissionUtils.checkLocationPermission
import com.fivesysdev.weatherapp.utils.PermissionUtils.isPermissionGranded
import com.fivesysdev.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModels()
    private val snackBar by lazy { createSnackbar() }
    private lateinit var hourlyRecyclerAdapter: HourlyRecyclerAdapter

    private val networkCallback by lazy {
        val callback: ((Boolean) -> (Unit)) = { isNetworkConnected ->
            if (!isNetworkConnected) {
                snackBar.show()
            } else {
                if (snackBar.isShown) {
                    snackBar.dismiss()
                }
            }
        }
        NetworkUtils.initNetworkListener(callback)
    }

    private val networkRequest by lazy { NetworkUtils.networkRequest() }
    private val connectivityManager by lazy { requireActivity().connectivityManager() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).fabClickListener = {
            findNavController().navigate(R.id.action_weatherFragment_to_locationSelectDialogFragmentSelectDialogFragment)
        }

        viewModel.weatherLiveData.observe(this, weatherObserver)
        viewModel.errorLiveData.observe(this, errorObserver)
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.cityInfoLiveData.observe(this, cityInfoObserver)

        this.checkLocationPermission {
            getInfo()
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            this.checkLocationPermission {
                getInfo()
            }
        }
        initRecyclerView()
    }
    private fun getInfo() {
        LocationUtils.getLocation(requireActivity()) { latitude, longitude ->
            viewModel.getWeather(latitude, longitude)
            viewModel.getCityName(latitude, longitude)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this@WeatherFragment.isPermissionGranded()) {
            getInfo()
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onPause() {
        super.onPause()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private val weatherObserver = Observer<WeatherData> { weatherData ->
        binding.run {
            currentTemperature.text =
                String.format(getString(R.string.current_temp), weatherData.current.temp)
            currentDate.text = TimeUtils.getDate(weatherData.current.dt)
            wind.text =
                String.format(getString(R.string.current_wind_speed), weatherData.current.windSpeed)
            uvIndex.text = String.format(getString(R.string.uv_index), weatherData.current.uvi)
            sunrise.text = String.format(
                getString(R.string.current_sunrise),
                TimeUtils.getTime(weatherData.current.sunrise)
            )
            sunset.text = String.format(
                getString(R.string.current_sunset),
                TimeUtils.getTime(weatherData.current.sunset)
            )
            humidity.text =
                String.format(getString(R.string.humidity), weatherData.current.humidity)
            weatherState.text = weatherData.current.weather.first().description
            minTemp.text = String.format(
                getString(R.string.min_temp),
                weatherData.daily.first().dailyTemp.minTemp
            )
            maxTemp.text = String.format(
                getString(R.string.max_temp),
                weatherData.daily.first().dailyTemp.maxTemp
            )
            rainProbability.text = String.format(
                getString(R.string.rain_probability),
                weatherData.daily.first().dailyTemp.dailyRainProbabilyty.toInt()
            )
            cloudCover.text = String.format(getString(R.string.clouds), weatherData.current.clouds)
            windGust.text =
                String.format(getString(R.string.wind_gust), weatherData.current.windGust)
            pressure.text =
                String.format(getString(R.string.pressure), weatherData.current.pressure)
            currentTime.text = TimeUtils.getTime(weatherData.current.dt)

            switch1.setOnClickListener {
                weatherData.hourly.forEach { it.isFahrenheit = switch1.isChecked }
                hourlyRecyclerAdapter.submitList(null)
                hourlyRecyclerAdapter.submitList(weatherData.hourly)
                if (switch1.isChecked) {
                    currentTemperature.text = String.format(
                        getString(R.string.current_temp_converter),
                        TempUtils.convertTemp(weatherData.current.temp).toFloat()
                    )
                    minTemp.text = String.format(
                        getString(R.string.current_min_temp_converter),
                        TempUtils.convertTemp(weatherData.daily.first().dailyTemp.minTemp).toFloat()
                    )
                    maxTemp.text = String.format(
                        getString(R.string.current_max_temp_converter),
                        TempUtils.convertTemp(weatherData.daily.first().dailyTemp.maxTemp).toFloat()
                    )
                } else {
                    currentTemperature.text =
                        String.format(getString(R.string.current_temp), weatherData.current.temp)
                    minTemp.text = String.format(
                        getString(R.string.min_temp),
                        weatherData.daily.first().dailyTemp.minTemp
                    )
                    maxTemp.text = String.format(
                        getString(R.string.max_temp),
                        weatherData.daily.first().dailyTemp.maxTemp
                    )
                }
            }

            Glide.with(this@WeatherFragment)
                .load("http://openweathermap.org/img/wn/${weatherData.current.weather.first().icon}@2x.png")
                .into(weatherIcon)

            Glide.with(this@WeatherFragment)
                .load("http://openweathermap.org/img/wn/${weatherData.current.weather.first().icon}@2x.png")
                .into(secondWeatherIcon)

            swipeRefreshLayout.isRefreshing = false

            hourlyRecyclerAdapter.submitList(weatherData.hourly)
        }
    }
    private val cityInfoObserver = Observer<CityInfo> { cityInfo ->
        binding.location.text =
            String.format(getString(R.string.location), cityInfo.name + "/" + cityInfo.country)
    }

    private fun initRecyclerView() = with(binding) {
        rvWeather.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        binding.swipeRefreshLayout.isRefreshing = false
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun createSnackbar(): Snackbar {
        return Snackbar.make(binding.root, "There is no Internet connection", Snackbar.LENGTH_INDEFINITE)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setBackgroundTint(Color.parseColor("#006400"))
            .setAction("Retry") {
                if (requireActivity().isInternetAvailable() && snackBar.isShown) {
                    snackBar.dismiss()
                }
                networkCallback
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}