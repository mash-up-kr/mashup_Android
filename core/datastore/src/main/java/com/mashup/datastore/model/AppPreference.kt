package com.mashup.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AppPreference(
    val isShowCoachMarkInScheduleList: Boolean
) {
    companion object {
        fun getDefaultInstance() = AppPreference(
            isShowCoachMarkInScheduleList = true
        )
    }
}
