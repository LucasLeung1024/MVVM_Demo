package com.kevin.mvvm.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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

        binding.itemVideoLayout.setOnClickListener{
            if (item.share_url != null) {
                //跳转抖音去播放
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.share_url)))
            } else {
                Toast.makeText(context, "视频地址为空", Toast.LENGTH_SHORT).show()
            }
        }
    }

}