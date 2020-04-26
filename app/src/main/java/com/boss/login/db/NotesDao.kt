package com.boss.login.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

// data access objects - dao
// it helps us to work with data
// example : to add to the database, update the data, etc.
@Dao
interface NotesDao {
    @Query("Select * from notesData")
    fun getAll(): List<Notes>

    @Insert(onConflict = REPLACE)
    fun insert(note: Notes)

    @Update
    fun update(note: Notes)

    @Delete
    fun delete(note: Notes)

    @Query("Delete FROM notesData WHERE isTaskCompleted= :status")
    fun deleteNotes(status: Boolean)
}
