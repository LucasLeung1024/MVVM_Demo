package com.kevin.mvvm.ui.adapter

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.kevin.mvvm.databinding.ItemImageBinding
import com.kevin.mvvm.db.bean.WallPaper

class ImageAdapter(data: MutableList<WallPaper>) :
    ViewBindingAdapter<ItemImageBinding, WallPaper>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemImageBinding>, item: WallPaper) {
        val binding = holder.vb
        Glide.with(context).load(item.img).into(binding.image)
    }

}
