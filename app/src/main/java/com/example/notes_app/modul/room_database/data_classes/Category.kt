package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category {

    @PrimaryKey(autoGenerate = true)
    var id : Int
    var image : String
    var name  : String
    var description : String

    constructor(id: Int = 0 , image: String, name: String, description : String) {
        this.id = id
        this.image = image
        this.name = name
        this.description = description
    }
}
