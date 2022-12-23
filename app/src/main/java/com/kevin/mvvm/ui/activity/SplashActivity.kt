package com.kevin.mvvm.ui.activity

import android.os.Bundle
import android.view.View
import com.kevin.mvvm.databinding.ActivitySplashBinding
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.EasyAnimation.TranslateCallback
import com.kevin.mvvm.utils.EasyAnimation.moveViewWidth
import com.kevin.mvvm.utils.MVUtils

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar(true)
        moveViewWidth(binding.tvTranslate, object : TranslateCallback {
            override fun animationEnd() {
                binding.tvMvvm.visibility = View.VISIBLE
                jumpActivity(if (MVUtils.getBoolean(Constant.IS_LOGIN)) MainActivity::class.java else LoginActivity::class.java)
            }
        })
    }
}
