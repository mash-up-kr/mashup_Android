package com.mashup.ui.attendance.crew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.ui.attendance.model.CrewAttendance

@Composable
fun CrewList(
    modifier: Modifier = Modifier,
    crewAttendanceList: List<CrewAttendance> = emptyList()
) {
    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            crewAttendanceList,
            key = { item -> item.name }) { crewAttendance ->
            CrewListItem(
                modifier = Modifier.fillMaxWidth(),
                crewAttendance = crewAttendance
            )
        }
    }
}