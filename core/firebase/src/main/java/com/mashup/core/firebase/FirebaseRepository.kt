package com.mashup.core.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {
    suspend fun getFcmToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result) {}
            } else {
                continuation.cancel()
            }
        }
    }

    suspend fun getRecentAppVersion(): Long = suspendCancellableCoroutine { continuation ->
        Firebase.firestore.document("app/config").get()
            .addOnSuccessListener { document ->
                val version = document?.getLong("version")
                if (version != null) {
                    continuation.resume(version) {}
                } else {
                    continuation.cancel()
                }
            }
            .addOnFailureListener { _ ->
                continuation.cancel()
            }
    }
}
