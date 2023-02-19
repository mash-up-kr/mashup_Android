package com.mashup.fake

import com.mashup.data.dto.*
import com.mashup.network.Response
import com.mashup.network.dao.AttendanceDao

class FakeAttendanceDao : AttendanceDao {
    var isEnd = false
    var eventNum = 1

    override suspend fun postAttendanceCheck(attendanceCheckRequest: AttendanceCheckRequest): Response<AttendanceCheckResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendancePlatforms(scheduleId: Int): Response<TotalAttendanceResponse> {
        return Response(
            code = "SUCCESS",
            message = null,
            page = null,
            data = TotalAttendanceResponse(
                isEnd = isEnd,
                eventNum = eventNum,
                platformInfos = emptyList(),
            ),
        )
    }

    override suspend fun getAttendancePlatformCrew(
        platformName: String, scheduleId: Int
    ): Response<PlatformAttendanceResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendanceInSchedule(scheduleId: Int): Response<AttendanceInfoResponse> {
        TODO("Not yet implemented")
    }
}
