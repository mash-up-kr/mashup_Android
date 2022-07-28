package com.mashup.ui.event

import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.dto.AttendanceCodeResponse
import com.mashup.data.dto.ContentResponse
import com.mashup.data.dto.EventResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.data.repository.ScheduleRepository
import com.mashup.ui.event.model.Event
import com.mashup.ui.event.model.EventAttendance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EventViewModel @Inject constructor(
    private val userDataSource: UserDataSource,
    private val scheduleRepository: ScheduleRepository,
) : BaseViewModel() {
    override fun handleErrorCode(code: String) {
    }

    fun getList(): Event {
        val contentList = listOf(
            ContentResponse(
                contentId = 1,
                title = "안드로이드 팀 세미나",
                content = "Android Crew",
                startedAt = "2022-07-02T15:30:00"
            ), ContentResponse(
                contentId = 2,
                title = "웹 팀 세미나",
                content = "Android Crew",
                startedAt = "2022-07-02T15:30:00"
            )
        )

        val list: ScheduleResponse = ScheduleResponse(
            scheduleId = 1,
            generationNum = 12,
            name = "1차 정기 세미나",
            eventList = listOf(
                EventResponse(
                    eventId = 1,
                    attendanceCode = AttendanceCodeResponse(
                        id = 1, eventId = 1, code = "testCode1", startedAt = "", endedAt = "",
                    ),
                    contentList = contentList,
                    startedAt = "",
                    endedAt = "",
                ),
                EventResponse(
                    eventId = 2,
                    attendanceCode = AttendanceCodeResponse(
                        id = 2, eventId = 2, code = "testCode2", startedAt = "", endedAt = "",
                    ),
                    contentList = contentList,
                    startedAt = "",
                    endedAt = "",
                )
            ),
            startedAt = "",
            endedAt = ""
        )

        return Event(id = list.scheduleId,
            title = list.name,
            startAt = list.startedAt,
            endAt = list.endedAt,
            eventAttendance = list.eventList.map {
                EventAttendance(it.eventId, "출석")
            })

    }
}