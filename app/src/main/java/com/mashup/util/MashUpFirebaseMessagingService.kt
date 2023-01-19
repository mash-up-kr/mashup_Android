package com.mashup.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mashup.BuildConfig
import com.mashup.data.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MashUpFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (BuildConfig.DEBUG) {
            Log.d(TAG, token)
        }
        userRepository.setUserFcmToken(token)
    }

    override fun onMessageReceived(messae: RemoteMessage) {
        super.onMessageReceived(messae)

        // TODO: 수신한 메시지 처리
    }

    companion object {
        private const val TAG = "MashUpFirebaseMessagingService"
    }
}
