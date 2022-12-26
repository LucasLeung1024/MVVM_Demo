package com.kevin.mvvm.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.kevin.mvvm.databinding.ActivityNotebookBinding
import com.kevin.mvvm.viewmodel.NotebookViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 记事本
 * @author llw
 */

class NotebookActivity : BaseActivity() {

    //viewBinding
    private lateinit var binding: ActivityNotebookBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[NotebookViewModel::class.java] }

    private var hasNotebook = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar(true)
        back(binding.toolbar)
    }

    /**
     * 去编辑
     */
    fun toEdit(view: View?) {
        jumpActivity(EditActivity::class.java)
    }

    companion object {
        private val TAG = NotebookActivity::class.java.simpleName
    }
}