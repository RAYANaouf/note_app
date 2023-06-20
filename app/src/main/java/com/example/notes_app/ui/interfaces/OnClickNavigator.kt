package com.example.notes_app.ui.interfaces

interface OnClickNavigator {
    fun onClick_to_notesFragment(cat_id : Int)
    fun onClick_to_addNoteFragment(rating : Float)

    fun onClick_to_diaryFragment(diary_id : Int , rate : Float )
}