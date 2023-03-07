package com.example.notes_app.modul

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes_app.modul.data_classes.Category
import com.example.notes_app.modul.data_classes.Note
import com.example.notes_app.modul.room_database.DAO.CategoryDAO
import com.example.notes_app.modul.room_database.DAO.NoteDAO
import com.example.notes_app.modul.room_database.MyDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Repository {

    private  var m_noteDAO : NoteDAO
    private  var m_categoryDAO : CategoryDAO

    constructor(context: Context){
        var database = MyDatabase.getDatabase(context)
        m_categoryDAO = database.categoryDAO()
        m_noteDAO = database.noteDAO()
    }

    fun get_noteDao():NoteDAO=m_noteDAO
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

    fun addNote(note: Note){
        GlobalScope.launch {
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