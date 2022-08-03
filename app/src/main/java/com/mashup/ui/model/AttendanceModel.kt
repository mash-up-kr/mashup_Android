package com.mashup.ui.model

import com.mashup.ui.mypage.AttendanceType
import com.mashup.ui.mypage.MyPageAdapterType

data class AttendanceModel(
    val id: Int,
    val myPageType: MyPageAdapterType,
    val profile: Profile?,
    val generationNum: Int?,
    val activityHistory: ActivityHistory?
) {
    companion object {
        val EMPTY = AttendanceModel(
            id = 0,
            myPageType = MyPageAdapterType.TITLE,
            profile = Profile.EMPTY,
            generationNum = 12,
            activityHistory = ActivityHistory.EMPTY
        )
    }

    fun getGeneration() = "${generationNum}기"

}


data class ActivityHistory(
    val attendanceType: AttendanceType,
    val totalScore: Double?,
    val detail: String?,
    val date: String?,
) {
    companion object {
        val EMPTY = ActivityHistory(
            attendanceType = AttendanceType.ETC,
            totalScore = 103.0,
            detail = "3차 전체 세미나",
            date = "2022.02.03",
        )
    }

    fun getAttendanceDetail() = "$date | $detail"
    fun getTotalScoreText() = "${totalScore}점"
}

data class Profile(
    val platform: Platform,
    val name: String,
    var score: Double,
) {
    companion object {
        val EMPTY = Profile(
            platform = Platform.NODE,
            name = "test",
            score = 2.0
        )
    }

    fun getAttendanceScore() = "${score}점"
}