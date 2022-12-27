package com.kevin.mvvm.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.kevin.mvvm.databinding.ItemNotebookBinding
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.ui.activity.EditActivity


class NotebookAdapter(data: MutableList<Notebook>) :
    ViewBindingAdapter<ItemNotebookBinding, Notebook>(data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: ViewBindingHolder<ItemNotebookBinding>, notebook: Notebook) {
        val binding = holder.vb
        binding.notebook = notebook
        binding.onClick = ClickBinding()
        binding.executePendingBindings()
    }

    class ClickBinding {
        fun itemClick(notebook: Notebook, view: View) {
            val intent = Intent(view.context, EditActivity::class.java)
            intent.putExtra("uid", notebook.uid)
            view.context.startActivity(intent)
        }
    }

}