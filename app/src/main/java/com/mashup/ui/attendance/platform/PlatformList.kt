package com.mashup.ui.attendance.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.ui.attendance.model.PlatformAttendance

@Composable
fun PlatformList(
    modifier: Modifier = Modifier,
    notice: String,
    platformAttendanceList: List<PlatformAttendance> = emptyList()
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            platformAttendanceList,
            key = { _, item -> item.platform }) { index, platform ->
            if (index == 0 && notice.isNotBlank()) {
                AttendanceNoticeItem(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    notice = notice
                )
            }
            PlatformListItem(
                modifier = Modifier.fillMaxWidth(),
                platformAttendance = platform
            )
        }
    }
}