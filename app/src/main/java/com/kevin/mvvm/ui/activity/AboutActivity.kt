package com.kevin.mvvm.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityAboutBinding
import com.kevin.mvvm.utils.APKVersionInfoUtils
import com.kevin.mvvm.viewmodel.NotebookViewModel

open class AboutActivity : BaseActivity() {

    /**
     * 博客个人主页
     */
    private val CSDN = "https://llw-study.blog.csdn.net/"

    /**
     * 博客地址
     */
    private val CSDN_BLOG_URL = "https://blog.csdn.net/qq_38436214/category_11482619.html"

    /**
     * 源码地址
     */
    private val GITHUB_URL = "https://github.com/lilongweidev/MVVM-Demo"

    //viewBinding
    private lateinit var binding: ActivityAboutBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[NotebookViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        back(binding.toolbar)
        binding.tvVersion.text = APKVersionInfoUtils.getVerName(context)
        binding.tvBlog.setOnClickListener { jumpUrl(CSDN_BLOG_URL) }
        binding.tvCode.setOnClickListener {jumpUrl(GITHUB_URL) }
        binding.tvCopyEmail.setOnClickListener { copyEmail() }
        binding.tvAuthor.setOnClickListener { jumpUrl(CSDN) }
    }

    /**
     * 跳转URL
     *
     * @param url 地址
     */
    private fun jumpUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun copyEmail() {
        val myClipboard = context!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", "lonelyholiday@qq.com")
        myClipboard.setPrimaryClip(myClip)
        showMsg("邮箱已复制")
    }
}