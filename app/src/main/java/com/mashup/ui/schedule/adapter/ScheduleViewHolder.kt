package com.mashup.ui.schedule.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.common.extensions.fromHtml
import com.mashup.common.extensions.gone
import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.databinding.ItemEndScheduleBinding
import com.mashup.databinding.ItemInprogressScheduleBinding
import com.mashup.ui.attendance.model.AttendanceStatus
import com.mashup.ui.schedule.model.ScheduleCard
import java.text.SimpleDateFormat
import java.util.*

sealed class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    class EndScheduleCard(
        private val binding: ItemEndScheduleBinding,
        private val listener: OnItemClickListener
    ) : ScheduleViewHolder(binding.root) {

        private var scheduleResponse: ScheduleResponse? = null

        init {
            itemView.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickAttendanceList(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener
                    )
                }
            }

            binding.btnAttendanceList.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickCrewAttendanceActivity(
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
            binding.tvCardTitle.apply {
                text = String.format(
                    context.resources.getString(R.string.event_list_card_title),
                    data.attendanceInfo.memberName
                ).fromHtml()
            }

            onBindAttendanceImage(
                view = binding.timeline1.ivTimeline,
                attendanceStatus = data.attendanceInfo.getAttendanceStatus(0),
                isFinal = false
            )
            onBindAttendanceImage(
                view = binding.timeline2.ivTimeline,
                attendanceStatus = data.attendanceInfo.getAttendanceStatus(1),
                isFinal = false
            )
            onBindAttendanceImage(
                view = binding.timeline3.ivTimeline,
                attendanceStatus = data.attendanceInfo.getFinalAttendance(),
                isFinal = true
            )

            onBindAttendanceStatus(
                view = binding.timeline1.tvTimelineAttendance,
                attendanceStatus = data.attendanceInfo.getAttendanceStatus(0),
                isFinal = false
            )
            onBindAttendanceStatus(
                view = binding.timeline2.tvTimelineAttendance,
                attendanceStatus = data.attendanceInfo.getAttendanceStatus(1),
                isFinal = false
            )
            onBindAttendanceStatus(
                view = binding.timeline3.tvTimelineAttendance,
                attendanceStatus = data.attendanceInfo.getFinalAttendance(),
                isFinal = true
            )

            binding.timeline1.tvTimelineCaption.text = "1부"
            binding.timeline2.tvTimelineCaption.text = "2부"
            binding.timeline3.tvTimelineCaption.text = "최종"

            onBindAttendanceTime(
                binding.timeline1.tvTimelineTime,
                data.attendanceInfo.getAttendanceAt(0)
            )
            onBindAttendanceTime(
                binding.timeline2.tvTimelineTime,
                data.attendanceInfo.getAttendanceAt(1)
            )
            binding.timeline3.tvTimelineTime.gone()
        }

        private fun onBindAttendanceImage(
            view: ImageView,
            attendanceStatus: AttendanceStatus,
            isFinal: Boolean
        ) {
            val drawableRes = when (attendanceStatus) {
                AttendanceStatus.ABSENT -> {
                    if (isFinal) {
                        R.drawable.ic_absent_final
                    } else {
                        R.drawable.ic_absent_default
                    }
                }
                AttendanceStatus.ATTENDANCE -> {
                    if (isFinal) {
                        R.drawable.ic_attendance_final
                    } else {
                        R.drawable.ic_attendance_default
                    }
                }
                AttendanceStatus.LATE -> {
                    if (isFinal) {
                        R.drawable.ic_late_final
                    } else {
                        R.drawable.ic_late_default
                    }
                }
                else -> {
                    R.drawable.ic_attendance_not_yet
                }
            }
            view.setImageResource(drawableRes)
        }

        private fun onBindAttendanceStatus(
            view: TextView,
            attendanceStatus: AttendanceStatus,
            isFinal: Boolean
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
            time: Date?
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
        private val listener: OnItemClickListener
    ) : ScheduleViewHolder(binding.root) {

        private var scheduleResponse: ScheduleResponse? = null

        init {
            itemView.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickAttendanceList(
                        scheduleResponse?.scheduleId ?: return@setOnClickListener
                    )
                }
            }

            binding.btnAttendanceList.setOnClickListener {
                if (scheduleResponse?.eventList.isNullOrEmpty()) {
                    listener.onClickEmptySchedule()
                } else {
                    listener.onClickCrewAttendanceActivity(
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
            onBindButton(data.scheduleResponse)
        }

        private fun onBindEmptyContent() {
            binding.tvTitle.text = itemView.context.getString(R.string.title_content_empty_schedule)
            binding.tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray400))
            binding.tvDDay.text = itemView.context.getString(R.string.unknown_content_d_day)
            binding.tvCalender.text = "-"
            binding.tvTimeLine.text = "-"
        }

        private fun onBindContent(data: ScheduleResponse) {
            binding.tvTitle.text = data.name
            binding.tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray900))
            binding.tvDDay.text = data.getDDay()
            binding.tvCalender.text = data.getDate()
            binding.tvTimeLine.text = data.getTimeLine()
        }

        private fun onBindEmptyImage() {
            binding.ivSchedule.setImageResource(R.drawable.img_placeholder_sleeping)
            binding.tvDescription.text =
                itemView.context.getString(R.string.description_empty_schedule)
        }

        private fun onBindStandbyImage(data: AttendanceInfoResponse?) {
            binding.ivSchedule.setImageResource(R.drawable.img_standby)
            binding.tvDescription.text =
                itemView.context.getString(
                    R.string.description_standby_schedule,
                    data?.memberName ?: "알 수 없음"
                ).fromHtml()
        }

        private fun onBindButton(scheduleResponse: ScheduleResponse) {
            binding.btnAttendanceList.isVisible = scheduleResponse.dateCount > 0
        }
    }
}