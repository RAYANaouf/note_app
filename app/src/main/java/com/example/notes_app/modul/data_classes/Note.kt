package com.example.notes_app.modul.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note {

    @PrimaryKey
    var id      : Int
    var cat_id  :Int
    var title   : String
    var content : String

    constructor(id : Int , cat_id : Int , title : String , content : String ){
        this.id = id
        this.cat_id = cat_id
        this.title = title
        this.content = content
    }

}