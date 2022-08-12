package com.mashup.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class PlatformInfo(
    @Json(name = "attendanceCount")
    val attendanceCount: Int? = 0,
    @Json(name = "lateCount")
    val lateCount: Int? = 0,
    @Json(name = "totalCount")
    val totalCount: Int,
    @Json(name = "platform")
    val platform: Platform
) : Serializable