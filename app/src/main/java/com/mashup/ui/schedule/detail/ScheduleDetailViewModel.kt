package com.mashup.ui.schedule.detail

import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.dto.ContentResponse
import com.mashup.data.dto.EventResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.data.repository.ScheduleRepository
import com.mashup.ui.schedule.model.Body
import com.mashup.ui.schedule.model.EventDetail
import com.mashup.ui.schedule.model.EventDetailType
import com.mashup.ui.schedule.model.Header
import com.mashup.ui.schedule.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScheduleDetailViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val scheduleId =
        checkNotNull(
            savedStateHandle.get<Int>(EXTRA_SCHEDULE_ID)
        )

    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Empty)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState

    init {
        getSchedule()
    }

    private fun getSchedule() {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Loading)
            scheduleRepository.getSchedule(scheduleId)
                .onSuccess { response ->
                    _scheduleState.emit(
                        ScheduleState.Success(
                            getEventDetailList(response.eventList, response.location)
                        )
                    )
                }
                .onFailure { code ->
                    handleErrorCode(code)
                }
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Error(code))
        }
    }

    private fun getEventDetailList(
        eventList: List<EventResponse>,
        location: ScheduleResponse.Location?
    ): List<EventDetail> {
        var itemId = 0
        val eventDetailList = mutableListOf<EventDetail>()

        if (location?.placeName != null) { // 위치 정보가 있는 경우(온라인이면 placeName이 Zoom으로 내려옴)
            eventDetailList.add(mapToLocationModel(itemId++, location))
        }

        eventList.forEachIndexed { eventIndex, event ->
            eventDetailList.add(mapToHeaderModel(itemId++, eventIndex, event))

            event.contentList.forEachIndexed { contentIndex, content ->
                eventDetailList.add(mapToContentModel(itemId++, contentIndex, content))
            }
        }

        return eventDetailList
    }

    private fun mapToHeaderModel(itemId: Int, eventIndex: Int, event: EventResponse): EventDetail {
        return EventDetail(
            id = itemId,
            type = EventDetailType.HEADER,
            header = Header(
                eventId = eventIndex + 1,
                startedAt = event.startedAt,
                endedAt = event.endedAt
            ),
            body = null,
            location = null
        )
    }

    private fun mapToContentModel(
        itemId: Int,
        contentIndex: Int,
        content: ContentResponse
    ): EventDetail {
        return EventDetail(
            id = itemId,
            type = EventDetailType.CONTENT,
            header = null,
            body = Body(
                contentId = "${contentIndex + 1}",
                title = content.title,
                content = content.content.orEmpty(),
                startedAt = content.startedAt
            ),
            location = null
        )
    }

    private fun mapToLocationModel(itemId: Int, location: ScheduleResponse.Location): EventDetail {
        return EventDetail(
            id = itemId,
            type = EventDetailType.LOCATION,
            header = null,
            body = null,
            location = Location(
                placeName = location.placeName.orEmpty(),
                address = location.address.orEmpty(),
                latitude = location.latitude,
                longitude = location.longitude
            )
        )
    }
}

sealed interface ScheduleState {
    object Empty : ScheduleState
    object Loading : ScheduleState
    data class Success(
        val eventDetailList: List<EventDetail>
    ) : ScheduleState

    data class Error(val code: String) : ScheduleState
}
