package com.kevin.mvvm.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.mvvm.databinding.ActivityNotebookBinding
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.ui.adapter.NotebookAdapter
import com.kevin.mvvm.viewmodel.NotebookViewModel

/**
 * 记事本
 * @author llw
 */

class NotebookActivity : BaseActivity() {

    private val TAG = NotebookActivity::class.java.simpleName

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

    override fun onResume() {
        super.onResume()
        vm.getNoteBooks()
        vm.notebooks.observe(this) { results ->
            val notebooks = results.getOrNull()
            if (notebooks!!.isNotEmpty()) {
                binding.rvNotebook.layoutManager = LinearLayoutManager(
                    context
                )
                binding.rvNotebook.adapter = NotebookAdapter(notebooks as MutableList<Notebook>)
                hasNotebook = true
            } else {
                hasNotebook = false
            }
            binding.hasNotebook = hasNotebook
        }
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