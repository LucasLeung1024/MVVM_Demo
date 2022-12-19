package com.kevin.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.kevin.mvvm.adapter.WallPaperAdapter
import com.kevin.mvvm.databinding.ActivityMainBinding
import com.kevin.mvvm.model.WallPaperResponse
import com.kevin.mvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityMainBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.getBiYing()
        vm.biying.observe(this) { result ->
            val biYingResponse = result.getOrNull()
            val imageUrl = biYingResponse!!.images!![0].url
            binding.image.setBiyingUrl(binding.image, imageUrl!!)
        }

        initView()
        //热门壁纸 网络请求
        vm.getWallPaper()
        vm.wallPaper.observe(this) { result->
            val wallPaper = result.getOrNull()
            if(wallPaper != null) {
                val vertical = wallPaper.res?.vertical
                binding.rv.adapter =
                    WallPaperAdapter(vertical as MutableList<WallPaperResponse.ResBean.VerticalBean>)
            }
        }

    }

    /**
     * 初始化
     */
    private fun initView() {
        val manager = GridLayoutManager(this, 2)
        binding.rv.layoutManager = manager

        //伸缩偏移量监听，这部分只是为了修改标题的
        binding.appbarLayout.addOnOffsetChangedListener(object :
            AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) { //收缩时
                    binding.toolbarLayout.title = "MVVM-Demo"
                    isShow = true
                } else if (isShow) { //展开时
                    binding.toolbarLayout.title = ""
                    isShow = false
                }
            }
        })

    }
}