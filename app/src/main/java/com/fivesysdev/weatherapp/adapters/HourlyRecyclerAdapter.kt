package com.fivesysdev.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fivesysdev.weatherapp.R
import com.fivesysdev.weatherapp.databinding.ItemHourWeatherBinding
import com.fivesysdev.weatherapp.model.HourlyWeather
import com.fivesysdev.weatherapp.utils.TempUtils
import com.fivesysdev.weatherapp.utils.TimeUtils

class HourlyRecyclerAdapter : ListAdapter<HourlyWeather,HourlyRecyclerAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemHourWeatherBinding.bind(view)
        fun bind(item: HourlyWeather) = with(binding) {
            tvTime.text = TimeUtils.getTime(item.dt)
            tvRainPercentage.text = String.format(
                itemView.context.getString(R.string.rain_percentage),
                item.rainProbability.toInt()
            )
            if (item.isFahrenheit) {
                tvTemperature.text = String.format(
                    itemView.context.getString(R.string.rv_temp_converter),
                    TempUtils.convertTemp(item.temp)
                )
            } else {
                tvTemperature.text =
                    String.format(itemView.context.getString(R.string.rv_temp), item.temp)
            }

            Glide.with(itemView.context)
                .load("http://openweathermap.org/img/wn/${item.weather.first().icon}@2x.png")
                .into(imgWeather)


        }
    }
    class Comparator : DiffUtil.ItemCallback<HourlyWeather>(){
        override fun areItemsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
            return oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hour_weather,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}