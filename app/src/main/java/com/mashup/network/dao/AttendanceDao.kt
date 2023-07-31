package com.mashup.network.dao

import com.mashup.core.network.Response
import com.mashup.data.dto.AttendanceCheckRequest
import com.mashup.data.dto.AttendanceCheckResponse
import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.PlatformAttendanceResponse
import com.mashup.data.dto.TotalAttendanceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
