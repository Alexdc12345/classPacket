package com.example.classpacket.database

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModel(application: Application): AndroidViewModel(application) {


    private val repository: ViewRepository

    init {
        val dbRepo = CpDatabase.getDatabase(application).cpDao()
        repository = ViewRepository(dbRepo)
    }

    fun findUser(username: String, password: String): User {
        return repository.findUser(username, password)
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }
}