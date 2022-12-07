package com.fivesysdev.weatherapp.di

import com.fivesysdev.weatherapp.repository.WeatherRepository
import com.fivesysdev.weatherapp.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(repo: WeatherRepositoryImpl): WeatherRepository

}