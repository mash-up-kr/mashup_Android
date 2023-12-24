package com.mashup.ui.schedule.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.core.common.databinding.ViewEventTimelineBinding
import com.mashup.core.common.extensions.fromHtml
import com.mashup.core.common.extensions.gone
import com.mashup.core.model.AttendanceStatus
import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.EventResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.databinding.ItemCardTitleBinding
import com.mashup.databinding.ItemEndScheduleBinding
import com.mashup.databinding.ItemInprogressScheduleBinding
import com.mashup.ui.schedule.model.ScheduleCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class EndScheduleCard(
        private val binding: ItemEndScheduleBinding,
        private val listener: OnItemClickListener,
    ) : ScheduleViewHolder(binding.root) {

        private var scheduleResponse: ScheduleResponse? = null

        init {
            itemView.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickScheduleInformation(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener,
                    )
                }
            }

            binding.btnAttendanceList.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickAttendanceInfoButton(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener,
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
                    } else {
                        var componentHeight by remember { mutableStateOf(0.dp) }
                        val density = LocalDensity.current
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().onGloballyPositioned {
                                componentHeight = with(density) {
                                    it.size.height.toDp()
                                }
                            },
                            horizontalAlignment = Alignment.Start,
                        ) {
                            item {
                                AndroidViewBinding(ItemCardTitleBinding::inflate) {
                                    this.tvCardTitle.apply {
                                        text = String.format(
                                            context.resources.getString(R.string.event_list_card_title),
                                            data.attendanceInfo.memberName,
                                        ).fromHtml()
                                    }
                                }
                            }
                            itemsIndexed(scheduleResponse!!.eventList, key = { index: Int, item: EventResponse ->
                                item.eventId
                            }) { index: Int, item: EventResponse ->
                                val isFinal = index == scheduleResponse!!.eventList.lastIndex
                                AndroidViewBinding(ViewEventTimelineBinding::inflate) {
                                    onBindAttendanceImage(
                                        this.ivTimeline,
                                        attendanceStatus = if (isFinal) data.attendanceInfo.getFinalAttendance() else data.attendanceInfo.getAttendanceStatus(index),
                                        isFinal = isFinal,
                                    )
                                    onBindAttendanceStatus(
                                        this.tvTimelineAttendance,
                                        attendanceStatus = if (isFinal) data.attendanceInfo.getFinalAttendance() else data.attendanceInfo.getAttendanceStatus(index),
                                        isFinal = isFinal,
                                    )
                                    onBindAttendanceTime(
                                        this.tvTimelineTime,
                                        data.attendanceInfo.getAttendanceAt(index),
                                    )
                                    this.tvTimelineCaption.text = if (isFinal) {
                                        "최종"
                                    } else {
                                        "${index + 1}부"
                                    }
                                    if (isFinal)this.tvTimelineTime.gone()
                                }
                                if (!isFinal) {
                                    Row {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Divider(
                                            modifier = Modifier.width(1.dp).height(
                                                componentHeight * 16 / 220,
                                            ),
                                            thickness = 1.dp,
                                            color = com.mashup.core.ui.colors.Gray200,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun onBindAttendanceImage(
            view: ImageView,
            attendanceStatus: AttendanceStatus,
            isFinal: Boolean,
        ) {
            val drawableRes = when (attendanceStatus) {
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
            view.setImageResource(drawableRes)
        }

        private fun onBindAttendanceStatus(
            view: TextView,
            attendanceStatus: AttendanceStatus,
            isFinal: Boolean,
        ) {
            val text = when (attendanceStatus) {
                AttendanceStatus.ABSENT -> {
                    if (isFinal) {
                        "슬프지만 결석이에요..."
                    } else {
                        "결석"
                    }
                }
                AttendanceStatus.ATTENDANCE -> {
                    if (isFinal) {
                        "출석을 완료했어요!"
                    } else {
                        "출석"
                    }
                }
                AttendanceStatus.LATE -> {
                    if (isFinal) {
                        "아쉽지만 지각이에요..."
                    } else {
                        "지각"
                    }
                }
                else -> {
                    "-"
                }
            }
            view.text = text
        }

        @SuppressLint("SimpleDateFormat")
        fun onBindAttendanceTime(
            view: TextView,
            time: Date?,
        ) {
            val timeString = if (time != null) {
                try {
                    SimpleDateFormat("a hh:mm", Locale.KOREA).format(time)
                } catch (ignore: Exception) {
                    "-"
                }
            } else {
                "-"
            }
            view.text = timeString
        }
    }

    class InProgressScheduleCard(
        private val binding: ItemInprogressScheduleBinding,
        private val listener: OnItemClickListener,
    ) : ScheduleViewHolder(binding.root) {

        private var scheduleResponse: ScheduleResponse? = null

        init {
            itemView.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickScheduleInformation(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener,
                    )
                }
            }

            binding.btnAttendanceList.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickAttendanceInfoButton(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener,
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
                    com.mashup.core.common.R.color.gray400,
                ),
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
                    com.mashup.core.common.R.color.gray900,
                ),
            )
            binding.tvDDay.text = data.getDDay()
            binding.tvCalender.text = data.getDate()
            binding.tvTimeLine.text = data.getTimeLine()
        }

        private fun onBindEmptyImage() {
            binding.ivSchedule.setImageResource(com.mashup.core.common.R.drawable.img_placeholder_sleeping)
            binding.tvDescription.text =
                itemView.context.getString(R.string.description_empty_schedule)
        }

        private fun onBindStandbyImage(data: AttendanceInfoResponse?) {
            binding.ivSchedule.setImageResource(com.mashup.core.common.R.drawable.img_standby)
            binding.tvDescription.text =
                itemView.context.getString(
                    R.string.description_standby_schedule,
                    data?.memberName ?: "알 수 없음",
                ).fromHtml()
        }

        private fun onBindButton(data: ScheduleCard.InProgressSchedule) {
            binding.btnAttendanceList.isVisible = data.attendanceInfo != null
        }
    }
}