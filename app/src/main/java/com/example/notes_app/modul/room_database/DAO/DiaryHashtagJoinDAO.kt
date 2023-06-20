package com.example.notes_app.modul.room_database.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.notes_app.modul.room_database.data_classes.DiaryHashtagJoin
import com.example.notes_app.modul.room_database.data_classes.Hashtag

@Dao
interface DiaryHashtagJoinDAO {

    @Query("select * from  DiaryHashtagJoin where diaryId = :diaryId")
    fun getHashtagsByDiaryId(diaryId : Int ) : List<DiaryHashtagJoin>

}