package com.example.classpacket.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class CpDatabase : RoomDatabase() {
    abstract fun cpDao(): CpDao

    companion object {
        @Volatile
        private var INSTANCE: CpDatabase? = null

        fun getDatabase(context: Context): CpDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CpDatabase::class.java,
                    "cp_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
