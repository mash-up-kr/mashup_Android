package com.mashup.ui.attendance.platform

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.*
import com.mashup.compose.shape.CardListShape
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.*
import com.mashup.ui.attendance.model.PlatformAttendance
import com.mashup.ui.model.Platform

@Composable
fun PlatformListItem(
    modifier: Modifier = Modifier,
    isAttendingEvent: Boolean = true,
    platformAttendance: PlatformAttendance,
    onClickPlatform: (PlatformAttendance) -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onClickPlatform(platformAttendance)
        },
        elevation = 2.dp,
        shape = CardListShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                PlatformInfo(
                    platform = platformAttendance.platform,
                    modifier = Modifier
                        .padding(start = 18.dp)
                )

                if (isAttendingEvent) {
                    PlatformStatus(
                        modifier = Modifier
                            .padding(end = 18.dp),
                        numberOfAttend = platformAttendance.numberOfAttend,
                        numberOfMaxAttend = platformAttendance.numberOfAttend
                            + platformAttendance.numberOfLateness
                            + platformAttendance.numberOfAbsence
                    )
                }
            }
            if (!isAttendingEvent) {
                PlatformAttendanceStatus(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .padding(horizontal = 20.dp),
                    numberOfAttend = platformAttendance.numberOfAttend,
                    numberOfLateness = platformAttendance.numberOfLateness,
                    numberOfAbsence = platformAttendance.numberOfAbsence
                )
            }
        }
    }
}


@Composable
fun PlatformIcon(@DrawableRes platformImageRes: Int) {
    Image(
        painter = painterResource(id = platformImageRes),
        contentDescription = null
    )
}

@Composable
fun PlatformInfo(platform: Platform, modifier: Modifier = Modifier) {
    val platformImage = remember(platform) {
        when (platform) {
            Platform.DESIGN -> {
                R.drawable.img_statusprofile_design
            }
            Platform.ANDROID -> {
                R.drawable.img_statusprofile_android
            }
            Platform.WEB -> {
                R.drawable.img_statusprofile_web
            }
            Platform.IOS -> {
                R.drawable.img_statusprofile_ios
            }
            Platform.SPRING -> {
                R.drawable.img_statusprofile_spring
            }
            Platform.NODE -> {
                R.drawable.img_statusprofile_node
            }
            else -> {
                R.drawable.ic_img_placeholder_sleeping
            }
        }
    }

    Column(modifier = modifier) {
        PlatformIcon(platformImage)

        MashTextView(
            modifier = Modifier.padding(start = 2.dp),
            text = platform.detailName,
            style = Header2,
            color = Gray800
        )
    }
}

@Composable
fun PlatformStatus(
    modifier: Modifier = Modifier,
    numberOfAttend: Int = 0,
    numberOfMaxAttend: Int = 0,
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
                text = "$numberOfAttend",
                style = Title1,
                color = if (numberOfAttend != 0) platformColor else Gray300
            )
            MashTextView(
                modifier = Modifier.padding(start = 2.dp),
                text = "/$numberOfMaxAttend",
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

@Composable
fun PlatformAttendanceStatus(
    modifier: Modifier = Modifier,
    numberOfAttend: Int,
    numberOfLateness: Int,
    numberOfAbsence: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PlatformAttendanceStatusItem(
            modifier = Modifier.weight(1f),
            backgroundColor = Green100,
            iconRes = R.drawable.ic_check,
            iconColor = Green500,
            label = "출석",
            labelColor = Green500,
            value = numberOfAttend,
            valueColor = Green600
        )
        PlatformAttendanceStatusItem(
            modifier = Modifier.weight(1f),
            backgroundColor = Yellow100,
            iconRes = R.drawable.ic_triangle,
            iconColor = Yellow500,
            label = "지각",
            labelColor = Yellow500,
            value = numberOfLateness,
            valueColor = Yellow600
        )
        PlatformAttendanceStatusItem(
            modifier = Modifier.weight(1f),
            backgroundColor = Red100,
            iconRes = R.drawable.ic_xmark,
            iconColor = Red500,
            label = "불참",
            labelColor = Red500,
            value = numberOfAbsence,
            valueColor = Red600
        )
    }
}

@Composable
fun PlatformAttendanceStatusItem(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    @DrawableRes iconRes: Int,
    iconColor: Color,
    label: String,
    labelColor: Color,
    value: Int,
    valueColor: Color
) {
    Row(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp)
            .padding(start = 8.dp, end = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = iconColor
        )
        MashTextView(
            modifier = Modifier.padding(end = 2.dp),
            text = label,
            color = labelColor,
            style = Body1
        )
        MashTextView(
            text = String.format("%02d", value),
            color = valueColor,
            style = SubTitle2
        )
    }
}

@Preview
@Composable
fun PlatformInfoPrev() {
    MashUpTheme {
        PlatformInfo(Platform.ANDROID)
    }
}

@Preview
@Composable
fun PlatformStatuePrev() {
    MashUpTheme {
        PlatformStatus(numberOfAttend = 13, numberOfMaxAttend = 20)
    }
}

@Preview
@Composable
fun PlatformListItemPrev() {
    MashUpTheme {
        PlatformListItem(
            modifier = Modifier.fillMaxWidth(),
            platformAttendance = PlatformAttendance(
                platform = Platform.ANDROID,
                numberOfAttend = 13,
                numberOfLateness = 0,
                numberOfAbsence = 7
            ),
            onClickPlatform = {}
        )
    }
}

@Preview(name = "출석이 끝난 리스트")
@Composable
fun EndedPlatformListItemPrev() {
    MashUpTheme {
        PlatformListItem(
            modifier = Modifier.fillMaxWidth(),
            isAttendingEvent = false,
            platformAttendance = PlatformAttendance(
                platform = Platform.ANDROID,
                numberOfAttend = 13,
                numberOfLateness = 0,
                numberOfAbsence = 7
            ),
            onClickPlatform = {}
        )
    }
}

@Preview
@Composable
fun PlatformAttendanceStatusItemPrev() {
    MashUpTheme {
        PlatformAttendanceStatusItem(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Green100,
            iconRes = R.drawable.ic_check,
            iconColor = Green500,
            label = "출석",
            labelColor = Green500,
            value = 10,
            valueColor = Green600
        )
    }
}

@Preview
@Composable
fun PlatformAttendanceStatusPrev() {
    MashUpTheme {
        PlatformAttendanceStatus(
            modifier = Modifier.fillMaxWidth(),
            numberOfAttend = 10,
            numberOfLateness = 1,
            numberOfAbsence = 2
        )
    }
}