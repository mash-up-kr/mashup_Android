package com.mashup.ui.attendance.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.data.dto.TotalAttendanceResponse
import com.mashup.data.model.PlatformInfo

@Composable
fun PlatformAttendanceScreen(
    modifier: Modifier = Modifier,
    platformAttendanceState: PlatformAttendanceState,
    onClickPlatform: (PlatformInfo) -> Unit,
    onClickBackButton: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = Gray50)
    ) {
        MashUpToolbar(
            title = "플랫폼별 출석현황",
            showBackButton = true,
            onClickBackButton = onClickBackButton
        )
        if (platformAttendanceState is PlatformAttendanceState.Success) {
            PlatformList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                notice = platformAttendanceState.notice,
                isAttendingEvent =
                platformAttendanceState.totalAttendance.eventNum != 2 ||
                    !platformAttendanceState.totalAttendance.isEnd,
                totalAttendanceResponse = platformAttendanceState.totalAttendance,
                onClickPlatform = onClickPlatform
            )
        }
    }
}

@Composable
fun PlatformList(
    modifier: Modifier = Modifier,
    notice: String,
    isAttendingEvent: Boolean,
    totalAttendanceResponse: TotalAttendanceResponse,
    onClickPlatform: (PlatformInfo) -> Unit
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Fixed(2)
    ) {
        totalAttendanceResponse.platformInfos.forEachIndexed { index, platform ->
            if (index == 0) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AttendanceNoticeItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        notice = notice
                    )
                }
                item(span = { GridItemSpan(1) }) {
                    PlatformListItem(
                        platformInfo = platform,
                        onClickPlatform = onClickPlatform,
                        isAttendingEvent = isAttendingEvent
                    )
                }
            } else {
                item(span = { GridItemSpan(1) }) {
                    PlatformListItem(
                        platformInfo = platform,
                        onClickPlatform = onClickPlatform,
                        isAttendingEvent = isAttendingEvent
                    )
                }
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
                        platform = Platform.ANDROID,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    ),
                    PlatformInfo(
                        platform = Platform.DESIGN,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    ),
                    PlatformInfo(
                        platform = Platform.WEB,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    ),
                    PlatformInfo(
                        platform = Platform.IOS,
                        totalCount = 13,
                        attendanceCount = 0,
                        lateCount = 7
                    )
                )
            ),
            isAttendingEvent = false
        ) {
        }
    }
}
