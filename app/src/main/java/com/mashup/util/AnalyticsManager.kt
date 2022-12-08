package com.mashup.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mashup.data.datastore.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor(
    userDataSource: UserDataSource
) {
    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    companion object {
        private const val KEY_USER_TOKEN = "token"
    }

    init {
        firebaseAnalytics.setUserId(userDataSource.memberId?.toString())
        firebaseAnalytics.setUserProperty(KEY_USER_TOKEN, userDataSource.token)
    }

    fun addEvent(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }
}
