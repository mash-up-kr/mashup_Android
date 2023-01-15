package com.mashup.util

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

object AnalyticsManager {
    private var firebaseAnalytics: FirebaseAnalytics? = null

    private const val KEY_USER_TOKEN = "token"

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun setUserId(userId: Int? = null) {
        firebaseAnalytics?.setUserId(userId?.toString())
    }

    fun setUserToken(userToken: String? = null) {
        firebaseAnalytics?.setUserProperty(KEY_USER_TOKEN, userToken)
    }

    fun addEvent(eventName: String, params: Bundle = bundleOf()) {
        firebaseAnalytics?.logEvent(eventName, params)
    }
}
