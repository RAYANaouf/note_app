package com.example.notes_app.modul.room_database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes_app.modul.room_database.data_classes.Note

@Dao
interface NoteDAO {

    @Insert
    fun addNote(note: Note):Long

    @Delete
    fun deleteNote(note : Note)

    @Update
    fun updateNote(note : Note)

    @Query("select * from Note ORDER BY id DESC ")
    fun getAllNotes() : LiveData<List<Note>>

    @Query("select * from Note where id=:id ")
    fun getNoteById(id : Int):LiveData<Note>

    @Query("select * from Note where cat_id=:cat_id ORDER BY id DESC ")
    fun getNoteByCategory(cat_id : Int):LiveData<List<Note>>

}