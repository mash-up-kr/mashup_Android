package com.mashup.core.model.data.local

import kotlinx.serialization.Serializable

@Serializable
data class DanggnPreference(
    val personalFirstRanking: Int,
    val platformFirstRanking: Int
) {
    companion object {
        fun getDefaultInstance() = DanggnPreference(
            personalFirstRanking = -1,
            platformFirstRanking = -1
        )
    }
}
