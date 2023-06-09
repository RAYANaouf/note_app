package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
class Note {

    @PrimaryKey(autoGenerate = true)
    var id      : Long
    var cat_id  :Long
    var date    : String
    var lock    : Boolean
    var rate    : Float
    var title   : String
    var description : String
    var icon : Int
    var color   : Int
    var theme   : Int

    constructor(id : Long = 0 , cat_id : Long = 1  , date : String  = "MM/DD/YY", lock : Boolean = false , rate : Float = 0F , color : Int = -9408400 , icon : Int=0 , theme : Int = 0,  title : String , description : String ){
        this.id          = id
        this.cat_id      = cat_id
        this.date        = date
        this.lock        = lock
        this.rate        = rate
        this.color       = color
        this.icon        = icon
        this.theme       = theme
        this.title       = title
        this.description = description
    }

}