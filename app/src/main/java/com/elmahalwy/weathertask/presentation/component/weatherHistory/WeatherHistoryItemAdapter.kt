package com.elmahalwy.weathertask.presentation.component.weatherHistory

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elmahalwy.weathertask.R
import com.elmahalwy.weathertask.data.dto.local.WeatherItem
import kotlinx.android.synthetic.main.item_weather_history_layout.view.*
import javax.inject.Inject

class WeatherHistoryItemAdapter @Inject constructor():
    RecyclerView.Adapter<WeatherHistoryItemAdapter.WeatherHistoryItemViewHolder>() {
    class WeatherHistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<WeatherItem>() {
        override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var weatherHistoryItems: List<WeatherItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHistoryItemViewHolder {
        return WeatherHistoryItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_weather_history_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return weatherHistoryItems.size
    }

    override fun onBindViewHolder(holder: WeatherHistoryItemViewHolder, position: Int) {
        val weatherItem = weatherHistoryItems[position]
        holder.itemView.apply {
            ivWeather.setImageURI(Uri.parse(weatherItem.image))
            tvWeatherCondition.text = weatherItem.weatherCondition
            tvCityName.text = weatherItem.placeName
            tvCurrentWeatherTemp.text = weatherItem.temp.toString()
            tvTempMin.text = weatherItem.tempMin.toString()
            tvTempMax.text = weatherItem.tempMax.toString()
            tvWindSpeed.text = weatherItem.windSpeed.toString()
        }
    }
}
