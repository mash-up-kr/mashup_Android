package com.mashup.core.model.data.local

import kotlinx.serialization.Serializable

@Serializable
data class AppPreference(
    val showCoachMarkInScheduleList: Boolean
)