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
    private  var m_categoryHashtagJoinDAO  : CategoryHashtagJoinDAO


    constructor(context: Context){
        var database             = MyDatabase.getDatabase(context)
        m_categoryDAO            = database.categoryDAO()
        m_noteDAO                = database.noteDAO()
        m_noteContentDAO         = database.noteContentDAO()
        m_userDAO                = database.UserDAO()
        m_hashtagDAO             = database.hashtagDAO()
        m_diaryHashtagJoinDAO    = database.diaryHashtagJoinDAO()
        m_categoryHashtagJoinDAO = database.categoryHashtagJoinDAO()
    }

     fun addCategory(category: Category):Long{

        return  m_categoryDAO.addCategory(category)

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

    fun getAllNoteContents(note_id:Long) : List<NoteContent>{
        return m_noteContentDAO.getAllNoteContents(note_id )
    }

    fun getNoteContentsByText(text : String) :List<NoteContent>{
        return m_noteContentDAO.getNoteContentsByText(text)
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

    fun getNoteById(id : Long): LiveData<Note>{
        return m_noteDAO.getNoteById(id)
    }

    fun getNoteByIdRow(id : Long): Note{
        return m_noteDAO.getNoteByIdRow(id)
    }


    fun getNoteByDate( date : String):Note{
        return m_noteDAO.getNoteByDate(date)
    }

    fun getNoteByTitle(title : String):List<Note>{
        return m_noteDAO.getNoteByTitle( title )
    }

    fun getNoteByCategory(cat_id : Long):LiveData<List<Note>>{
        return m_noteDAO.getNoteByCategory(cat_id)
    }

    fun getUserByMail(userEmail : String):User{
        return m_userDAO.getUserByMail(userEmail)
    }

    fun addUser(user : User):Job{
        return GlobalScope.launch {
            m_userDAO.addUser(user)
        }
    }

    fun updateUser(user : User):Job{
       return GlobalScope.launch {
           m_userDAO.updateUser(user)
       }
    }

    fun addHashtag(hashtag : Hashtag){
            m_hashtagDAO.addHashtag(hashtag)
    }

    fun deleteHashtag(hashtag : Hashtag){
        GlobalScope.launch {
            m_hashtagDAO.deleteHashtag(hashtag)
        }
    }

    fun getAllHashtags() : List<Hashtag>{
        return m_hashtagDAO.getAllHashtags()
    }

    fun getHashtagsByDiaryId(diaryId : Long ) : List<DiaryHashtagJoin>{
        return m_diaryHashtagJoinDAO.getHashtagsByDiaryId(diaryId)
    }

    fun getHashtagById(hashtag: String) : Hashtag{
        return m_hashtagDAO.getHashtagById(hashtag)
    }

    fun isHashtagExist(hashtag : String): Int{
        return m_hashtagDAO.isHashtagExist(hashtag)
    }

    fun addDiaryHashtagJoin(diaryHashtagJoin : DiaryHashtagJoin){
        m_diaryHashtagJoinDAO.addDiaryHashtagJoin(diaryHashtagJoin)
    }

    fun getDiaryHashtagRelationByHashtag(hashtag : String ) : List<DiaryHashtagJoin>{
        return m_diaryHashtagJoinDAO.getDiaryHashtagRelationByHashtag(hashtag)
    }

    fun addCategoryHashtagJoin(categoryHashtagJoin: CategoryHashtagJoin){
        return m_categoryHashtagJoinDAO.addCategoryHashtagJoin(categoryHashtagJoin)
    }

    fun getHashtagsByCategoryId(categoryId : Long ) : List<CategoryHashtagJoin>{
        return m_categoryHashtagJoinDAO.getHashtagsByCategoryId(categoryId)
    }

    fun getCategoryHashtagRelationByHashtag(hashtag : String ) : List<CategoryHashtagJoin>{
        return m_categoryHashtagJoinDAO.getCategoryHashtagRelationByHashtag(hashtag)
    }

}