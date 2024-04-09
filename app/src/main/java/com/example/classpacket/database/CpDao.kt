package com.example.classpacket.database


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CpDao {
    //Packets
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(packet: Phishing)

    @Update
    suspend fun updatePacket(packet: Phishing)

    @Delete
    suspend fun deletePacket(packet: Phishing)

    @Query("Select * from phishing_table where username = :username order by uid asc")
    fun readAllData(username: String): LiveData<List<Phishing>>

    @Query("DELETE FROM phishing_table WHERE username = :username")
    suspend fun deleteAllPacket(username: String)



    //USers
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("Select * from user_table where username = :username and password = :password")
    fun findUser(username: String, password: String): User


}
