package com.fivesysdev.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fivesysdev.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    private val haveFavouriteMutableLiveData = MutableLiveData<Boolean>()
    val haveFavouriteLiveData: LiveData<Boolean>
        get() = haveFavouriteMutableLiveData

    fun haveFavourite() {
        viewModelScope.launch {
            val haveFavourite = repository.haveFavourite()
            haveFavouriteMutableLiveData.postValue(haveFavourite)
        }
    }
}