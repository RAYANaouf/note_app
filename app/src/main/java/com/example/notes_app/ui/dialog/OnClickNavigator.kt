package com.example.notes_app.ui.dialog

import android.graphics.Bitmap
import android.net.Uri

interface OnClickNavigator {
    fun onClickGetPicture():Uri?
    fun onClick_to_notesFragment()
    fun onClick_addCategory(name : String , des : String , image : Bitmap)
}