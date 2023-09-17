package com.mashup.ui.model

import android.annotation.SuppressLint
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.mypage.AttendanceType
import com.mashup.ui.mypage.MyPageAdapterType
import java.text.SimpleDateFormat
import java.util.Date

sealed class AttendanceModel(
    open val id: Int,
    val myPageType: MyPageAdapterType
) {
    data class Title(
        override val id: Int,
        val name: String
    ) : AttendanceModel(id, MyPageAdapterType.TITLE)

    data class MyProfile(
        override val id: Int,
        val data: ProfileData
    ) : AttendanceModel(id, MyPageAdapterType.MY_PROFILE)

    data class Score(
        override val id: Int,
        val score: Double
    ) : AttendanceModel(id, MyPageAdapterType.SCORE) {
        fun getAttendanceScore() = "${if (score % 1 == 0.0) score.toInt() else score}점"
    }

    data class ProfileCard(
        override val id: Int,
        val cardList: List<ProfileCardData>
    ) : AttendanceModel(id, MyPageAdapterType.PROFILE_CARD)

    data class HistoryLevel(
        override val id: Int,
        val generationNum: Int
    ) : AttendanceModel(id, MyPageAdapterType.LIST_LEVEL) {
        fun getGeneration() = "${generationNum}기"
    }

    data class HistoryItem(
        override val id: Int,
        val activityHistory: ActivityHistory
    ) : AttendanceModel(id, MyPageAdapterType.LIST_ITEM)

    data class None(
        override val id: Int
    ) : AttendanceModel(id, MyPageAdapterType.LIST_NONE)
}

data class ActivityCardData(
    val generationNum: Int,
    val isRunning: Boolean,
    val name: String,
    val platform: Platform
    val platform: Platform,
    val projectTeamName: String,
    val role: String,
)

data class ActivityHistory(
    val scoreName: String,
    val attendanceType: AttendanceType,
    val cumulativeScore: Double,
    val score: Double,
    val detail: String?,
    val date: Date
) {
    @SuppressLint("SimpleDateFormat")
    fun getAttendanceDetail(): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat = try {
            format.format(date)
        } catch (ignore: Exception) {
            "????-??-??"
        }
        val detailFormat = if (detail != null) "| $detail" else ""

        return "$dateFormat $detailFormat"
    }

    fun getTotalScoreText(): String {
        val score: Number =
            if (cumulativeScore % 1 == 0.0) cumulativeScore.toInt() else cumulativeScore
        return "${score}점"
    }
}
