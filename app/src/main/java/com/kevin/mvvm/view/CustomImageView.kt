package com.kevin.mvvm_2.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kevin.mvvm.BaseApplication

/**
 * 自定义View
 * @description CustomImageVIew
 */
class CustomImageView(context: Context?, @Nullable attrs: AttributeSet?) : AppCompatImageView(context!!, attrs) {

    //companion object {
        /**
         * 必应壁纸  因为拿到的url不完整，因此需要做一次地址拼接
         * @param imageView 图片视图
         * @param url 网络url
         */
        @BindingAdapter(value = ["biyingUrl"], requireAll = false)
        fun setBiyingUrl(imageView: ImageView?, url: String) {
            val assembleUrl = "http://cn.bing.com$url"
            Log.d("", assembleUrl)
            Glide.with(BaseApplication.context).load(assembleUrl)
                .into(imageView!!)
        }

        /**
         * 普通网络地址图片
         * @param imageView 图片视图
         * @param url 网络url
         */
        @BindingAdapter(value = ["networkUrl"], requireAll = false)
        fun setNetworkUrl(imageView: ImageView?, url: String?) {
            Glide.with(BaseApplication.context).load(url).into(imageView!!)
        }
   // }
}
