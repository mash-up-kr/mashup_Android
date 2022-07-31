package com.mashup.ui.attendance.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.compose.theme.MashUpTheme
import com.mashup.data.dto.TotalAttendanceResponse
import com.mashup.data.model.Platform
import com.mashup.data.model.PlatformInfo

@Composable
fun PlatformList(
    modifier: Modifier = Modifier,
    notice: String,
    totalAttendanceResponse: TotalAttendanceResponse,
    onClickPlatform: (PlatformInfo) -> Unit
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        if (notice.isNotBlank()) {
            AttendanceNoticeItem(
                modifier = Modifier
                    .fillMaxWidth(),
                notice = notice
            )
        }
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            cells = GridCells.Fixed(2)
        ) {
            items(
                items = totalAttendanceResponse.platformInfos,
            ) { platform ->
                PlatformListItem(
                    modifier = Modifier.fillMaxWidth(),
                    platformInfo = platform,
                    onClickPlatform = onClickPlatform
                )
            }
        }
    }
}

@Preview(widthDp = 360)
@Composable
fun PlatformListPrev() {
    MashUpTheme {
        PlatformList(
            modifier = Modifier.fillMaxWidth(),
            notice = "출석 진행 중",
            totalAttendanceResponse = TotalAttendanceResponse(
                isEnd = false,
                eventNum = 1,
                platformInfos = listOf(
                    PlatformInfo(
                        platform = Platform.ANDROID.name,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    ),
                    PlatformInfo(
                        platform = Platform.DESIGN.name,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    ),
                    PlatformInfo(
                        platform = Platform.WEB.name,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    ),
                    PlatformInfo(
                        platform = Platform.IOS.name,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    )
                )
            )
        ) {
        }
    }
}