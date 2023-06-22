package com.fivesysdev.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fivesysdev.weatherapp.R
import com.fivesysdev.weatherapp.model.LocationInfo

class LocationAdapter(
    location: List<LocationInfo> = emptyList()
) : BaseAdapter<LocationInfo, LocationAdapter.ViewHolder>(location) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolder(view)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryNameTextView: TextView = itemView.findViewById(R.id.tv_country_name)
    }

    override fun bindData(holder: ViewHolder, data: LocationInfo) {
        holder.countryNameTextView.text = data.name
    }
}


