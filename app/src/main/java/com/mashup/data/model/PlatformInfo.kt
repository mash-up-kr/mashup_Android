package com.mashup.data.model

import com.squareup.moshi.Json
import java.io.Serializable

data class PlatformInfo(
    @Json(name = "attendanceCount")
    val attendanceCount: Int,
    @Json(name = "lateCount")
    val lateCount: Int,
    @Json(name = "totalCount")
    val totalCount: Int,
    @Json(name = "platform")
    val platform: Platform,
) : Serializable