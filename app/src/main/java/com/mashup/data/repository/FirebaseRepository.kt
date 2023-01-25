package com.mashup.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {
    suspend fun getFcmToken(): String = suspendCancellableCoroutine {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                it.resume(task.result) {}
            }
            it.cancel()
        }
    }
}