package com.kevin.mvvm.adapter

import android.annotation.SuppressLint
import com.kevin.mvvm.databinding.ItemNewsBinding
import com.kevin.mvvm.model.NewsResponse

class NewsAdapter(data: MutableList<NewsResponse.ResultBean.DataBean>) :
    ViewBindingAdapter<ItemNewsBinding, NewsResponse.ResultBean.DataBean>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemNewsBinding>, item: NewsResponse.ResultBean.DataBean) {
        val binding = holder.vb
        binding.tvTitle.text = item.title
        binding.image.setNetworkUrl(binding.image, item.thumbnail_pic_s)
        binding.tvAuthor.text = item.author_name
        binding.tvDate.text = item.date
    }

}
