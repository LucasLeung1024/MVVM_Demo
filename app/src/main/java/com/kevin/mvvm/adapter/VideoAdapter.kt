package com.kevin.mvvm.adapter

import android.annotation.SuppressLint
import com.kevin.mvvm.databinding.ItemVideoBinding
import com.kevin.mvvm.model.VideoResponse

class VideoAdapter(data: MutableList<VideoResponse.ResultBean>) : ViewBindingAdapter<ItemVideoBinding, VideoResponse.ResultBean>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemVideoBinding>, item: VideoResponse.ResultBean) {
        val binding = holder.vb
        binding.tvTitle.text = item.title
        binding.image.setNetworkUrl(binding.image, item.item_cover)
        binding.tvAuthor.text = item.author
        binding.tvWords.text = item.hot_words
    }

}
