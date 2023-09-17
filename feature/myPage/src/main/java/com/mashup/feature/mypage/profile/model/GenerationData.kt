package com.mashup.feature.mypage.profile.model

data class GenerationData(
    val id: Int = 0,
    val name: String,
    val isRunning: Boolean,
    val generationNumber: Int = 0,
    val platform: String = "",
    val projectTeamName: String = "",
    val role: String = "",
)
