package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Hashtag {
    @PrimaryKey
    var hashtag : String

    constructor(hashtag : String){
        this.hashtag = hashtag
    }
}