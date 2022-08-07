package com.mashup.ui.schedule.detail

import androidx.lifecycle.SavedStateHandle
import com.mashup.base.BaseViewModel
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.data.dto.EventResponse
import com.mashup.data.repository.ScheduleRepository
import com.mashup.ui.schedule.model.Body
import com.mashup.ui.schedule.model.EventDetail
import com.mashup.ui.schedule.model.EventDetailType
import com.mashup.ui.schedule.model.Header
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

    fun getSchedule() {
        mashUpScope {
            val response = scheduleRepository.getSchedule(scheduleId)

            if (!response.isSuccess() || response.data == null) {
                handleErrorCode(response.code)
                return@mashUpScope
            }
            _scheduleState.emit(
                ScheduleState.Success(
                    getEventDetailList(response.data.eventList)
                )
            )
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Error(code))
        }
    }

    private fun getEventDetailList(eventList: List<EventResponse>): List<EventDetail> {
        return mutableListOf<EventDetail>().apply {
            for (event in eventList) {
                add(
                    EventDetail(
                        0,
                        EventDetailType.HEADER,
                        Header(
                            eventId = event.eventId,
                            startedAt = event.startedAt,
                            endedAt = event.endedAt
                        ),
                        null
                    )
                )
                event.contentList.forEach {
                    add(
                        EventDetail(
                            event.eventId,
                            EventDetailType.CONTENT,
                            null,
                            Body(it.contentId.toString(), it.title, it.content, it.startedAt)
                        )
                    )
                }
            }
        }
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