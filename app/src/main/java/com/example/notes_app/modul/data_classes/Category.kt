package com.example.notes_app.modul.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category {

    @PrimaryKey
    var id : Int
    var image : String
    var name  : String
    var description : String

    constructor(id: Int, image: String, name: String, description : String) {
        this.id = id
        this.image = image
        this.name = name
        this.description = description
    }
}