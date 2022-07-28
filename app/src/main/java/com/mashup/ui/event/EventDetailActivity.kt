package com.mashup.ui.event

import android.content.Context
import android.content.Intent
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.data.dto.AttendanceCodeResponse
import com.mashup.data.dto.ContentResponse
import com.mashup.data.dto.EventResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.databinding.ActivityEventDetailBinding
import com.mashup.ui.event.model.Body
import com.mashup.ui.event.model.EventDetail
import com.mashup.ui.event.model.EventDetailType
import com.mashup.ui.event.model.Header
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailActivity : BaseActivity<ActivityEventDetailBinding>() {
    override val layoutId = R.layout.activity_event_detail

    private val eventDetailAdapter by lazy {
        EventDetailAdapter().apply {
            setOnItemClickListener(object : EventDetailAdapter.OnItemEventListener {
                override fun onExitEventClick() {
                }
            })
        }
    }

    override fun initViews() {
        initButton()
        viewBinding.rvEvent.apply {
            adapter = eventDetailAdapter
            eventDetailAdapter.submitList(initList())
        }
    }

    private fun initList(): MutableList<EventDetail> {
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
            generationNum = 12, name = "1차 정기 세미나",
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

        val eventList = mutableListOf<EventDetail>()
        for (event in list.eventList) {
            eventList += EventDetail(
                0,
                EventDetailType.HEADER,
                Header(
                    eventId = event.eventId, startedAt = event.startedAt, endedAt = event.endedAt
                ),
                null
            )
            eventList += event.contentList.map {
                EventDetail(
                    event.eventId,
                    EventDetailType.CONTENT,
                    null,
                    Body(it.contentId.toString(), it.title, it.content, it.startedAt)
                )
            }
        }
        return eventList
    }

    private fun initButton() {
        viewBinding.btnReturn.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        fun start(context: Context?, action: Intent.() -> Unit = {}) {
            val intent = Intent(context, EventDetailActivity::class.java).apply(action)
            context?.startActivity(intent)
        }
    }

}
