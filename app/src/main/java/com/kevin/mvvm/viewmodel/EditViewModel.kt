package com.kevin.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kevin.mvvm.db.bean.Notebook
import com.kevin.mvvm.repository.NotebookRepository

class EditViewModel : ViewModel() {

    fun addNotebook(notebook: Notebook) {
       NotebookRepository.saveNotebook(notebook)
    }
}