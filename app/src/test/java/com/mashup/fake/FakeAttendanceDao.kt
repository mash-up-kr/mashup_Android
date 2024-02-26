package com.mashup.fake

import com.mashup.core.network.Response
import com.mashup.data.dto.PlatformAttendanceResponse
import com.mashup.data.dto.TotalAttendanceResponse
import com.mashup.data.dto.AttendanceCheckResponse
import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.AttendanceCheckRequest
import com.mashup.network.dao.AttendanceDao

class FakeAttendanceDao : AttendanceDao {
    var isEnd = false
    var eventNum = 1

    override suspend fun postAttendanceCheck(attendanceCheckRequest: AttendanceCheckRequest): Response<AttendanceCheckResponse> {
        return Response(
            code = "",
            message = null,
            page = null,
            data = null
        )
    }

    override suspend fun getAttendancePlatforms(scheduleId: Int): Response<TotalAttendanceResponse> {
        return Response(
            code = "SUCCESS",
            message = null,
            page = null,
            data = TotalAttendanceResponse(
                isEnd = isEnd,
                eventNum = eventNum,
                platformInfos = emptyList()
            )
        )
    }

    override suspend fun getAttendancePlatformCrew(
        platformName: String,
        scheduleId: Int
    ): Response<PlatformAttendanceResponse> {
        return Response(
            code = "",
            message = null,
            page = null,
            data = null
        )
    }

    override suspend fun getAttendanceInSchedule(scheduleId: Int): Response<AttendanceInfoResponse> {
        return Response(
            code = "",
            message = null,
            page = null,
            data = null
        )
    }
}
