package com.kevin.mvvm.repository

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

}
