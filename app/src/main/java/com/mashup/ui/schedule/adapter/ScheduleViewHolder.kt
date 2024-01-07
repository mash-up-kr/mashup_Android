package com.mashup.ui.schedule.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.core.common.extensions.fromHtml
import com.mashup.core.common.extensions.gone
import com.mashup.core.model.AttendanceStatus
import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.EventResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.databinding.ItemCardTitleBinding
import com.mashup.databinding.ItemEndScheduleBinding
import com.mashup.databinding.ItemInprogressScheduleBinding
import com.mashup.ui.schedule.ViewEventTimeline
import com.mashup.ui.schedule.model.ScheduleCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class EndScheduleCard(
        private val binding: ItemEndScheduleBinding,
        private val listener: OnItemClickListener,
        private val enabledSwipeRefreshLayout: (Boolean) -> Unit = {}
    ) : ScheduleViewHolder(binding.root) {

        private var scheduleResponse: ScheduleResponse? = null

        init {
            binding.layoutCard.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickScheduleInformation(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener
                    )
                }
            }

            binding.btnAttendanceList.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickAttendanceInfoButton(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener
                    )
                }
            }
        }

        fun onBind(data: ScheduleCard.EndSchedule) {
            scheduleResponse = data.scheduleResponse

            binding.tvTitle.text = data.scheduleResponse.name
            binding.tvDDay.text = data.scheduleResponse.getDDay()
            binding.tvCalender.text = data.scheduleResponse.getDate()
            binding.tvTimeLine.text = data.scheduleResponse.getTimeLine()

            binding.cvTimeLine.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
                setContent {
                    if (scheduleResponse?.eventList?.isEmpty() == true) {
                        return@setContent
                    }

                    val listState = rememberLazyListState()
                    LaunchedEffect(listState) {
                        snapshotFlow { listState.firstVisibleItemScrollOffset }
                            .collect { offset ->
                                enabledSwipeRefreshLayout(offset == 0)
                            }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        item {
                            AndroidViewBinding(ItemCardTitleBinding::inflate) {
                                this.tvCardTitle.apply {
                                    text = String.format(
                                        context.resources.getString(
                                            R.string.event_list_card_title
                                        ),
                                        data.attendanceInfo.memberName
                                    ).fromHtml()
                                }
                            }
                        }
                        if (scheduleResponse!!.eventList.isEmpty().not()) {
                            item {
                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )
                            }
                        }
                        itemsIndexed(scheduleResponse!!.eventList, key = { _: Int, item: EventResponse ->
                            item.eventId
                        }) { index: Int, _: EventResponse ->
                            val isFinal = index == scheduleResponse!!.eventList.lastIndex
                            ViewEventTimeline(
                                modifier = Modifier.fillMaxWidth(),
                                caption = if (isFinal) {
                                    stringResource(id = R.string.attendance_final)
                                } else {
                                    stringResource(id = R.string.attendance_caption, index + 1)
                                },
                                time = onBindAttendanceTime(
                                    data.attendanceInfo.getAttendanceAt(index)
                                ),
                                status = onBindAttendanceStatus(
                                    if (isFinal) {
                                        data.attendanceInfo.getFinalAttendance()
                                    } else {
                                        data.attendanceInfo.getAttendanceStatus(
                                            index
                                        )
                                    },
                                    isFinal = isFinal
                                ),
                                image = onBindAttendanceImage(
                                    if (isFinal) {
                                        data.attendanceInfo.getFinalAttendance()
                                    } else {
                                        data.attendanceInfo.getAttendanceStatus(
                                            index
                                        )
                                    },
                                    isFinal = isFinal
                                ),
                                isFinal = isFinal
                            )
                        }
                    }
                }
            }
        }

        private fun onBindAttendanceImage(
            attendanceStatus: AttendanceStatus,
            isFinal: Boolean
        ): Int {
            return when (attendanceStatus) {
                AttendanceStatus.ABSENT -> {
                    if (isFinal) {
                        com.mashup.core.common.R.drawable.ic_absent_final
                    } else {
                        com.mashup.core.common.R.drawable.ic_absent_default
                    }
                }
                AttendanceStatus.ATTENDANCE -> {
                    if (isFinal) {
                        com.mashup.core.common.R.drawable.ic_attendance_final
                    } else {
                        com.mashup.core.common.R.drawable.ic_attendance_default
                    }
                }
                AttendanceStatus.LATE -> {
                    if (isFinal) {
                        com.mashup.core.common.R.drawable.ic_late_final
                    } else {
                        com.mashup.core.common.R.drawable.ic_late_default
                    }
                }
                else -> {
                    com.mashup.core.common.R.drawable.ic_attendance_not_yet
                }
            }
        }

        private fun onBindAttendanceStatus(
            attendanceStatus: AttendanceStatus,
            isFinal: Boolean
        ): Int {
            return when (attendanceStatus) {
                AttendanceStatus.ABSENT -> {
                    if (isFinal) { R.string.attendance_status_absent_final } else { R.string.attendance_status_absent }
                }
                AttendanceStatus.ATTENDANCE -> {
                    if (isFinal) { R.string.attendance_status_attendance_final } else { R.string.attendance_status_attendance }
                }
                AttendanceStatus.LATE -> {
                    if (isFinal) { R.string.attendance_status_late_final } else { R.string.attendance_status_late }
                }
                else -> { R.string.attendance_nothing }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun onBindAttendanceTime(
            time: Date?
        ): String {
            return if (time != null) {
                try {
                    SimpleDateFormat("a hh:mm", Locale.KOREA).format(time)
                } catch (ignore: Exception) {
                    "-"
                }
            } else {
                "-"
            }
        }
    }

    class InProgressScheduleCard(
        private val binding: ItemInprogressScheduleBinding,
        private val listener: OnItemClickListener
    ) : ScheduleViewHolder(binding.root) {

        private var scheduleResponse: ScheduleResponse? = null

        init {
            binding.layoutCard.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickScheduleInformation(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener
                    )
                }
            }

            binding.btnAttendanceList.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickAttendanceInfoButton(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener
                    )
                }
            }
        }

        fun onBind(data: ScheduleCard) {
            when (data) {
                is ScheduleCard.EmptySchedule -> {
                    onBind(data)
                }
                is ScheduleCard.InProgressSchedule -> {
                    onBind(data)
                }
                else -> {
                }
            }
        }

        private fun onBind(data: ScheduleCard.EmptySchedule) {
            scheduleResponse = null

            if (data.scheduleResponse == null) {
                onBindEmptyContent()
            } else {
                onBindContent(data.scheduleResponse)
            }
            onBindEmptyImage()
            binding.btnAttendanceList.gone()
        }

        private fun onBind(data: ScheduleCard.InProgressSchedule) {
            scheduleResponse = data.scheduleResponse

            onBindContent(data.scheduleResponse)
            onBindStandbyImage(data.attendanceInfo)
            onBindButton(data)
        }

        private fun onBindEmptyContent() {
            binding.tvTitle.text = itemView.context.getString(R.string.title_content_empty_schedule)
            binding.tvTitle.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.mashup.core.common.R.color.gray400
                )
            )
            binding.tvDDay.text = itemView.context.getString(R.string.unknown_content_d_day)
            binding.tvCalender.text = "-"
            binding.tvTimeLine.text = "-"
        }

        private fun onBindContent(data: ScheduleResponse) {
            binding.tvTitle.text = data.name
            binding.tvTitle.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.mashup.core.common.R.color.gray900
                )
            )
            binding.tvDDay.text = data.getDDay()
            binding.tvCalender.text = data.getDate()
            binding.tvTimeLine.text = data.getTimeLine()
        }

        private fun onBindEmptyImage() {
            binding.ivSchedule.setImageResource(
                com.mashup.core.common.R.drawable.img_placeholder_sleeping
            )
            binding.tvDescription.text =
                itemView.context.getString(R.string.description_empty_schedule)
        }

        private fun onBindStandbyImage(data: AttendanceInfoResponse?) {
            binding.ivSchedule.setImageResource(com.mashup.core.common.R.drawable.img_standby)
            binding.tvDescription.text =
                itemView.context.getString(
                    R.string.description_standby_schedule,
                    data?.memberName ?: "알 수 없음"
                ).fromHtml()
        }

        private fun onBindButton(data: ScheduleCard.InProgressSchedule) {
            binding.btnAttendanceList.isVisible = data.attendanceInfo != null
        }
    }
}
