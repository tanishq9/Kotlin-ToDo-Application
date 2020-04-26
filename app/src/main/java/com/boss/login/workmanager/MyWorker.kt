package com.boss.login.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.boss.login.NotesApp

class MyWorker(val context: Context, val workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    // this worker will be used to delete completed tasks after every 15 minutes
    override fun doWork(): Result {
        val notesApp = applicationContext as NotesApp
        val notesDao = notesApp.getNotesDB().notesDao()
        notesDao.deleteNotes(true)
        return Result.success()
    }
}