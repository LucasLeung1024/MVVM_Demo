package com.kevin.mvvm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityMainBinding
import com.kevin.mvvm.model.User
import com.kevin.mvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityMainBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //数据绑定视图
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //设置数据，直接显示在xml上，初始值
        //Model → View
        vm.user.value = User("admin", "123456")

        //获取观察对象
        vm.user.observe(this) {
            binding.viewModel = vm
        }

        //1. 单向绑定：最常用的就是当我Model中的数据改变时，改变页面上的值。这个是单向绑定
        //2. 双向绑定：在输入框输入数据时候直接将数据源中的数据进行改变，这里会用到ViewModel和LiveData

        binding.btnLogin.setOnClickListener{
            if (vm.user.value?.account?.isEmpty() == true) {
                Toast.makeText(this@MainActivity, "请输入账号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (vm.user.value?.pwd?.isEmpty() == true) {
                Toast.makeText(this@MainActivity, "请输入密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this@MainActivity, "登录成功", Toast.LENGTH_SHORT).show()
        }
    }
}