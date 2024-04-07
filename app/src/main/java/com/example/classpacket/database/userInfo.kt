package com.example.classpacket.database


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val Id: Int,
    val fullName: String,
    val email: String,
    val username: String,
    val password: String

): Parcelable