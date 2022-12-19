package com.kevin.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.adapter.ImageAdapter
import com.kevin.mvvm.databinding.ActivityPictureViewBinding
import com.kevin.mvvm.viewmodel.PictureViewModel

class PictureViewActivity : AppCompatActivity() {

    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[PictureViewModel::class.java] }
    private lateinit var binding: ActivityPictureViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val img = intent.getStringExtra("img")

        vm.getWallPaper()
        vm.wallPaperList.observe(this){ result->
            val list = result.getOrNull()
            binding.vp.adapter = ImageAdapter(list!!)
            // 通过传递过来的url地址和查询到的url进行比对，得到具体的位置，然后显示这个vp的
            // 当前位置item，这里有一个false，为什么是false，因为不需要显示动画效果，如果
            // 不设置为false，当所选的位置不是第0个时，会有一个动画效果，现在去掉这个效果
            for (i in 0 until list.size) {
                if (list[i].img.equals(img)) {
                    binding.vp.setCurrentItem(i, false)
                    break
                }
            }
        }
    }

}