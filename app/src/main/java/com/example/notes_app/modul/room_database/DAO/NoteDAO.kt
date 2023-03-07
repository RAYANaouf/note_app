package com.example.notes_app.modul.room_database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes_app.modul.data_classes.Note

@Dao
interface NoteDAO {

    @Insert
    fun addNote(note: Note)

    @Delete
    fun deleteNote(note : Note)

    @Update
    fun updateNote(note : Note)

    @Query("select * from Note")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("select * from Note where cat_id=:cat_id ")
    fun getNoteByCategory(cat_id : Int):LiveData<List<Note>>

}