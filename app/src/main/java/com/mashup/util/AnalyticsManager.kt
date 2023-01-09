package com.mashup.util

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsManager {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    private const val KEY_USER_TOKEN = "token"

    fun setUserId(userId: Int? = null) {
        firebaseAnalytics.setUserId(userId?.toString())
    }

    fun setUserToken(userToken: String? = null) {
        firebaseAnalytics.setUserProperty(KEY_USER_TOKEN, userToken)
    }

    fun addEvent(eventName: String, params: Bundle = bundleOf()) {
        firebaseAnalytics.logEvent(eventName, params)
    }
}
