package com.kevin.mvvm.repository

import androidx.lifecycle.MutableLiveData
import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.Notebook
import kotlinx.coroutines.Dispatchers


object NotebookRepository : BaseRepository() {

    private val TAG = UserRepository::class.java.simpleName

    /**
     * 添加笔记
     */
    fun saveNotebook(notebook : Notebook ) {
        BaseApplication.db.notebookDao().insert(notebook)
    }

    /**
     * 获取所有笔记
     */
    fun getNotebooks() = fire(Dispatchers.IO) {
        val notebookList = BaseApplication.db.notebookDao().getAll()
        Result.success(notebookList)
    }

    /**
     * 根据id获取笔记
     * @param uid id
     */
    fun getNotebookById(uid: Int): Notebook {
        return BaseApplication.db.notebookDao().findById(uid)
    }

    /**
     * 更新笔记
     *
     * @param notebook
     */
    fun updateNotebook(notebook: Notebook) {
        BaseApplication.db.notebookDao().update(notebook)
    }

    /**
     * 删除笔记
     *
     * @param notebook
     */
    fun deleteNotebook(notebook: Notebook) {
        BaseApplication.db.notebookDao().delete(notebook)
    }

}
