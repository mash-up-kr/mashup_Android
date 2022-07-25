package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.AttendanceCheckRequest
import com.mashup.data.dto.AttendanceCheckResponse
import com.mashup.data.dto.PlatformAttendanceResponse
import com.mashup.data.dto.TotalAttendanceResponse
import retrofit2.http.*

interface AttendanceDao {

    @POST("api/v1/attendance/check")
    suspend fun postAttendanceCheck(
        @Body attendanceCheckRequest: AttendanceCheckRequest
    ): Response<AttendanceCheckResponse>

    @GET("api/v1/attendance/platforms")
    suspend fun getAttendancePlatforms(
        @Query("scheduleId") scheduleId: Int
    ): Response<TotalAttendanceResponse>

    @GET("api/v1/attendance/platforms/{platformName}")
    suspend fun getAttendancePlatformCrew(
        @Path("platformName") platformName: String,
        @Query("scheduleId") scheduleId: Int
    ): Response<PlatformAttendanceResponse>
}