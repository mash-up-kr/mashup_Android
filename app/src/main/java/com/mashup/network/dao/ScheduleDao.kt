package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.ScheduleListResponse
import com.mashup.data.dto.ScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ScheduleDao {
    @GET("api/v1/schedule/{id}")
    suspend fun getSchedule(
        @Path("id") id: Int
    ): Response<ScheduleResponse>

    @GET("api/v1/schedules/generations/{generationNumber}")
    suspend fun getScheduleList(
        @Path("generationNumber") generationNumber: Int
    ): Response<ScheduleListResponse>
}