package com.mashup.data.dto

import com.mashup.data.model.PlatformInfo
import com.squareup.moshi.Json

data class TotalAttendanceResponse(
    @Json(name = "isEnd")
    val isEnd: Boolean,
    @Json(name = "platformInfos")
    val platformInfos: List<PlatformInfo>,
)