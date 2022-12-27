package com.kevin.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.repository.NotebookRepository

class NotebookViewModel : ViewModel() {

    var notebooks = MutableLiveData<Result<List<Notebook>>>()

    fun getNoteBooks(){
        notebooks = NotebookRepository.getNotebooks() as MutableLiveData<Result<List<Notebook>>>
    }

}