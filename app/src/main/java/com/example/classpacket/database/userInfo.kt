package com.example.classpacket.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "user_table")
class User {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "full_name")
    var fullName: String = ""

    @ColumnInfo(name = "email")
    var email: String = ""

    @ColumnInfo(name = "username")
    var username: String = ""

    @ColumnInfo(name = "password")
    var password: String = ""
}