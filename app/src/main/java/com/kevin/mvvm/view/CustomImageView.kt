package com.kevin.mvvm.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.R


/**
 * 自定义View
 * @description CustomImageVIew
 */
class CustomImageView(context: Context?, @Nullable attrs: AttributeSet?) : ShapeableImageView(context!!, attrs) {

    private val OPTIONS: RequestOptions = RequestOptions()
        .placeholder(R.drawable.wallpaper_bg) //图片加载出来前，显示的图片
        .fallback(R.drawable.wallpaper_bg) //url为空的时候,显示的图片
        .error(R.mipmap.ic_loading_failed) //图片加载失败后，显示的图片

    private val OPTIONS_LOCAL: RequestOptions = RequestOptions()
        .placeholder(R.drawable.logo) //图片加载出来前，显示的图片
        .fallback(R.drawable.logo) //url为空的时候,显示的图片
        .error(R.mipmap.ic_loading_failed) //图片加载失败后，显示的图片
        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
        .skipMemoryCache(true)

    @BindingAdapter(value = ["localUrl"], requireAll = false)
    fun setLocalUrl(imageView: ImageView?, url: String?) {
        Glide.with(BaseApplication.context).load(url).apply(OPTIONS_LOCAL).into(imageView!!)
    }

    /**
     * 必应壁纸  因为拿到的url不完整，因此需要做一次地址拼接
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = ["biyingUrl"], requireAll = false)
    fun setBiyingUrl(imageView: ImageView?, url: String) {
        val assembleUrl = "http://cn.bing.com$url"
        Log.d("", assembleUrl)
        Glide.with(BaseApplication.context).load(assembleUrl).apply(OPTIONS)
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
}
