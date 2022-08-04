package com.mashup.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.data.dto.ScheduleResponse
import com.mashup.databinding.ItemEventBinding
import com.mashup.extensions.onThrottleFirstClick

class ScheduleViewPagerAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ScheduleResponse, ScheduleViewHolder>(SCHEDULE_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ScheduleViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            listener
        )

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val SCHEDULE_DIFF_UTIL = object : DiffUtil.ItemCallback<ScheduleResponse>() {
            override fun areItemsTheSame(
                oldItem: ScheduleResponse,
                newItem: ScheduleResponse
            ) = oldItem.scheduleId == newItem.scheduleId

            override fun areContentsTheSame(
                oldItem: ScheduleResponse,
                newItem: ScheduleResponse
            ) = oldItem == newItem
        }
    }
}

class ScheduleViewHolder(
    private val binding: ItemEventBinding,
    private val listener: OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private var scheduleResponse: ScheduleResponse? = null

    init {
        itemView.findViewTreeLifecycleOwner()?.run {
            binding.layoutHistoryLevel.onThrottleFirstClick(lifecycleScope) {
                scheduleResponse?.run {
                    listener.onClickAttendanceList(scheduleId)
                }
            }

            binding.btnAttendanceList.onThrottleFirstClick(lifecycleScope) {
                scheduleResponse?.run {
                    listener.onClickCrewAttendanceActivity(scheduleId)
                }
            }
        }
    }

    fun onBind(data: ScheduleResponse) {
        scheduleResponse = data

        binding.tvTitle.text = data.name
        binding.tvDDay.text = data.getDDay()
        binding.tvCalender.text = data.getDate()
        binding.tvTimeLine.text = data.getTimeLine()
        binding.tvTitle.apply {
            text = String.format(
                context.resources.getString(R.string.event_list_card_title),
                "홍길동"
            )
        }
    }
}

interface OnItemClickListener {
    fun onClickAttendanceList(scheduleId: Int)
    fun onClickCrewAttendanceActivity(scheduleId: Int)
}