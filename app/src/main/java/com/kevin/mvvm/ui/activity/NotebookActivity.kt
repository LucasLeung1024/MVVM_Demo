package com.kevin.mvvm.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.mvvm.R
import com.kevin.mvvm.databinding.ActivityNotebookBinding
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.ui.adapter.NotebookAdapter
import com.kevin.mvvm.utils.Constant
import com.kevin.mvvm.utils.MVUtils
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

    //菜单Item
    private var itemViewType: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar(true)
        setSupportActionBar(binding.toolbar)
        back(binding.toolbar)
    }

    override fun onResume() {
        super.onResume()
        vm.getNoteBooks()
        vm.notebooks.observe(this) { results ->
            val notebooks = results.getOrNull()
            if (notebooks!!.isNotEmpty()) {
                //选择是列表试图还是宫格视图
                if(MVUtils.getInt(Constant.NOTEBOOK_VIEW_TYPE) == 1) {
                    binding.rvNotebook.layoutManager = GridLayoutManager(context,2)
                } else {
                    binding.rvNotebook.layoutManager = LinearLayoutManager(context)
                }

                binding.rvNotebook.adapter = NotebookAdapter(notebooks as MutableList<Notebook>)
                hasNotebook = true
            } else {
                hasNotebook = false
            }
            binding.hasNotebook = hasNotebook
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notebook_settings, menu)
        itemViewType = menu!!.findItem(R.id.item_view_type)
            .setTitle(if (MVUtils.getInt(Constant.NOTEBOOK_VIEW_TYPE) == 1) "列表视图" else "宫格视图")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 0 是列表视图，1是宫格视图
        var viewType = MVUtils.getInt(Constant.NOTEBOOK_VIEW_TYPE)
        if(item.itemId == R.id.item_view_type) { //视图方式
            if(viewType == 0) {
                viewType = 1
                itemViewType!!.title = "列表视图"
                binding.rvNotebook.layoutManager = GridLayoutManager(context, 2)
            } else {
                viewType = 0
                itemViewType!!.title = "宫格视图"
                binding.rvNotebook.layoutManager = LinearLayoutManager(context)
            }
            MVUtils.put(Constant.NOTEBOOK_VIEW_TYPE, viewType)
        }
        return true
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