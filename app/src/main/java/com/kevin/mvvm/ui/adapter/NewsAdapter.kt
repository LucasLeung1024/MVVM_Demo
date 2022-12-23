package com.kevin.mvvm.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import com.kevin.mvvm.ui.activity.WebActivity
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
        binding.itemLayout.setOnClickListener{
            if ("1" == item.is_content) {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra("uniquekey", item.uniquekey)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "没有详情信息", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
