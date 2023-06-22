package com.fivesysdev.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.fivesysdev.weatherapp.MyApplication
import com.fivesysdev.weatherapp.db.AppDatabase
import com.fivesysdev.weatherapp.db.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "weather_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(db: AppDatabase): WeatherDao = db.getWeatherDao()

}