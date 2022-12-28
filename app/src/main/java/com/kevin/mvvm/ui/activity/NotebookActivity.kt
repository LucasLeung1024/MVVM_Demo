package com.kevin.mvvm.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
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

class NotebookActivity : BaseActivity(), View.OnClickListener {

    private val TAG = NotebookActivity::class.java.simpleName

    //viewBinding
    private lateinit var binding: ActivityNotebookBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[NotebookViewModel::class.java] }

    private var hasNotebook = false

    //菜单Item
    private var itemViewType: MenuItem? = null

    //笔记适配器
    private var notebookAdapter: NotebookAdapter? = null
    //笔记列表
    private val mList: MutableList<Notebook> = arrayListOf()
    //是否为批量删除
    private var isBatchDeletion = false
    //是否全选
    private var isAllSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化
        initView()
    }

    private fun initView(){
        setStatusBar(true)
        setSupportActionBar(binding.toolbar)
        back(binding.toolbar)
        //监听事件
        binding.tvDelete.setOnClickListener(this)
        binding.tvAllSelected.setOnClickListener(this)
        binding.ivClear.setOnClickListener(this)
        //初始化列表
        initList()
        //输入框监听
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    binding.isSearch = true
                    //搜索笔记
                    vm.searchNotebook(s.toString())
                } else {
                    //获取全部笔记
                    binding.isSearch = false
                    vm.getNoteBooks()
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        vm.getNoteBooks()
        vm.notebooks.observe(this) { results ->
            val notebooks = results!!.getOrNull()
            if (notebooks!!.isNotEmpty()) {
                mList.clear()
                //添加数据
                mList.addAll(notebooks)
                notebookAdapter!!.notifyDataSetChanged()
                hasNotebook = true
            } else {
                hasNotebook = false
            }
            binding.hasNotebook = hasNotebook
            //是否显示搜索布局
            binding.showSearchLay = hasNotebook || binding.etSearch.text.toString().isNotEmpty()
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
        } else if (item.itemId == R.id.item_batch_deletion) {  //批量删除
            //设置批量删除模式
            setBatchDeletionMode()
        }
        return true
    }

    /**
     * 初始化列表
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initList() {
        //适配器
        notebookAdapter = NotebookAdapter(mList)
        //设置适配器
        binding.rvNotebook.adapter = notebookAdapter
        binding.rvNotebook.layoutManager =
        if (MVUtils.getInt(Constant.NOTEBOOK_VIEW_TYPE) == 1) {
            GridLayoutManager(context, 2)
        } else {
            LinearLayoutManager(context)
        }
        //item点击事件
        notebookAdapter!!.setOnItemClickListener(OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            if (isBatchDeletion) {
                //选中设备
                mList[position].isSelect = !mList[position].isSelect
                notebookAdapter!!.notifyDataSetChanged()
                //修改页面标题
                changeTitle()
            } else {
                val intent = Intent(this@NotebookActivity, EditActivity::class.java)
                intent.putExtra("uid", mList[position].uid)
                startActivity(intent)
            }
        })
    }

    /**
     * 修改标题
     */
    @SuppressLint("SetTextI18n")
    private fun changeTitle() {
        var selectedNum = 0
        for (notebook in mList) {
            if (notebook.isSelect) {
                selectedNum++
            }
        }
        Log.e(TAG, "changeTitle: $selectedNum")
        binding.tvTitle.text = "已选择 $selectedNum 项"
        binding.isAllSelected = selectedNum == mList.size
    }

    /**
     * 设置批量删除模式
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun setBatchDeletionMode() {
        //进入批量删除模式
        isBatchDeletion = !isBatchDeletion
        //设置当前页面
        binding.isBatchDeletion = isBatchDeletion
        if (!isBatchDeletion) {
            //取消所有选中
            for (notebook in mList) {
                notebook.isSelect = false
            }
        }
        //设置适配器
        notebookAdapter!!.isBatchDeletion = isBatchDeletion
        notebookAdapter!!.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (isBatchDeletion) {
            //设置批量删除模式
            setBatchDeletionMode()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 显示确认删除弹窗
     */
    private fun showConfirmDelete() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this).setMessage("确定要删除所选的笔记吗？")
            .setPositiveButton("确定") { dialog, which ->
                val notebookList: MutableList<Notebook> = ArrayList()
                //删除所选中的笔记
                for (notebook in mList) {
                    if (notebook.isSelect) {
                        notebookList.add(notebook)
                    }
                }
                val notebooks = notebookList.toTypedArray()
                vm.deleteNotebook(*notebooks)
                //设置批量删除模式
                setBatchDeletionMode()
                //请求数据
                vm.getNoteBooks()
                dialog.dismiss()
            }
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
        builder.create().show()
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

    override fun onClick(v: View) {
        when (v.id) {
            // "删除"
            R.id.tv_delete -> {showConfirmDelete()}
            // "取消全选"
            R.id.tv_all_selected -> {allSelected()}
            else -> {}
        }
    }

    /**
     * 全选/取消全选
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun allSelected() {
        isAllSelected = !isAllSelected
        //设置适配器
        for (notebook in mList) {
            notebook.isSelect = isAllSelected
        }
        //修改页面标题
        changeTitle()
        //设置当前页面
        binding.isAllSelected = isAllSelected
        //刷新适配器
        notebookAdapter!!.notifyDataSetChanged()
    }

}