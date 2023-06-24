package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["categoryId", "hashtagId"],
    foreignKeys = [
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"]),
        ForeignKey(entity = Hashtag::class, parentColumns = ["hashtag"], childColumns = ["hashtagId"])
    ]
)
class CategoryHashtagJoin  {
    val categoryId: Long
    val hashtagId: String

    constructor(categoryId : Long , hashtagId : String){
        this.categoryId = categoryId
        this.hashtagId = hashtagId
    }

}