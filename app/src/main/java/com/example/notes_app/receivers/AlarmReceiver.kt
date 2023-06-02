package com.example.notes_app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.notes_app.services.CreateNotificationService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context , "notify \n notify \n notify " , Toast.LENGTH_LONG).show()
        var intent = Intent(context , CreateNotificationService::class.java)
        context.startService(intent)
    }
}