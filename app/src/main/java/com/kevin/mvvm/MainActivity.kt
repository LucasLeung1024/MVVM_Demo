package com.kevin.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityMainBinding
import com.kevin.mvvm.repository.MainRepository
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
    }
}