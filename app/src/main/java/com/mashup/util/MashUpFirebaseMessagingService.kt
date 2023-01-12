package com.mashup.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashup.BuildConfig
import com.mashup.R

class MashUpFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val body = message.notification?.body

        if (title != null && body != null) {
            createNotificationChannel()
            notifyPushMessage(title, body)
        }
    }

    private fun notifyPushMessage(title: String, body: String) {
        val notificationBuild = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // TODO: 추후 push 메세지용 아이콘으로 교체
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(this)
            .notify(NOTIFICATION_ID++, notificationBuild.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android O 이상부터 NotificationChannel 생성 필요
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "MashUpFirebaseMessagingService"

        private const val CHANNEL_ID = "MashUpNotificationChannel"
        private const val CHANNEL_NAME = "Mash-Up Notification"
        private var NOTIFICATION_ID = 1001
    }
}
