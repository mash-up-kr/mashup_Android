package com.mashup.ui.attendance.crew

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.Gray200
import com.mashup.compose.colors.Gray800
import com.mashup.compose.shape.CardListShape
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.MashTextView
import com.mashup.compose.typography.SubTitle1
import com.mashup.ui.attendance.model.AttendanceStatus
import com.mashup.ui.attendance.model.CrewAttendance
import com.mashup.ui.attendance.platform.AttendanceSeminarItem

@Composable
fun CrewListItem(
    modifier: Modifier = Modifier,
    crewAttendance: CrewAttendance
) {
    Card(
        modifier = modifier,
        elevation = 2.dp,
        shape = CardListShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MashTextView(
                modifier = Modifier
                    .padding(start = 24.dp),
                text = crewAttendance.name,
                style = SubTitle1,
                color = Gray800,
                textAlign = TextAlign.Center
            )
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 14.dp, horizontal = 22.dp)
            ) {
                Divider(
                    color = Gray200,
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )
            }
            SeminarItems(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 20.dp),
                crewAttendance = crewAttendance
            )
        }
    }
}

@Composable
fun SeminarItems(
    modifier: Modifier = Modifier,
    crewAttendance: CrewAttendance
) {
    val finalAttendance = remember(
        key1 = crewAttendance.firstSeminarAttendance,
        key2 = crewAttendance.secondSeminarAttendance
    ) {
        crewAttendance.finalAttendance
    }

    Row(modifier = modifier) {
        AttendanceSeminarItem(
            modifier = Modifier.padding(vertical = 6.dp),
            timeStamp = crewAttendance.firstSeminarAttendanceTimeStamp,
            attendanceStatus = crewAttendance.firstSeminarAttendance,
            iconRes = R.drawable.ic_circle,
            iconSize = 8
        )
        SeminarItemSpacer()
        AttendanceSeminarItem(
            modifier = Modifier.padding(vertical = 6.dp),
            timeStamp = crewAttendance.secondSeminarAttendanceTimeStamp,
            attendanceStatus = crewAttendance.secondSeminarAttendance,
            iconRes = R.drawable.ic_circle,
            iconSize = 8
        )
        SeminarItemSpacer()
        AttendanceSeminarItem(
            modifier = Modifier.padding(vertical = 6.dp),
            timeStamp = crewAttendance.secondSeminarAttendanceTimeStamp,
            attendanceStatus = finalAttendance,
            iconRes = finalAttendance.iconRes,
            iconSize = 16
        )
    }
}

@Composable
fun RowScope.SeminarItemSpacer() {
    Surface(
        modifier = Modifier
            .weight(1f)
            .padding(top = 15.dp)
    ) {
        Divider(
            color = Gray200,
            modifier = Modifier
                .height(1.dp)
        )
    }
}

@Preview
@Composable
fun SeminarItemsPrev() {
    MashUpTheme {
        SeminarItems(
            crewAttendance = CrewAttendance(
                name = "가길동",
                firstSeminarAttendance = AttendanceStatus.ATTEND,
                firstSeminarAttendanceTimeStamp = "13:30",
                secondSeminarAttendance = AttendanceStatus.ATTEND,
                secondSeminarAttendanceTimeStamp = "14:00"
            )
        )
    }
}

@Preview
@Composable
fun CrewListItemPrev() {
    MashUpTheme {
        CrewListItem(
            modifier = Modifier.fillMaxWidth(),
            crewAttendance =
            CrewAttendance(
                name = "가길동",
                firstSeminarAttendance = AttendanceStatus.ATTEND,
                firstSeminarAttendanceTimeStamp = "13:30",
                secondSeminarAttendance = AttendanceStatus.ATTEND,
                secondSeminarAttendanceTimeStamp = "14:00"
            )
        )
    }
}