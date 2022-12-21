package com.kevin.mvvm.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityRegisterBinding
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.viewmodel.RegisterViewModel
import kotlin.concurrent.thread

class RegisterActivity : BaseActivity() {
    //viewBinding
    private lateinit var binding: ActivityRegisterBinding

    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[RegisterViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm.user.value = User(1, "", "", "", "", "")
        //获取观察对象
        vm.user.observe(this) {
            binding.register = vm
        }
        initView()
    }

    private fun initView() {
        back(binding.toolbar)
        binding.btnRegister.setOnClickListener { v ->
            if (vm.user.value?.getAccount()?.isEmpty() == true) {
                showMsg("请输入账号")
                return@setOnClickListener
            }
            if (vm.user.value?.getPwd()?.isEmpty() == true) {
                showMsg("请输入密码")
                return@setOnClickListener
            }
            if (vm.user.value?.getConfirmPwd()?.isEmpty() == true) {
                showMsg("请确认密码")
                return@setOnClickListener
            }
            if (!vm.user.value?.getPwd().equals(vm.user.value?.getConfirmPwd())) {
                showMsg("两次输入密码不一致")
                return@setOnClickListener
            }
            thread {
                vm.register()
            }
            showMsg("注册成功")
            finish()
        }
    }
}
