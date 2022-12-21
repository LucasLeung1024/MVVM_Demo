package com.kevin.mvvm.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kevin.mvvm.R
import com.kevin.mvvm.databinding.ActivityHomeBinding
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.MVUtils
import com.kevin.mvvm.viewmodel.HomeViewModel

class HomeActivity : BaseActivity() {

    //viewBinding
    private lateinit var binding: ActivityHomeBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    /**
     * 初始化
     */
    private fun initView() {
        //获取navController
        val navView: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.news_fragment -> {
                    binding.tvTitle.text = "头条新闻"
                    navController.navigate(R.id.news_fragment)
                }
                R.id.video_fragment -> {
                    binding.tvTitle.text = "热门视频"
                    navController.navigate(R.id.video_fragment)
                }
                else -> {}
            }
            true
        }

        binding.ivAvatar.setOnClickListener { v -> binding.drawerLayout.open() }
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_setting -> {}
                R.id.item_logout -> logout()
                else -> {}
            }
            true
        }

    }

    /**
     * 退出登录
     */
    private fun logout() {
        showMsg("退出登录")
        MVUtils.put(Constant.IS_LOGIN, false)
        jumpActivityFinish(LoginActivity::class.java)
    }

}
