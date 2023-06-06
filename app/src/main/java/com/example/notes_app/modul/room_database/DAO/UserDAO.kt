package com.example.notes_app.modul.room_database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes_app.modul.room_database.data_classes.User

@Dao
interface UserDAO {

    @Insert
    fun addUser(user: User)

    @Delete
    fun deleteUser(user : User)

    @Update
    fun updateUser(user : User)

    @Query("select * from User")
    fun getAllUsers() : LiveData<List<User>>

    @Query("select * from User where email=:user_email")
    fun getUserByMail(user_email :String) : User

}