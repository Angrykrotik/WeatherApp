package com.fivesysdev.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fivesysdev.weatherapp.R
import com.fivesysdev.weatherapp.model.WeatherData

class FavouriteAdapter(weather: List<WeatherData> = emptyList()) :
    BaseAdapter<WeatherData, FavouriteAdapter.ViewHolder>(weather) {

    override fun bindData(holder: ViewHolder, data: WeatherData) {
        holder.cityName.text = data.cityName
        holder.temp.text = data.current.temp.toString()
        holder.weatherState.text = data.current.weather.first().description
        Glide.with(holder.itemView)
            .load("http://openweathermap.org/img/wn/${data.current.weather.first().icon}@2x.png") // Replace with the actual image URL or resource
            .into(holder.weatherIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location_weather, parent, false)
        return ViewHolder(view)
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.tv_location_name)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_location)
        val weatherState: TextView = itemView.findViewById(R.id.tv_weather_state)
        val weatherIcon: ImageView = itemView.findViewById(R.id.iv_image_location)
    }


}