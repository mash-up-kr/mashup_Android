package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AttendanceCheckResponse(
    @field:Json(name = "status")
    val status: String
) {
    fun isAttendance() = status.uppercase() == "ATTENDANCE"
}
