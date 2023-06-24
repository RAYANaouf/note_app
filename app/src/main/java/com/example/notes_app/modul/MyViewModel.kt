package com.example.notes_app.modul

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes_app.modul.room_database.DAO.CategoryHashtagJoinDAO
import com.example.notes_app.modul.room_database.data_classes.*
import kotlinx.coroutines.*

class MyViewModel : AndroidViewModel {

    private var m_repo : Repository

    constructor(application : Application):super(application){
        m_repo = Repository(application.baseContext)
    }

    suspend fun addCategory(category: Category):Long{
        return  GlobalScope.async { m_repo.addCategory(category) }.await()
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


    fun getNoteByCategory(cat_id : Long): LiveData<List<Note>> {
        return m_repo.getNoteByCategory(cat_id)
    }

    fun getNoteById(id : Long ) : LiveData<Note>{
        return m_repo.getNoteById(id)
    }

    fun getNoteByIdRow(id : Long ) : Note{
        return m_repo.getNoteByIdRow(id)
    }

    fun getNoteByDate( date : String):Note{
        return m_repo.getNoteByDate(date)
    }

    fun getNoteByTitle(title : String):List<Note>{
        return m_repo.getNoteByTitle( title )
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

    fun getAllNoteContents(noteId : Long): List<NoteContent>{
        return m_repo.getAllNoteContents(noteId)
    }

    fun getNoteContentsByText(text : String) :List<NoteContent>{
        return m_repo.getNoteContentsByText(text)
    }

    suspend fun getUserByEmail( userEmail : String): User {
        var user = GlobalScope.async {
            m_repo.getUserByMail(userEmail)
        }.await()
        return user;
    }

    fun addUser(user : User):Job{
        return m_repo.addUser(user)
    }

    fun updateUser(user : User):Job{
        return m_repo.updateUser(user)
    }

    fun addHashtag(hashtag : Hashtag):Job{
        return GlobalScope.launch {
            m_repo.addHashtag(hashtag)
        }
    }

    fun deleteHashtag(hashtag : Hashtag){
        GlobalScope.launch {
            m_repo.deleteHashtag(hashtag)
        }
    }

    fun getAllHashtags() : List<Hashtag> {
        return m_repo.getAllHashtags()
    }

    fun getHashtagsByDiaryId(diaryId : Long ) : List<DiaryHashtagJoin>{
        return m_repo.getHashtagsByDiaryId(diaryId)
    }

    suspend fun getHashtagById(hashtag: String) : Hashtag{
        return GlobalScope.async { m_repo.getHashtagById(hashtag) }.await()
    }

    fun isHashtagExist(hashtag : String): Int{
        return m_repo.isHashtagExist(hashtag)
    }

    fun addDiaryHashtagJoin(diaryHashtagJoin : DiaryHashtagJoin){
        GlobalScope.launch {
            m_repo.addDiaryHashtagJoin(diaryHashtagJoin)
        }
    }

    fun getDiaryHashtagRelationByHashtag(hashtag : String ) : List<DiaryHashtagJoin>{
        return m_repo.getDiaryHashtagRelationByHashtag(hashtag)
    }


    fun addCategoryHashtagJoin(categoryHashtagJoin: CategoryHashtagJoin):Job{
        return GlobalScope.launch {
            m_repo.addCategoryHashtagJoin(categoryHashtagJoin)
        }
    }

    suspend fun getHashtagsByCategoryId(categoryId : Long ) : List<CategoryHashtagJoin>{
        return GlobalScope.async { m_repo.getHashtagsByCategoryId(categoryId) }.await()
    }

    suspend fun getCategoryHashtagRelationByHashtag(hashtag : String ) : List<CategoryHashtagJoin>{
        return GlobalScope.async { m_repo.getCategoryHashtagRelationByHashtag(hashtag) }.await()
    }


}