package com.mashup.ui.model

import android.annotation.SuppressLint
import com.mashup.ui.mypage.AttendanceType
import com.mashup.ui.mypage.MyPageAdapterType
import java.text.SimpleDateFormat
import java.util.*

data class AttendanceModel(
    val id: Int,
    val myPageType: MyPageAdapterType,
    val profile: Profile?,
    val generationNum: Int?,
    val activityHistory: ActivityHistory?
) {
    fun getGeneration() = "${generationNum}기"
}


data class ActivityHistory(
    val scoreName: String,
    val attendanceType: AttendanceType,
    val cumulativeScore: Double,
    val totalScore: Double,
    val detail: String?,
    val date: Date,
) {
    @SuppressLint("SimpleDateFormat")
    fun getAttendanceDetail(): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat = try {
            format.format(date)
        } catch (ignore: Exception) {
            "????-??-??"
        }
        return "$dateFormat | $detail"
    }

    fun getTotalScoreText(): String {
        val score: Number =
            if (totalScore % 1 == 0.0) totalScore.toInt() else totalScore
        return "${score}점"
    }
}

data class Profile(
    val platform: Platform,
    val name: String,
    val score: Double,
) {
    fun getAttendanceScore() = "${score}점"
}