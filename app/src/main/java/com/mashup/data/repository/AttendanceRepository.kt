package com.mashup.data.repository

import com.mashup.common.Response
import com.mashup.data.dto.AttendanceCheckRequest
import com.mashup.data.dto.AttendanceCheckResponse
import com.mashup.network.dao.AttendanceDao
import com.mashup.ui.attendance.model.CrewAttendance
import com.mashup.ui.attendance.model.PlatformAttendance
import com.mashup.ui.model.Platform
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

    suspend fun getNotice(): String {
        return "공지사항입니다."
    }

    suspend fun getCrewAttendanceList(): List<CrewAttendance> {
        return emptyList()
    }

    suspend fun getPlatformList(): List<PlatformAttendance> {
        return listOf(
            PlatformAttendance(
                platform = Platform.DESIGN,
                numberOfAttend = 7,
                numberOfAbsence = 10,
                numberOfLateness = 0
            ),
            PlatformAttendance(
                platform = Platform.ANDROID,
                numberOfAttend = 3,
                numberOfAbsence = 18,
                numberOfLateness = 0
            ),
            PlatformAttendance(
                platform = Platform.WEB,
                numberOfAttend = 10,
                numberOfAbsence = 20,
                numberOfLateness = 0
            ),
            PlatformAttendance(
                platform = Platform.IOS,
                numberOfAttend = 9,
                numberOfAbsence = 10,
                numberOfLateness = 0
            )
        )
    }
}