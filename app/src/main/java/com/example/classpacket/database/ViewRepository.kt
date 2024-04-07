package com.example.classpacket.database

class ViewRepository(private val cpDao : CpDao) {
        fun findUser(username: String, password: String): User {
            return cpDao.findUser(username,password)
        }

        suspend fun addUser(user: User){
            cpDao.insertUser(user)
        }
}