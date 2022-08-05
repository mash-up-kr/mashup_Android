package com.mashup.ui.schedule.model

import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.ScheduleResponse

sealed interface ScheduleCard {
    data class EmptySchedule(val scheduleResponse: ScheduleResponse? = null) : ScheduleCard {
        override fun getScheduleId(): Int? = null
    }

    data class EndSchedule(
        val scheduleResponse: ScheduleResponse,
        val attendanceInfo: AttendanceInfoResponse
    ) : ScheduleCard {
        override fun getScheduleId() = scheduleResponse.scheduleId
    }

    data class InProgressSchedule(
        val scheduleResponse: ScheduleResponse,
        val attendanceInfo: AttendanceInfoResponse?
    ) : ScheduleCard {
        override fun getScheduleId() = scheduleResponse.scheduleId
    }

    fun getScheduleId(): Int?
}