package com.mashup.ui.event.model

class Event(
    val id: Int,
    val title: String,
    val startAt: String,
    val endAt: String,
    val eventAttendance: List<EventAttendance>
) {
    fun getDDay() = "D-12"
    fun getDate() = "3월 27일"
    fun getTimeLine() = "오후 3:00 - 오전 7:00"
}


data class EventAttendance(
    val eventId: Int,
    val res: String,
) {
    companion object {
        val EMPTY = EventAttendance(
            eventId = 1,
            res = "출석",
        )
    }
}
