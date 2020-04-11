package com.boss.login.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notes::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    // equivalent to public static, i.e it can be accessed outside the class
    companion object {
        lateinit var INSTANCE: NotesDatabase
        fun getInstance(context: Context): NotesDatabase {
            // sync means only one thread can access the whole db class
            synchronized(NotesDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, NotesDatabase::class.java, "my-notes.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE
        }
    }
}