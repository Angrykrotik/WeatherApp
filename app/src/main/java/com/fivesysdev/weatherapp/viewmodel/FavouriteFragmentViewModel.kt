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
class FavouriteFragmentViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val weatherArrayMutableLiveData = MutableLiveData<List<WeatherData?>>()
    val weatherArrayLiveData: LiveData<List<WeatherData?>>
        get() = weatherArrayMutableLiveData

    fun favoriteList() = viewModelScope.launch {
        try {
            val weatherInfoArray = repository.getLocationInfoList()
            weatherArrayMutableLiveData.postValue(weatherInfoArray)
        } catch (e: IOException) {
            Log.e("LocationInfo", e.message ?: "")
        } catch (e: HttpException) {
            Log.e("LocationInfo", e.message ?: "")
        }
    }

}