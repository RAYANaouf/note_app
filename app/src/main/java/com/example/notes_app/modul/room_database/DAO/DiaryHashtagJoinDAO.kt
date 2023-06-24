package com.example.notes_app.modul.room_database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notes_app.modul.room_database.data_classes.Category
import com.example.notes_app.modul.room_database.data_classes.DiaryHashtagJoin
import com.example.notes_app.modul.room_database.data_classes.Hashtag

@Dao
interface DiaryHashtagJoinDAO {

    @Insert
    fun addDiaryHashtagJoin(diaryHashtagJoin : DiaryHashtagJoin)

    @Query("select * from  DiaryHashtagJoin where diaryId = :diaryId")
    fun getHashtagsByDiaryId(diaryId : Long ) : List<DiaryHashtagJoin>

    @Query("select * from  DiaryHashtagJoin where hashtagId LIKE '%' || :hashtag || '%'")
    fun getDiaryHashtagRelationByHashtag(hashtag : String ) : List<DiaryHashtagJoin>

}