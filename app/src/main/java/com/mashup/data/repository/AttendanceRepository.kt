package com.mashup.data.repository

import com.mashup.data.dto.*
import com.mashup.network.Response
import com.mashup.network.dao.AttendanceDao
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao
) {
    suspend fun attendanceCheck(code: String): Response<AttendanceCheckResponse> {
        return attendanceDao.postAttendanceCheck(
            AttendanceCheckRequest(code)
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

    suspend fun getPlatformAttendanceList(
        scheduleId: Int
    ): Response<TotalAttendanceResponse> {
        return attendanceDao.getAttendancePlatforms(
            scheduleId = scheduleId
        )
    }

    suspend fun getScheduleAttendanceInfo(
        scheduleId: Int
    ): Response<AttendanceInfoResponse> {
        return attendanceDao.getAttendanceInSchedule(
            scheduleId = scheduleId
        )
    }
}