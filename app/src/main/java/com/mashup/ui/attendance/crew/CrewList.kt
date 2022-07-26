package com.mashup.ui.attendance.crew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.data.model.MemberInfo

@Composable
fun CrewList(
    modifier: Modifier = Modifier,
    crewAttendanceList: List<MemberInfo> = emptyList()
) {
    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            crewAttendanceList,
            key = { item -> item.name }) { memberInfo ->
            CrewListItem(
                modifier = Modifier.fillMaxWidth(),
                memberInfo = memberInfo
            )
        }
    }
}