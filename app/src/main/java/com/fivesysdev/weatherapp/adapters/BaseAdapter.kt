package com.fivesysdev.weatherapp.adapters

import androidx.recyclerview.widget.RecyclerView
import com.fivesysdev.weatherapp.model.WeatherData

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(
    var dataList: List<T>,
) : RecyclerView.Adapter<VH>() {

    private var onItemClickListener: ((position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: ((position: Int) -> Unit)?) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getItem(position: Int): T {
        return dataList[position]
    }

    open fun updateData(newDataList: List<T>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = dataList[position]
        bindData(holder, data)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    abstract fun bindData(holder: VH, data: T)
}
