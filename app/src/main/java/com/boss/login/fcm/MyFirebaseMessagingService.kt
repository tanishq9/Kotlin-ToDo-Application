package com.boss.login.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.boss.login.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_login.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val tag = "MyFirebaseService"
    // in onMessageReceived function we receive all the messages that we sent from the console
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.e(tag, p0?.from)
        Log.e(tag, p0?.notification?.body)
        // based upon that message from console, we setup a notification
        setUpNotification(p0?.notification?.body)
    }

    private fun setUpNotification(body: String?) {
        val channelID = "Todo ID"
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ToDo Notes App")
                .setContentText(body)
                .setSound(ringtone)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // create notification channel for Oreo and above version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, "ToDo-Notes", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        // trigger the notification
        notificationManager.notify(0, notificationBuilder.build())
    }


    override fun onNewToken(p0: String?) {
        // this new token is configured when a new instance of app is installed on a phone
        // or reinstalled, or even installed in a new device then the token changes
        // this token is used primarily in chatting application as it is unique to you
        super.onNewToken(p0)
    }
}