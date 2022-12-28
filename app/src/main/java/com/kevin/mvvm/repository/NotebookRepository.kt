package com.kevin.mvvm.repository

import com.kevin.mvvm.BaseApplication
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.db.bean.WallPaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object NotebookRepository : BaseRepository() {

    private val TAG = UserRepository::class.java.simpleName

    var noteBookList: MutableList<Notebook> = arrayListOf()

    /**
     * 添加笔记
     */
    fun saveNotebook(notebook : Notebook ) {
        BaseApplication.db.notebookDao().insert(notebook)
    }

    /**
     * 获取所有笔记
     */
    suspend fun getNotebooks(): MutableList<Notebook> {
        withContext(Dispatchers.IO) {
            noteBookList = BaseApplication.db.notebookDao().getAll()
        }
        return noteBookList
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
//    fun deleteNotebook(notebook: Notebook) {
//        BaseApplication.db.notebookDao().delete(notebook)
//    }
    //这个表示删除一个笔记也可以，删除多个笔记也可以
    fun deleteNotebook(vararg notebook: Notebook) {
        BaseApplication.db.notebookDao().delete(*notebook)
    }



}
