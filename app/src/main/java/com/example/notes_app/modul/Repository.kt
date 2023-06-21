package com.example.notes_app.modul

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.notes_app.modul.room_database.DAO.*
import com.example.notes_app.modul.room_database.MyDatabase
import com.example.notes_app.modul.room_database.data_classes.*
import kotlinx.coroutines.*

class Repository {

    private  var m_noteDAO                 : NoteDAO
    private  var m_noteContentDAO          : NoteContentDAO
    private  var m_categoryDAO             : CategoryDAO
    private  var m_userDAO                 : UserDAO
    private  var m_hashtagDAO              : HashtagDAO
    private  var m_diaryHashtagJoinDAO     : DiaryHashtagJoinDAO


    constructor(context: Context){
        var database          = MyDatabase.getDatabase(context)
        m_categoryDAO         = database.categoryDAO()
        m_noteDAO             = database.noteDAO()
        m_noteContentDAO      = database.noteContentDAO()
        m_userDAO             = database.UserDAO()
        m_hashtagDAO          = database.hashtagDAO()
        m_diaryHashtagJoinDAO = database.diaryHashtagJoinDAO()
    }

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

    fun addHashtag(hashtag : Hashtag){
        GlobalScope.launch {
            m_hashtagDAO.addHashtag(hashtag)
        }
    }

    fun deleteHashtag(hashtag : Hashtag){
        GlobalScope.launch {
            m_hashtagDAO.deleteHashtag(hashtag)
        }
    }

    fun getAllHashtags() : List<Hashtag>{
        return m_hashtagDAO.getAllHashtags()
    }

    fun getHashtagsByDiaryId(diaryId : Int ) : List<DiaryHashtagJoin>{
        return m_diaryHashtagJoinDAO.getHashtagsByDiaryId(diaryId)
    }


    fun isHashtagExist(hashtag : String): Int{
        return m_hashtagDAO.isHashtagExist(hashtag)
    }

    fun addDiaryHashtagJoin(diaryHashtagJoin : DiaryHashtagJoin){
        m_diaryHashtagJoinDAO.addDiaryHashtagJoin(diaryHashtagJoin)
    }

}