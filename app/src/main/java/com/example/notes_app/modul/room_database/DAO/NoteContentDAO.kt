package com.example.notes_app.modul.room_database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes_app.modul.room_database.data_classes.NoteContent


@Dao
interface NoteContentDAO {

    @Insert
    fun addNoteContent(content : NoteContent)

    @Update
    fun updateNoteContent(content : NoteContent)

    @Delete
    fun deleteNoteContent(content: NoteContent)

    @Query("Select * from NoteContent where note_id=:noteId")
    fun getAllNoteContents(noteId : Long) :List<NoteContent>

    @Query("Select * from NoteContent where cont like '%' || :text || '%'")
    fun getNoteContentsByText(text : String) :List<NoteContent>
}