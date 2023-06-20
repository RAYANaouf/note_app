package com.example.notes_app.modul

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.DAO.CategoryDAO
import com.example.notes_app.modul.room_database.DAO.NoteContentDAO
import com.example.notes_app.modul.room_database.DAO.NoteDAO
import com.example.notes_app.modul.room_database.DAO.UserDAO
import com.example.notes_app.modul.room_database.MyDatabase
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import com.example.notes_app.modul.room_database.data_classes.User
import kotlinx.coroutines.*

class Repository {

    private  var m_noteDAO        : NoteDAO
    private  var m_noteContentDAO : NoteContentDAO
    private  var m_categoryDAO    : CategoryDAO
    private  var m_userDAO        : UserDAO

    constructor(context: Context){
        var database     = MyDatabase.getDatabase(context)
        m_categoryDAO    = database.categoryDAO()
        m_noteDAO        = database.noteDAO()
        m_noteContentDAO = database.noteContentDAO()
        m_userDAO        = database.UserDAO()
    }

    fun get_noteDao():NoteDAO              =m_noteDAO
    fun get_noteContenDao():NoteContentDAO =m_noteContentDAO
    fun get_categoryDAO():CategoryDAO      =m_categoryDAO
    fun get_UserDAO():UserDAO              =m_userDAO

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

    fun getNoteById(id : Int): LiveData<Note>{
        return m_noteDAO.getNoteById(id)
    }


    fun getNoteByCategory(cat_id : Int):LiveData<List<Note>>{
        return m_noteDAO.getNoteByCategory(cat_id)
    }

    fun getUserByMail(userEmail : String):User{
        return m_userDAO.getUserByMail(userEmail)
    }

    fun addUser(user : User){
        GlobalScope.launch {
            m_userDAO.addUser(user)
        }
    }

}