package com.mashup.data.dto

import com.mashup.data.model.PlatformInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TotalAttendanceResponse(
    @field:Json(name = "isEnd")
    val isEnd: Boolean,
    @field:Json(name = "eventNum")
    val eventNum: Int,
    @field:Json(name = "platformInfos")
    val platformInfos: List<PlatformInfo>,
)