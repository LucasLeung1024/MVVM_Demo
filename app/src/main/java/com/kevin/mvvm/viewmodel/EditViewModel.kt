package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.repository.NotebookRepository


class EditViewModel : ViewModel() {

    fun addNotebook(notebook: Notebook) {
       NotebookRepository.saveNotebook(notebook)
    }

    var notebook: MutableLiveData<Notebook>? = MutableLiveData()

    /**
     * 根据Id搜索笔记
     */
    fun queryById(uid: Int) {
        notebook!!.value = NotebookRepository.getNotebookById(uid)
    }

    /**
     * 更新笔记
     */
    fun updateNotebook(notebook: Notebook?) {
        NotebookRepository.updateNotebook(notebook!!)
    }

    /**
     * 删除笔记
     */
    fun deleteNotebook(notebook: Notebook?) {
        NotebookRepository.deleteNotebook(notebook!!)
    }


}