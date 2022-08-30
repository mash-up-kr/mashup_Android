package com.mashup.network.dao

import com.mashup.data.dto.*
import com.mashup.network.Response
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

    @GET("api/v1/attendance/schedules/{scheduleId}")
    suspend fun getAttendanceInSchedule(
        @Path("scheduleId") scheduleId: Int
    ): Response<AttendanceInfoResponse>
}