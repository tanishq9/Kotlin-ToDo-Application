package com.boss.login

import android.app.Application
import com.boss.login.db.NotesDatabase

class NotesApp : Application() {
    // Load DB instance here, so that it will be a singleton variable
    // i.e it's instance will be created only once in the android app lifecycle

    override fun onCreate() {
        super.onCreate()
    }

    fun getNotesDB(): NotesDatabase {
        return NotesDatabase.getInstance(this)
    }

}