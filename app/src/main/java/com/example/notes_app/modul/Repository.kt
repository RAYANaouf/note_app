package com.example.notes_app.modul

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.DAO.CategoryDAO
import com.example.notes_app.modul.room_database.DAO.NoteContentDAO
import com.example.notes_app.modul.room_database.DAO.NoteDAO
import com.example.notes_app.modul.room_database.MyDatabase
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import kotlinx.coroutines.*

class Repository {

    private  var m_noteDAO : NoteDAO
    private  var m_noteContentDAO : NoteContentDAO
    private  var m_categoryDAO : CategoryDAO

    constructor(context: Context){
        var database = MyDatabase.getDatabase(context)
        m_categoryDAO = database.categoryDAO()
        m_noteDAO = database.noteDAO()
        m_noteContentDAO = database.noteContentDAO()
    }

    fun get_noteDao():NoteDAO=m_noteDAO
    fun get_noteContenDao():NoteContentDAO=m_noteContentDAO
    fun get_categoryDAO():CategoryDAO=m_categoryDAO

    fun addCategory(category: Category){
        GlobalScope.launch {
            m_categoryDAO.addCategory(category)
        }
    }

    fun deleteCategory(category : Category){
        GlobalScope.launch {
            m_categoryDAO.deleteCategory(category)
        }
    }

    fun updateCategory(category : Category){
        GlobalScope.launch {
            m_categoryDAO.updateCategory(category)
        }
    }

    fun getAllCategories() : LiveData<List<Category>>{
        return m_categoryDAO.getAllCategories()
    }

    suspend fun addNoteContent(notecontent: NoteContent) {

        return withContext(Dispatchers.IO)  {
            m_noteContentDAO.addNoteContent(notecontent)
        }
    }

     fun deleteNoteContent(noteContent : NoteContent){
         GlobalScope.launch {
             m_noteContentDAO.deleteNoteContent(noteContent)
         }
     }

    fun updateNoteContent(noteContent  : NoteContent){
        GlobalScope.launch {
         m_noteContentDAO.updateNoteContent(noteContent )
        }
    }

    fun getAllNoteContents(note_id:Int) : LiveData<List<NoteContent>>{
        return m_noteContentDAO.getAllNoteContents(note_id )
    }


    suspend fun addNote(note: Note) : Long{

        return withContext(Dispatchers.IO)  {
            m_noteDAO.addNote(note)
        }
    }

    fun deleteNote(note : Note){
        GlobalScope.launch {
            m_noteDAO.deleteNote(note)
        }
    }

    fun updateNote(note : Note){
        GlobalScope.launch {
            m_noteDAO.updateNote(note)
        }
    }

    fun getAllNotes() : LiveData<List<Note>>{
        return m_noteDAO.getAllNotes()
    }


    fun getNoteByCategory(cat_id : Int):LiveData<List<Note>>{
        return m_noteDAO.getNoteByCategory(cat_id)
    }

}