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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.data.model.MemberInfo

@Composable
fun CrewScreen(
    modifier: Modifier = Modifier,
    crewAttendanceState: CrewAttendanceState,
    onClickBackButton: () -> Unit,
    showMemberInfoDialog: (String, String) -> Unit,
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
                crewAttendanceList = crewAttendanceState.crewAttendance.members,
                showMemberInfoDialog = showMemberInfoDialog,
            )
        }
    }
}

@Composable
fun CrewList(
    modifier: Modifier = Modifier,
    crewAttendanceList: List<MemberInfo>,
    showMemberInfoDialog: (String, String) -> Unit,
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
                memberInfo = memberInfo,
                showMemberInfoDialog = showMemberInfoDialog
            )
        }
    }
}
