package com.mashup.ui.attendance.crew

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.mashup.compose.colors.Gray100
import com.mashup.compose.colors.Gray200
import com.mashup.compose.colors.Gray800
import com.mashup.compose.shape.CardListShape
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.MashTextView
import com.mashup.compose.typography.SubTitle1
import com.mashup.core.model.AttendanceStatus
import com.mashup.data.model.AttendanceInfo
import com.mashup.data.model.MemberInfo
import com.mashup.ui.attendance.platform.AttendanceSeminarItem
import java.util.*

@Composable
fun CrewListItem(
    modifier: Modifier = Modifier,
    memberInfo: MemberInfo
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = Gray100, shape = CardListShape),
        shape = CardListShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MashTextView(
                modifier = Modifier
                    .padding(start = 24.dp, end = 22.dp)
                    .width(70.dp),
                text = memberInfo.name,
                style = SubTitle1,
                color = Gray800,
                textAlign = TextAlign.Center
            )
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 22.dp)
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
                    .padding(start = 22.dp, end = 20.dp),
                memberInfo = memberInfo
            )
        }
    }
}

@Composable
fun SeminarItems(
    modifier: Modifier = Modifier,
    memberInfo: MemberInfo
) {
    val finalAttendance = remember(
        key1 = memberInfo.attendanceInfos.getOrNull(0)?.status,
        key2 = memberInfo.attendanceInfos.getOrNull(1)?.status
    ) {
        memberInfo.getFinalAttendance()
    }

    Row(modifier = modifier) {
        AttendanceSeminarItem(
            modifier = Modifier.padding(vertical = 14.dp),
            timeStamp = memberInfo.attendanceInfos.getOrNull(0)?.attendanceAt,
            attendanceStatus = memberInfo.attendanceInfos.getOrNull(0)?.status
                ?: AttendanceStatus.NOT_YET,
            iconSize = 8,
            index = 0
        )
        SeminarItemSpacer()
        AttendanceSeminarItem(
            modifier = Modifier.padding(vertical = 14.dp),
            timeStamp = memberInfo.attendanceInfos.getOrNull(1)?.attendanceAt,
            attendanceStatus = memberInfo.attendanceInfos.getOrNull(1)?.status
                ?: AttendanceStatus.NOT_YET,
            iconSize = 8,
            index = 1
        )
        SeminarItemSpacer()
        AttendanceSeminarItem(
            modifier = Modifier.padding(vertical = 14.dp),
            timeStamp = null,
            attendanceStatus = finalAttendance,
            iconSize = 16,
            index = 2
        )
    }
}

@Composable
fun RowScope.SeminarItemSpacer() {
    Surface(
        modifier = Modifier
            .weight(1f)
            .padding(top = 24.dp)
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
            memberInfo = MemberInfo(
                name = "가길동",
                attendanceInfos = listOf(
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    ),
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    )
                )
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
            memberInfo = MemberInfo(
                name = "가길동",
                attendanceInfos = listOf(
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    ),
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    )
                )
            )
        )
    }
}
