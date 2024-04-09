package com.example.classpacket.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ViewRepository

    init {
        val dbRepo = CpDatabase.getDatabase(application).cpDao()
        repository = ViewRepository(dbRepo)
    }
    fun readAllDataUser(user: String): LiveData<List<Phishing>> {
        return repository.readAllDataUser(user)
    }

    fun addItem(packet: Phishing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(packet)
        }
    }

    fun updateItem(packet: Phishing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(packet)
        }
    }

    fun deleteItem(packet: Phishing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePacket(packet)
        }
    }

    fun deleteAll(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPackets(username)
        }
    }






    /* USER */

    fun findUser(username: String, password: String): User {
        return repository.findUser(username, password)
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }
}