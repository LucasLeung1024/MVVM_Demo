package com.kevin.mvvm.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.kevin.mvvm.databinding.ItemNotebookBinding
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.ui.activity.EditActivity


class NotebookAdapter(data: MutableList<Notebook>) :
    ViewBindingAdapter<ItemNotebookBinding, Notebook>(data) {

    //是否批量删除
    var isBatchDeletion: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemNotebookBinding>, notebook: Notebook) {
        val binding = holder.vb
        binding.notebook = notebook
        binding.isBatchDeletion = isBatchDeletion
        binding.executePendingBindings()
    }

}