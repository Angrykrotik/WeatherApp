package com.fivesysdev.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fivesysdev.weatherapp.model.CityInfo
import com.fivesysdev.weatherapp.model.WeatherData
import com.fivesysdev.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    private val weatherMutableLiveData = MutableLiveData<WeatherData>()
    val weatherLiveData: LiveData<WeatherData>
        get() = weatherMutableLiveData

    private val cityInfoMutableLiveData = MutableLiveData<CityInfo>()
    val cityInfoLiveData: LiveData<CityInfo>
        get() = cityInfoMutableLiveData

    val errorLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun getWeather(latitude: Double, longitude: Double) = viewModelScope.launch {
        try {
            loadingLiveData.postValue(true)
            val weather = repository.getWeather(latitude, longitude)
            if (weather == null) {
                errorLiveData.postValue("Something were wrong!")
            } else {
                weatherMutableLiveData.postValue(weather)
            }
            loadingLiveData.postValue(false)
        } catch (e: IOException) {
            errorLiveData.postValue("Error ${e.message}!")
            loadingLiveData.postValue(false)
        } catch (e: HttpException) {
            errorLiveData.postValue("Error ${e.message}!")
            loadingLiveData.postValue(false)
        }
    }

    fun getCityName(latitude: Double, longitude: Double) = viewModelScope.launch {
        try {
            val cityInfo = repository.getCityName(latitude, longitude)
            if (cityInfo != null) {
                cityInfoMutableLiveData.postValue(cityInfo)
            }
        } catch (e: IOException) {
            Log.e("CityInfo", e.message ?: "")
        } catch (e: HttpException) {
            Log.e("CityInfo", e.message ?: "")
        }
    }

}

