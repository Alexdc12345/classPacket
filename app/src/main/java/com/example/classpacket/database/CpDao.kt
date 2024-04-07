package com.example.classpacket.database


import androidx.room.*

@Dao
interface CpDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("Select * from user_table where username = :username and password = :password")
    fun findUser(username: String, password: String): User
}