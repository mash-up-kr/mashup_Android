package com.mashup.util

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.mashup.core.common.R
import com.mashup.ui.mypage.AttendanceType

object DesignUtil {
    @JvmStatic
    @BindingAdapter("attendanceScore")
    fun AppCompatTextView.setAttendanceScore(attendanceScore: Double) {
        text = if (attendanceScore == 0.0) {
            setTextColor(ContextCompat.getColor(context, R.color.gray500))
            "0점"
        } else {
            val score: Number =
                if (attendanceScore % 1 == 0.0) attendanceScore.toInt() else attendanceScore
            if (attendanceScore > 0) {
                setTextColor(ContextCompat.getColor(context, R.color.blue500))
                "+${score}점"
            } else {
                setTextColor(ContextCompat.getColor(context, R.color.red500))
                "${score}점"
            }
        }
    }

    @JvmStatic
    @BindingAdapter("attendanceIcon")
    fun ImageView.setAttendanceIcon(attendanceType: AttendanceType) {
        setImageDrawable(ContextCompat.getDrawable(context, attendanceType.resourceId))
    }
}
