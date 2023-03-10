package com.example.notes_app.modul

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.DAO.CategoryDAO
import com.example.notes_app.modul.room_database.DAO.NoteDAO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyViewModul : AndroidViewModel {

    private var m_repo : Repository

    constructor(application : Application):super(application){
        m_repo = Repository(application.baseContext)
    }

    fun addCategory(category: Category){
        m_repo.addCategory(category)
    }

    fun deleteCategory(category : Category){
        m_repo.deleteCategory(category)
    }

    fun updateCategory(category : Category){
        m_repo.updateCategory(category)
    }

    fun getAllCategories() : LiveData<List<Category>> {
        return m_repo.getAllCategories()
    }

    fun addNote(note: Note){
        m_repo.addNote(note)
    }

    fun deleteNote(note : Note){
        m_repo.deleteNote(note)
    }

    fun updateNote(note : Note){
        m_repo.updateNote(note)
    }

    fun getAllNotes() : LiveData<List<Note>> {
        return m_repo.getAllNotes()
    }


    fun getNoteByCategory(cat_id : Int): LiveData<List<Note>> {
        return m_repo.getNoteByCategory(cat_id)
    }



}