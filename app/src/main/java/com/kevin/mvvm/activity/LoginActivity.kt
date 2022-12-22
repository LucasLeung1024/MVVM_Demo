package com.kevin.mvvm.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityLoginBinding
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.MVUtils
import com.kevin.mvvm.viewmodel.LoginViewModel

class LoginActivity : BaseActivity() {
    //viewBinding
    private lateinit var binding: ActivityLoginBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {

        /**测试腾讯 MMKV **/
        //存
        Log.d("TAG", "onCreate: 存")
        MVUtils.put("age", 24)
        //取
        val age = MVUtils.getInt("age", 0)
        Log.d("TAG", "onCreate: 取 ：$age")

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //设置数据，直接显示在xml上
        vm.user.value =  User(1, "", "", "", "", "","")

        //获取观察对象
        vm.user.observe(this) {
            binding.viewModel = vm
        }

        //1. 单向绑定：最常用的就是当我Model中的数据改变时，改变页面上的值。这个是单向绑定
        //2. 双向绑定：在输入框输入数据时候直接将数据源中的数据进行改变，这里会用到ViewModel和LiveData

        binding.btnLogin.setOnClickListener{
            if (vm.user.value?.getAccount()?.isEmpty() == true) {
                Toast.makeText(this@LoginActivity, "请输入账号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (vm.user.value?.getPwd()?.isEmpty() == true) {
                Toast.makeText(this@LoginActivity, "请输入密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //检查输入的账户和密码是否是注册过的。
            checkUser()
        }
    }


    private fun checkUser() {
        vm.getLocalUser().observe(this) { localUser ->
            if (!vm.user.value?.getAccount().equals(localUser.getOrNull()?.getAccount()) ||
                !vm.user.value?.getPwd().equals(localUser.getOrNull()?.getPwd())
            ) {
                showMsg("账号或密码错误")
                return@observe
            }
            //记录已经登录过
            MVUtils.put(Constant.IS_LOGIN, true)
            showMsg("登录成功")
            jumpActivity(MainActivity::class.java)
        }
    }

    private var timeMillis: Long = 0

    /**
     * Add a prompt to exit the application
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() === KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - timeMillis > 2000) {
                showMsg("再次按下退出应用程序")
                timeMillis = System.currentTimeMillis()
            } else {
                exitTheProgram()
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    fun toRegister(view: View?) {
        jumpActivity(RegisterActivity::class.java)
    }

}