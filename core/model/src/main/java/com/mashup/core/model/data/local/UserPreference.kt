package com.mashup.core.model.data.local

import com.mashup.core.model.Platform
import kotlinx.serialization.Serializable

@Serializable
data class UserPreference(
    val token: String,
    val name: String,
    val platform: Platform,
    val generationNumbers: List<Int>,
    val pushNotificationAgreed: Boolean,
    val danggnPushNotificationAgreed: Boolean = true
) {
    companion object {
        fun getDefaultInstance() = UserPreference(
            token = "",
            name = "",
            platform = Platform.UNKNOWN,
            generationNumbers = listOf(0),
            pushNotificationAgreed = true,
            danggnPushNotificationAgreed = true
        )
    }
}
