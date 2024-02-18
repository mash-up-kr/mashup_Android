package com.mashup.ui.attendance.crew

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.model.AttendanceStatus
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.data.model.AttendanceInfo
import com.mashup.data.model.MemberInfo

@Composable
fun CrewScreen(
    modifier: Modifier = Modifier,
    crewAttendanceState: CrewAttendanceState,
    onClickBackButton: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = Color.White)
    ) {
        MashUpToolbar(
            title = (crewAttendanceState as? CrewAttendanceState.Success)?.title.orEmpty(),
            showBackButton = true,
            onClickBackButton = onClickBackButton
        )
        if (crewAttendanceState is CrewAttendanceState.Success) {
            CrewList(
                modifier = Modifier.fillMaxWidth(),
                crewAttendanceList = crewAttendanceState.crewAttendance.members
            )
        }
    }
}

@Composable
fun CrewList(
    modifier: Modifier = Modifier,
    crewAttendanceList: List<MemberInfo>
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(crewAttendanceList) { memberInfo ->
            CrewListItem(
                modifier = Modifier.fillMaxWidth(),
                memberInfo = memberInfo
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCrewList() {
    val memberInfo = MemberInfo(
        name = "test",
        attendanceInfos = listOf(
            AttendanceInfo(
                null,
                status = AttendanceStatus.ATTENDANCE
            ),
            AttendanceInfo(
                null,
                status = AttendanceStatus.ATTENDANCE
            )
        )
    )
    MashUpTheme {
        CrewList(
            modifier = Modifier.fillMaxWidth(),
            crewAttendanceList = listOf(
                memberInfo,
                memberInfo,
                memberInfo,
                memberInfo
            )
        )
    }
}
