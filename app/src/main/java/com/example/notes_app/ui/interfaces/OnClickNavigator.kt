package com.example.notes_app.ui.interfaces

interface OnClickNavigator {
    fun onClick_to_notesFragment(cat_id : Int)
    fun onClick_to_addNoteFragment(rating : Float)
}