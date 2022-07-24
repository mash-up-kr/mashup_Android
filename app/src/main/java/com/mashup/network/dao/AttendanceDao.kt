package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.AttendanceCheckResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AttendanceDao {

    @POST("api/v1/attendance/check/{eventId}")
    suspend fun postAttendanceCheck(
        @Path("eventId") eventId: Int
    ): Response<AttendanceCheckResponse>
}