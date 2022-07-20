package com.mashup.data.dto

import com.mashup.data.model.MemberInfo
import com.mashup.data.model.Platform
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlatformAttendanceResponse(
    @field:Json(name = "members")
    val members: List<MemberInfo>,
    @field:Json(name = "platform")
    val platform: Platform
)
