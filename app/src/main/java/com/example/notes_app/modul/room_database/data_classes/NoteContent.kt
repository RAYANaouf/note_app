package com.example.notes_app.modul.room_database.data_classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "NoteContent" , foreignKeys = [ForeignKey(entity = Note::class , parentColumns = ["id"] , childColumns = ["note_id"])])
class NoteContent {

    @PrimaryKey(autoGenerate = true)
    var id : Int
    var note_id : Long
    var type : Int
    var type_count : Int
    var glonal_count : Int
    var cont : String
    var is_check: Boolean

    constructor(id : Int = 0  , note_id : Long = 0 ,type : Int , type_count : Int = 0 , glonal_count : Int =0 , cont:String = "" , is_check : Boolean = false){
        this.id = id
        this.note_id = note_id
        this.type = type
        this.type_count = type_count
        this.glonal_count = glonal_count
        this.cont = cont
        this.is_check = is_check
    }
}