package com.kevin.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.repository.NotebookRepository
import com.kevin.mvvm.repository.PictureRepository
import kotlinx.coroutines.launch

class NotebookViewModel : ViewModel() {

    var notebooks = MutableLiveData<Result<MutableList<Notebook>>?>()

    fun getNoteBooks() {
        viewModelScope.launch {
            val result = Result.success(NotebookRepository.getNotebooks())
            // 发射数据，之后观察者就会收到数据，放在子线程中
            notebooks.postValue(result)
        }
    }

    /**
     * 删除笔记
     */
    fun deleteNotebook(vararg notebook: Notebook?) {
        NotebookRepository.deleteNotebook(*notebook as Array<out Notebook>)
    }

//    /**
//     * 搜索笔记
//     * @param input 输入内容
//     */
//    fun searchNotebook(input: String?) {
//        notebooks = NotebookRepository.searchNotebook(input)
//    }

}