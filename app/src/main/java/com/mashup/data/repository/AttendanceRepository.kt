package com.mashup.data.repository

import com.mashup.common.Response
import com.mashup.data.dto.AttendanceCheckRequest
import com.mashup.data.dto.AttendanceCheckResponse
import com.mashup.data.dto.PlatformAttendanceResponse
import com.mashup.data.dto.TotalAttendanceResponse
import com.mashup.network.dao.AttendanceDao
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao
) {
    suspend fun attendanceCheck(eventId: Int, memberId: Int): Response<AttendanceCheckResponse> {
        return attendanceDao.postAttendanceCheck(
            AttendanceCheckRequest(
                eventId = eventId,
                memberId = memberId
            )
        )
    }

    suspend fun getCrewAttendanceList(
        platformName: String,
        scheduleId: Int
    ): Response<PlatformAttendanceResponse> {
        return attendanceDao.getAttendancePlatformCrew(
            platformName = platformName,
            scheduleId = scheduleId
        )
    }

    suspend fun getPlatformAttendanceList(scheduleId: Int): Response<TotalAttendanceResponse> {
        return attendanceDao.getAttendancePlatforms(
            scheduleId = scheduleId
        )
    }
}