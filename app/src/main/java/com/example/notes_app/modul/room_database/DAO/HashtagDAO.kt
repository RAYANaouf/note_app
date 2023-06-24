package com.example.notes_app.modul.room_database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.notes_app.modul.room_database.data_classes.Hashtag

@Dao
interface HashtagDAO {

    @Insert
    fun addHashtag(hashtag : Hashtag):Long

    @Delete
    fun deleteHashtag(hashtag: Hashtag)

    @Query("select * from Hashtag WHERE  hashtag =:hashtag ")
    fun getHashtagById(hashtag: String) : Hashtag

    @Query("select * from Hashtag ORDER BY hashtag ")
    fun getAllHashtags() : List<Hashtag>


    @Query("SELECT COUNT(hashtag) FROM Hashtag WHERE hashtag = :hashtag")
    fun isHashtagExist(hashtag : String): Int

}