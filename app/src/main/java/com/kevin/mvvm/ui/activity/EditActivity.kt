package com.kevin.mvvm.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.R
import com.kevin.mvvm.databinding.ActivityEditBinding
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.viewmodel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * 记事本
 */

class EditActivity : BaseActivity(), View.OnClickListener {

    //viewBinding
    private lateinit var binding: ActivityEditBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[EditViewModel::class.java] }

    private var inputMethodManager: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar(true)
        back(binding.toolbar)

        //新增时 获取焦点
        showInput()
        initView()
    }

    private fun initView() {
        //监听输入
        listenInput(binding.etTitle)
        listenInput(binding.etContent)
        binding.ivOk.setOnClickListener(this)
    }

    /**
     * 监听输入
     * @param editText 输入框
     */
    private fun listenInput(editText: AppCompatEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    binding.ivOk.visibility = View.VISIBLE
                } else {
                    if (binding.etTitle.text!!.length === 0 && binding.etContent.text!!.length === 0) {
                        binding.ivOk.visibility = View.GONE
                    }
                }
            }
        })
    }

    /**
     * 显示键盘
     */
    private fun showInput() {
        binding.etContent.requestFocus()
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 隐藏键盘
     */
    private fun dismiss() {
        if (inputMethodManager != null) {
            inputMethodManager!!.hideSoftInputFromWindow(binding.etContent.windowToken, 0)
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_ok -> {
                vm.addNotebook(Notebook(binding.etTitle.text.toString(),binding.etContent.text.toString()))
                finish()
            }
        }
    }

}