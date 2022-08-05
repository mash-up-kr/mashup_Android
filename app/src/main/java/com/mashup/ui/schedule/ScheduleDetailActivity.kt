package com.mashup.ui.schedule

import android.content.Context
import android.content.Intent
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityScheduleDetailBinding
import com.mashup.ui.constant.EXTRA_SCHEDULE_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailActivity : BaseActivity<ActivityScheduleDetailBinding>() {
    override val layoutId = R.layout.activity_schedule_detail

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
            eventDetailAdapter.submitList(emptyList())
        }
    }

//    private fun initList(): MutableList<EventDetail> {
//        val contentList = listOf(
//            ContentResponse(
//                contentId = 1,
//                title = "안드로이드 팀 세미나",
//                content = "Android Crew",
//                startedAt = "2022-07-02T15:30:00"
//            ), ContentResponse(
//                contentId = 2,
//                title = "웹 팀 세미나",
//                content = "Android Crew",
//                startedAt = "2022-07-02T15:30:00"
//            )
//        )
//
//        val list: ScheduleResponse = ScheduleResponse(
//            scheduleId = 1,
//            generationNum = 12, name = "1차 정기 세미나",
//            eventList = listOf(
//                EventResponse(
//                    eventId = 1,
//                    attendanceCode = AttendanceCodeResponse(
//                        id = 1, eventId = 1, code = "testCode1", startedAt = "", endedAt = "",
//                    ),
//                    contentList = contentList,
//                    startedAt = "",
//                    endedAt = "",
//                ),
//                EventResponse(
//                    eventId = 2,
//                    attendanceCode = AttendanceCodeResponse(
//                        id = 2, eventId = 2, code = "testCode2", startedAt = "", endedAt = "",
//                    ),
//                    contentList = contentList,
//                    startedAt = "",
//                    endedAt = "",
//                )
//            ),
//            startedAt = "",
//            endedAt = ""
//        )
//
//        val eventList = mutableListOf<EventDetail>()
//        for (event in list.eventList) {
//            eventList += EventDetail(
//                0,
//                EventDetailType.HEADER,
//                Header(
//                    eventId = event.eventId, startedAt = event.startedAt, endedAt = event.endedAt
//                ),
//                null
//            )
//            eventList += event.contentList.map {
//                EventDetail(
//                    event.eventId,
//                    EventDetailType.CONTENT,
//                    null,
//                    Body(it.contentId.toString(), it.title, it.content, it.startedAt)
//                )
//            }
//        }
//        return eventList
//    }

    private fun initButton() {
        viewBinding.btnReturn.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        fun newIntent(context: Context, scheduleId: Int) =
            Intent(context, ScheduleDetailActivity::class.java).apply {
                putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            }
    }

}
