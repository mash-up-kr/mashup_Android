package com.mashup.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticsManager {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    private const val KEY_USER_TOKEN = "token"

    fun setUserInfo(userId: String?, userToken: String?) {
        firebaseAnalytics.setUserId(userId)
        firebaseAnalytics.setUserProperty(KEY_USER_TOKEN, userToken)
    }

    fun addEvent(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }
}
