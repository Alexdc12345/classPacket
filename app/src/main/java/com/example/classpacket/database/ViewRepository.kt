package com.example.classpacket.database

import androidx.lifecycle.LiveData

class ViewRepository(private val cpDao : CpDao) {
    fun readAllDataUser(user: String): LiveData<List<Phishing>>{
        return cpDao.readAllData(user)
    }

    suspend fun addItem(packet: Phishing){
        cpDao.insert(packet)
    }

    suspend fun updateItem(packet: Phishing){
        cpDao.updatePacket(packet)
    }

    suspend fun deletePacket(packet: Phishing){
        cpDao.deletePacket(packet)
    }

    suspend fun deleteAllPackets(username: String) {
        cpDao.deleteAllPacket(username)
    }



    /* USER */

    fun findUser(username: String, password: String): User {
            return cpDao.findUser(username,password)
        }

    suspend fun addUser(user: User){
            cpDao.insertUser(user)
        }
}