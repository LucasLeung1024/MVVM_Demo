package com.kevin.mvvm.adapter

import android.annotation.SuppressLint
import android.content.Intent
import com.kevin.mvvm.activity.PictureViewActivity
import com.kevin.mvvm.databinding.ItemWallPaperBinding
import com.kevin.mvvm.model.WallPaperResponse.ResBean.VerticalBean

class WallPaperAdapter(data: MutableList<VerticalBean>) :
    ViewBindingAdapter<ItemWallPaperBinding, VerticalBean>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemWallPaperBinding>, item: VerticalBean) {
        val binding = holder.vb
        binding.cusImageView.setNetworkUrl(binding.cusImageView, item.img)

        binding.cusImageView.setOnClickListener{
            val intent = Intent(context, PictureViewActivity::class.java)
            intent.putExtra("img", item.img)
            context.startActivity(intent)
        }
    }

}

