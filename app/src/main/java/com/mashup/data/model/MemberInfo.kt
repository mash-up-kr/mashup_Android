package com.mashup.data.model

import com.squareup.moshi.Json

data class MemberInfo(
    @Json(name = "name")
    val name: String,
    @Json(name = "attendanceInfos")
    val attendanceInfos: List<AttendanceInfo>
)