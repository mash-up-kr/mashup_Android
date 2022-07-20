package com.mashup.ui.model

import com.mashup.ui.mypage.AttendanceType


data class AttendanceModel(
    val id: Int,
    val type: Int,
    val profile: Profile?,
    val generationNum: Int?,
    val activityHistory: ActivityHistory?
) {
    companion object {
        val EMPTY = AttendanceModel(
            id = 0,
            type = 0,
            profile = Profile.EMPTY,
            generationNum = 12,
            activityHistory = ActivityHistory.EMPTY
        )
    }

    fun getGeneration() = "${generationNum}기"

}


data class ActivityHistory(
    val attendanceType: AttendanceType,
    val totalScore: Int?,
    val detail: String?,
    val date: String?,
) {
    companion object {
        val EMPTY = ActivityHistory(
            attendanceType = AttendanceType.PLACEHOLDER_HISTORY,
            totalScore = 103,
            detail = "",
            date = "",
        )
    }

    fun getAttendanceDetail() = "$date | $detail"
    fun getTotalScoreText() = "${totalScore}점"
}

data class Profile(
    val platform: Platform,
    val name: String,
    val score: Int,
) {
    companion object {
        val EMPTY = Profile(
            platform = Platform.NODE,
            name = "test",
            score = 2
        )
    }

    fun getAttendanceScore() = "${score}점"
}