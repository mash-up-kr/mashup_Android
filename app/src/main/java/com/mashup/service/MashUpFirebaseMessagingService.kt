package com.mashup.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashup.BuildConfig
import com.mashup.R
import com.mashup.service.PushLinkType.Companion.getPushLinkType
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.qrscan.QRScanActivity
import com.mashup.ui.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import java.net.URL

@AndroidEntryPoint
class MashUpFirebaseMessagingService : FirebaseMessagingService() {

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
        val imageUrl = message.notification?.imageUrl

        if (title != null && body != null) {
            createNotificationChannel()
            notifyPushMessage(
                title = title,
                body = body,
                imageUrl = imageUrl,
                data = message.data
            )
        }
    }

    private fun notifyPushMessage(
        title: String,
        body: String,
        imageUrl: Uri?,
        data: Map<String, String>
    ) {
        val splashIntent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val linkType = data[PUSH_DEEP_LINK_KEY] ?: ""
        val taskStackBuilder = when (getPushLinkType(linkType)) {
            PushLinkType.DANGGN -> {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(splashIntent)
                    .addNextIntent(ShakeDanggnActivity.newIntent(this))
            }
            PushLinkType.QR -> {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(splashIntent)
                    .addNextIntent(QRScanActivity.newIntent(this))
            }
            else -> {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(splashIntent)
            }
        }
        val pendingIntent = taskStackBuilder.getPendingIntent(
            0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuild = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // 이미지가 전달된 경우
        getBitmapFromUrl(imageUrl)?.let { bitmapImage ->
            notificationBuild
                .setLargeIcon(bitmapImage)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmapImage)
                        .bigLargeIcon(null)
                )
        }

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

    private fun getBitmapFromUrl(imageUrl: Uri?): Bitmap? {
        return try {
            val url = URL(imageUrl?.toString())
            BitmapFactory.decodeStream(url.openConnection().inputStream)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val TAG = "MashUpFirebaseMessagingService"

        private const val CHANNEL_ID = "MashUpNotificationChannel"
        private const val CHANNEL_NAME = "Mash-Up Notification"
        private var NOTIFICATION_ID = 1001

        private const val PUSH_DEEP_LINK_KEY = "link"
    }
}
