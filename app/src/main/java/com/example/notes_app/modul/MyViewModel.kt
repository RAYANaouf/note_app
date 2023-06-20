package com.example.notes_app.modul

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.Note
import com.example.notes_app.modul.room_database.data_classes.NoteContent
import com.example.notes_app.modul.room_database.data_classes.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyViewModel : AndroidViewModel {

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

    fun addNote(note: Note):Deferred<Long>{
        return viewModelScope.async {
            m_repo.addNote(note)
        }
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

    fun getNoteById(id : Int ) : LiveData<Note>{
        return m_repo.getNoteById(id)
    }


    fun addNoteContent(noteContent: NoteContent){
        GlobalScope.launch{
            m_repo.addNoteContent(noteContent)
        }
    }

    fun deleteNoteContent(noteContent : NoteContent){
        GlobalScope.launch {
            m_repo.deleteNoteContent(noteContent)
        }
    }

    fun updateNoteContent(noteCotent : NoteContent){
        GlobalScope.launch {
            m_repo.updateNoteContent(noteCotent)
        }
    }

    fun getAllNoteContents(noteId : Int): LiveData<List<NoteContent>>{
        return m_repo.getAllNoteContents(noteId)
    }

    suspend fun getUserByEmail( userEmail : String): User {
        var user = GlobalScope.async {
            m_repo.getUserByMail(userEmail)
        }.await()
        return user;
    }

    fun addUser(user : User){
        m_repo.addUser(user)
    }



}