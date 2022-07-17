package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.AttendanceCheckRequest
import com.mashup.data.dto.AttendanceCheckResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AttendanceDao {

    @POST("api/v1/attendance/check")
    suspend fun postAttendanceCheck(
        @Body attendanceCheckRequest: AttendanceCheckRequest
    ): Response<AttendanceCheckResponse>
}