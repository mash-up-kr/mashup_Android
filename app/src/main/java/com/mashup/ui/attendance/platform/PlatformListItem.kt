package com.mashup.ui.attendance.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.*
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.*
import com.mashup.ui.attendance.model.PlatformAttendance
import com.mashup.ui.model.Platform

@Composable
fun PlatformListItem(
    modifier: Modifier = Modifier,
    platformAttendance: PlatformAttendance
) {
    Card(modifier = Modifier.shadow(elevation = 2.dp)) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PlatformInfo(
                platformName = platformAttendance.platform.platform,
                modifier = Modifier
                    .padding(start = 18.dp)
                    .padding(vertical = 20.dp)
            )
            PlatformStatus(
                modifier = Modifier
                    .padding(end = 18.dp)
                    .padding(vertical = 20.dp),
                currentAttendanceCrew = platformAttendance.currentAttendanceCrew,
                maxAttendanceCrew = platformAttendance.maxAttendanceCrew
            )
        }
    }
}


@Composable
fun PlatformIcon() {
    Box(modifier = Modifier.size(width = 48.dp, height = 26.dp)) {
        Image(
            modifier = Modifier
                .size(26.dp)
                .padding(2.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(id = R.drawable.ic_android_1),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .align(CenterEnd)
                .size(26.dp)
                .border(width = 2.dp, color = White, shape = CircleShape)
                .padding(start = 2.dp, top = 2.dp, end = 2.dp, bottom = 2.dp),
            painter = painterResource(id = R.drawable.ic_android_2),
            contentDescription = null
        )
    }
}

@Composable
fun PlatformInfo(platformName: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        PlatformIcon()

        MashTextView(
            modifier = Modifier.padding(top = 2.dp, start = 2.dp),
            text = platformName,
            style = Header2,
            color = Gray800
        )
    }
}

@Composable
fun PlatformStatus(
    modifier: Modifier = Modifier,
    currentAttendanceCrew: Int = 0,
    maxAttendanceCrew: Int = 0,
    platformColor: Color = Green500
) {
    Column(modifier = modifier) {
        MashTextView(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(End),
            text = "출석인원",
            style = Caption3,
            color = Gray600
        )
        
        Row(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            MashTextView(
                text = "$currentAttendanceCrew",
                style = Title1,
                color = platformColor
            )
            MashTextView(
                modifier = Modifier.padding(start = 2.dp),
                text = "/$maxAttendanceCrew",
                style = Title1,
                color = Gray700
            )
            MashTextView(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .align(CenterVertically),
                text = "명",
                style = Body4,
                color = Gray500
            )
        }
    }
}

@Preview
@Composable
fun PlatformInfoPrev() {
    MashUpTheme {
        PlatformInfo("Android")
    }
}

@Preview
@Composable
fun PlatformStatuePrev() {
    MashUpTheme {
        PlatformStatus(currentAttendanceCrew = 13, maxAttendanceCrew = 20)
    }
}

@Preview
@Composable
fun PlatformListItemPrev() {
    MashUpTheme {
        PlatformListItem(
            modifier = Modifier.fillMaxWidth(),
            platformAttendance = PlatformAttendance(
                platform = Platform.Android,
                currentAttendanceCrew = 13,
                maxAttendanceCrew = 20
            )
        )
    }
}