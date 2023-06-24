package com.example.notes_app.classes

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.notes_app.modul.room_database.data_classes.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val USER_DATA="data"
private const val IS_CONNECTED="is_connected"
private const val CONN_USERS = "connected_users"
private const val CONN_USER  = "connected_user"
private const val IS_ACTIVE  = "active"

class RegesterHandler {

    private var m_context : Context
    private var m_data :SharedPreferences
    private val m_editor : SharedPreferences.Editor

    constructor(context : Context){
        this.m_context = context
        m_data =  context.getSharedPreferences(USER_DATA,Context.MODE_PRIVATE)
        m_editor       = m_data.edit()
    }

    fun isLoggin():Boolean{
        return m_data.getBoolean(IS_CONNECTED,false)
    }

    fun isActive():Boolean{
        return m_data.getBoolean(IS_ACTIVE,true)
    }


    fun activate(){
        m_editor.putBoolean(IS_ACTIVE,true)
        m_editor.apply()
    }

    fun deactivate(){
        m_editor.putBoolean(IS_ACTIVE,false)
        m_editor.apply()
    }


    fun conn_user():String{
        return m_data.getString(CONN_USER,"") ?: ""
    }

    fun change_user_email(email: String){
         m_editor.putString(CONN_USER,email)
        m_editor.apply()
    }

    fun deconnecting(){
        m_editor.putBoolean(IS_CONNECTED,false)
        m_editor.putString(CONN_USER,"")
        m_editor.apply()
    }

    fun connecting(email : String ){
        //string format
        var users_string = m_data.getString(CONN_USERS,"")

        var users: ArrayList<String> = ArrayList<String>()

        if (users_string != ""){

            // Convert the JSON string back to a array list of email
            var type  = object : TypeToken<ArrayList<String>>() {}.type;
             users = Gson().fromJson(users_string, type)


        }

        //add the user
        users.add(email)

        //back to string format
        users_string = Gson().toJson(users)

        m_editor.putString(CONN_USERS,users_string)
        m_editor.apply()


        //make hem/her the connected User
        m_editor.putString(CONN_USER,email)
        m_editor.putBoolean(IS_CONNECTED,true)
        m_editor.apply()

    }

}