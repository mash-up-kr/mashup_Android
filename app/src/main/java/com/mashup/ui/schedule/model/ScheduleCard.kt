package com.mashup.ui.schedule.model

import com.mashup.data.dto.ScheduleResponse

sealed interface ScheduleCard {
    object EmptySchedule : ScheduleCard
    data class EndSchedule(val scheduleResponse: ScheduleResponse) : ScheduleCard
    data class InProgressSchedule(val scheduleResponse: ScheduleResponse) : ScheduleCard
}