package com.example.notes_app.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notes_app.R

const val NOTIFICATIONCHANNEL_ID="note_alarm_channel"

class CreateNotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            var notificationChannel = NotificationChannel(NOTIFICATIONCHANNEL_ID , "note alarm" , NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description="that channel is holding that alarms that is set to the notes in that app "
            var nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(notificationChannel)
        }
        var nBuider = NotificationCompat.Builder(this , NOTIFICATIONCHANNEL_ID)
        nBuider.setContentTitle("hiiii")
        nBuider.setContentText("ygfcfgtygfc")
        nBuider.setSmallIcon(R.drawable.add_alarm_blue)

        val nmCompat = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        }
        nmCompat.notify(10,nBuider.build())

        // Stop the service
        stopSelf()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}