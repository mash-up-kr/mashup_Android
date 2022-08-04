package com.mashup.ui.schedule.model

data class EventDetail(
    val id: Int,
    val type: EventDetailType,
    val header: Header?,
    val body: Body?
)

data class Header(
    val eventId: Int,
    val startedAt: String,
    val endedAt: String
) {
    companion object {
        val EMPTY = Header(
            eventId = 1,
            startedAt = "2021-07-02T15:30:00",
            endedAt = "2023-07-02T16:30:00",
        )
    }

    fun getHeader() = "${eventId}부"
    fun getTimeStampStr() = "AM 11:00 - PM 2:00"
}

data class Body(
    val contentId: String,
    val title: String,
    val content: String,
    val startedAt: String,
) {
    companion object {
        val EMPTY = Body(
            contentId = "1",
            title = "안드로이드 팀 세미나",
            content = "Android Crew",
            startedAt = "PM 2:30"
        )
    }
}
