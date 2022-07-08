package com.mashup.data.dto

import com.mashup.data.model.MemberInfo
import com.mashup.data.model.Platform
import com.squareup.moshi.Json

data class PlatformAttendanceResponse(
    @Json(name = "members")
    val members: MemberInfo,
    @Json(name = "platform")
    val platform: Platform
)
