package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["diaryId", "hashtagId"],
    foreignKeys = [
        ForeignKey(entity = Note::class, parentColumns = ["id"], childColumns = ["diaryId"]),
        ForeignKey(entity = Hashtag::class, parentColumns = ["hashtag"], childColumns = ["hashtagId"])
    ]
)
class  DiaryHashtagJoin{
    val diaryId: Int
    val hashtagId: String

    constructor(diaryId : Int , hashtagId : String){
        this.diaryId = diaryId
        this.hashtagId = hashtagId
    }
}