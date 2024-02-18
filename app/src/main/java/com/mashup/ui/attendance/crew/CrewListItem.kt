package com.mashup.ui.attendance.crew

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.model.AttendanceStatus
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.shape.CardListShape
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.MashTextView
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.data.model.AttendanceInfo
import com.mashup.data.model.MemberInfo
import com.mashup.ui.attendance.platform.AttendanceSeminarItem
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CrewListItem(
    modifier: Modifier = Modifier,
    memberInfo: MemberInfo,
    showMemberInfoDialog: (String, String) -> Unit
) {
    Card(
        modifier = modifier.border(width = 1.dp, color = Gray100, shape = CardListShape),
        shape = CardListShape,
        onClick = { showMemberInfoDialog(memberInfo.name, memberInfo.memberId.toString()) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            MashTextView(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .width(70.dp),
                text = memberInfo.name,
                style = SubTitle1,
                color = Gray800,
                textAlign = TextAlign.Left
            )
            Spacer(
                modifier = Modifier.height(9.dp)
            )
            SeminarItems(
                modifier = Modifier
                    .padding(horizontal = 16.dp).fillMaxWidth(),
                memberInfo = memberInfo
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}

@Composable
fun SeminarItems(
    modifier: Modifier = Modifier,
    memberInfo: MemberInfo
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val itemWidth = (maxWidth - 40.dp) / (memberInfo.attendanceInfos.size)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            memberInfo.attendanceInfos.forEachIndexed { index, attendanceInfo ->
                Row(
                    modifier = Modifier.width(itemWidth),
                    horizontalArrangement = Arrangement.Start
                ) {
                    AttendanceSeminarItem(
                        modifier = Modifier.width(40.dp),
                        timeStamp = attendanceInfo.attendanceAt,
                        attendanceStatus = attendanceInfo.status,
                        iconSize = 8,
                        index = index
                    )
                    Row(
                        modifier = Modifier.height(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp
                        )
                    }
                }
            }
            val status = memberInfo.getFinalAttendance()
            AttendanceSeminarItem(
                modifier = Modifier.width(40.dp),
                timeStamp = null,
                attendanceStatus = status,
                iconSize = 8,
                index = 0,
                isFinal = true
            )
        }
    }
}

@Preview
@Composable
fun SeminarItemsPrev() {
    MashUpTheme {
        SeminarItems(
            modifier = Modifier.fillMaxWidth(),
            memberInfo = MemberInfo(
                name = "가길동",
                memberId = 0,
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
                memberId = 0,
                attendanceInfos = listOf(
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    ),
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    ),
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    ),
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    ),
                    AttendanceInfo(
                        status = AttendanceStatus.ATTENDANCE,
                        attendanceAt = Date()
                    )
                )
            ),
            showMemberInfoDialog = { _, _ -> }
        )
    }
}
