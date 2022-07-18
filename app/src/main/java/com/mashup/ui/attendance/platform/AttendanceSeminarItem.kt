package com.mashup.ui.attendance.platform

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.colors.*
import com.mashup.compose.typography.Caption1
import com.mashup.compose.typography.Caption3
import com.mashup.compose.typography.MashTextView
import com.mashup.ui.attendance.model.AttendanceStatus
import com.mashup.R

@Composable
fun AttendanceSeminarItem(
    modifier: Modifier = Modifier,
    timeStamp: String,
    attendanceStatus: AttendanceStatus,
    @DrawableRes iconRes: Int,
    iconSize: Int,
) {
    val attendanceColor = when (attendanceStatus) {
        AttendanceStatus.ATTEND -> {
            Green500
        }
        AttendanceStatus.ABSENCE -> {
            Red500
        }
        AttendanceStatus.LATENESS -> {
            Yellow500
        }
        AttendanceStatus.NONE -> {
            Gray200
        }
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
            text = attendanceStatus.label,
            style = Caption1,
            color = Gray600
        )
        MashTextView(
            modifier = Modifier.defaultMinSize(minWidth = 40.dp),
            text = timeStamp,
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
            timeStamp = "13:30",
            attendanceStatus = AttendanceStatus.ATTEND,
            iconRes = R.drawable.ic_circle,
            iconSize = 8
        )
    }
}