package com.example.notes_app.ui.interfaces

interface OnClickNavigator {
    fun onClick_to_notesFragment(cat_id : Long)
    fun onClick_to_addNoteFragment(rating : Float)

    fun onClick_to_diaryFragment(diary_id : Long , rate : Float )
}