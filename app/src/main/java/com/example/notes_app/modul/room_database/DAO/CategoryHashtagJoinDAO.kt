package com.example.notes_app.modul.room_database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notes_app.modul.room_database.data_classes.CategoryHashtagJoin

@Dao
interface CategoryHashtagJoinDAO {
    @Insert
    fun addCategoryHashtagJoin(categoryHashtagJoin: CategoryHashtagJoin)

    @Query("select * from  CategoryHashtagJoin where categoryId = :categoryId")
    fun getHashtagsByCategoryId(categoryId : Long ) : List<CategoryHashtagJoin>

    @Query("select * from  CategoryHashtagJoin where hashtagId LIKE '%' || :hashtag || '%'")
    fun getCategoryHashtagRelationByHashtag(hashtag : String ) : List<CategoryHashtagJoin>
}