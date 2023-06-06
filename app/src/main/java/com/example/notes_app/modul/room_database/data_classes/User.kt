package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {

    @PrimaryKey(autoGenerate = true)
    var user_id  : Int = 0
    var f_name   : String = ""
    var l_name   : String = ""
    var email    : String = ""
    var password : String = ""
    var photo    : String = ""

    constructor(){}

    constructor(id : Int = 0 , f_name : String , l_name : String , email : String = "" , password :  String , photo : String =""){
        this.user_id     = id
        this.email  = email
        this.f_name = f_name
        this.l_name = l_name
        this.photo   = photo
        this.password = password
    }
}