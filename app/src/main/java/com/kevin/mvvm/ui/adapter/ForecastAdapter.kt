package com.kevin.mvvm.ui.adapter

import android.annotation.SuppressLint
import com.amap.api.services.weather.LocalDayWeatherForecast
import com.kevin.mvvm.databinding.ItemForecastBinding
import com.kevin.mvvm.utils.EasyDate

class ForecastAdapter(data: MutableList<LocalDayWeatherForecast>) :
    ViewBindingAdapter<ItemForecastBinding, LocalDayWeatherForecast>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemForecastBinding>, localDayWeatherForecast: LocalDayWeatherForecast) {
        val binding = holder.vb
        //设置Binding的相关属性
        binding.forecast = localDayWeatherForecast
        binding.tvWeek.text = EasyDate.getWeek(localDayWeatherForecast.date)
        binding.executePendingBindings()
    }

}