package com.example.notes_app.modul.room_database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes_app.modul.room_database.data_classes.Category

@Dao
interface CategoryDAO {

    @Insert
    fun addCategory(category: Category)

    @Delete
    fun deleteCategory(category : Category)

    @Update
    fun updateCategory(category : Category)

    @Query("select * from Category")
    fun getAllCategories() : LiveData<List<Category>>

}