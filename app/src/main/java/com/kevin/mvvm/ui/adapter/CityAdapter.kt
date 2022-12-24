package com.kevin.mvvm.ui.adapter

import android.annotation.SuppressLint
import com.kevin.mvvm.databinding.ItemCityBinding

class CityAdapter(data: MutableList<String>) :
    ViewBindingAdapter<ItemCityBinding, String>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemCityBinding>, s: String) {
        val binding = holder.vb
        binding.cityName = s
        binding.executePendingBindings()
    }

}
