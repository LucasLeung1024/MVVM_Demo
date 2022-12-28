package com.kevin.mvvm.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityWebBinding
import com.kevin.mvvm.viewmodel.WebViewModel
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class WebActivity : BaseActivity() {

    //viewBinding
    private lateinit var binding: ActivityWebBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[WebViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.webViewClient = client
        setStatusBar(!isNight())

        //enable: true(日间模式)，enable:false(夜间模式)
        binding.webView.settingsExtension.setDayOrNight(!isNight())

        // 在调用TBS初始化、创建WebView之前进行如下配置
        vm.setUniqueKey(intent.getStringExtra("uniquekey").toString())
        vm.newsDetailsResponse.observe(this) { result ->
            binding.webView.loadUrl(result.getOrNull()?.result?.detail?.url)
        }
    }

    //配置腾讯WebView
    private val client: WebViewClient = object : WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onReceivedHttpAuthRequest(
            webview: WebView?,
            httpAuthHandlerhost: HttpAuthHandler, host: String?,
            realm: String?
        ) {
            val flag = httpAuthHandlerhost.useHttpAuthUsernamePassword()
        }

        override fun onPageFinished(webView: WebView?, s: String?) {
            super.onPageFinished(webView, s)
        }

        override fun onReceivedError(webView: WebView?, i: Int, s: String?, s1: String?) {
            println("***********onReceivedError ************")
            super.onReceivedError(webView, i, s, s1)
        }

        override fun onReceivedHttpError(
            webView: WebView?,
            webResourceRequest: WebResourceRequest?,
            webResourceResponse: WebResourceResponse?
        ) {
            println("***********onReceivedHttpError ************")
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse)
        }
    }

}
