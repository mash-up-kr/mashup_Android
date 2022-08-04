package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleListResponse(
    @field:Json(name = "progress")
    val progress: SchedulesProgress,
    @field:Json(name = "dateCount")
    val dateCount: Int,
    @field:Json(name = "scheduleList")
    val scheduleList: List<ScheduleResponse>
)
