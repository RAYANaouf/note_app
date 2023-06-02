package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Category::class , parentColumns = [ "id" ] , childColumns = ["cat_id"] ) ] )
class Note {

    @PrimaryKey(autoGenerate = true)
    var id      : Int
    var cat_id  :Int
    var date    : String
    var title   : String
    var content : String

    constructor(id : Int = 0 , cat_id : Int  , date : String  = "MM/DD/YY", title : String , content : String ){
        this.id      = id
        this.cat_id  = cat_id
        this.date    = date
        this.title   = title
        this.content = content
    }

}