package com.mashup.ui.attendance.platform

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.Gray200
import com.mashup.compose.colors.Gray500
import com.mashup.compose.colors.Gray600
import com.mashup.compose.colors.Green500
import com.mashup.compose.colors.Red500
import com.mashup.compose.colors.Yellow500
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.Caption1
import com.mashup.compose.typography.Caption3
import com.mashup.compose.typography.MashTextView
import com.mashup.ui.attendance.model.AttendanceStatus
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun AttendanceSeminarItem(
    modifier: Modifier = Modifier,
    index: Int,
    timeStamp: Date?,
    attendanceStatus: String,
    @DrawableRes iconRes: Int,
    iconSize: Int
) {
    val (attendanceColor, label) = when (attendanceStatus) {
        AttendanceStatus.ATTENDANCE.name -> {
            Green500 to "출석"
        }
        AttendanceStatus.ABSENT.name -> {
            Red500 to "결석"
        }
        AttendanceStatus.LATE.name -> {
            Yellow500 to "지각"
        }
        else -> {
            Gray200 to if (index == 2) "최종" else "${index + 1}부"
        }
    }

    val timeString = if (timeStamp != null) {
        try {
            SimpleDateFormat("hh:mm", Locale.KOREA).format(timeStamp)
        } catch (ignore: Exception) {
            if (index < 2) "-" else ""
        }
    } else {
        if (index < 2) "-" else ""
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(20.dp)) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = attendanceColor, shape = CircleShape)
            )
            Icon(
                modifier = Modifier
                    .size(iconSize.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.White
            )
        }
        MashTextView(
            modifier = Modifier.padding(top = 4.dp),
            text = label,
            style = Caption1,
            color = Gray600
        )
        MashTextView(
            modifier = Modifier.defaultMinSize(minWidth = 40.dp),
            text = timeString,
            textAlign = TextAlign.Center,
            style = Caption3,
            color = Gray500
        )
    }
}

@Preview
@Composable
fun AttendanceSeminarPrev() {
    MashUpTheme {
        AttendanceSeminarItem(
            timeStamp = Date(),
            index = 1,
            attendanceStatus = AttendanceStatus.ATTENDANCE.name,
            iconRes = R.drawable.ic_circle,
            iconSize = 8
        )
    }
}
