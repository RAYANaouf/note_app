package com.example.notes_app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.notes_app.classes.RegesterHandler

class ActivateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        var regesterHandler =RegesterHandler(context)
        regesterHandler.activate()
        Toast.makeText(context , "fiha khir " , Toast.LENGTH_LONG).show()

    }
}