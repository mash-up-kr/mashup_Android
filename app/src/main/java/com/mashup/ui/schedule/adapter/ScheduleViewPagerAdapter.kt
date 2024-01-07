package com.mashup.ui.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.mashup.databinding.ItemEndScheduleBinding
import com.mashup.databinding.ItemInprogressScheduleBinding
import com.mashup.ui.schedule.model.ScheduleCard

class ScheduleViewPagerAdapter(
    private val listener: OnItemClickListener,
    private val enabledSwipeRefreshLayout: (Boolean) -> Unit = {}
) :
    ListAdapter<ScheduleCard, ScheduleViewHolder>(SCHEDULE_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return when (viewType) {
            TYPE_END_SCHEDULE -> {
                ScheduleViewHolder.EndScheduleCard(
                    ItemEndScheduleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener,
                    enabledSwipeRefreshLayout = enabledSwipeRefreshLayout
                )
            }
            else -> {
                ScheduleViewHolder.InProgressScheduleCard(
                    ItemInprogressScheduleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    listener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        when (holder) {
            is ScheduleViewHolder.InProgressScheduleCard -> {
                holder.onBind(getItem(position))
            }
            is ScheduleViewHolder.EndScheduleCard -> {
                holder.onBind(getItem(position) as ScheduleCard.EndSchedule)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ScheduleCard.EndSchedule -> {
                TYPE_END_SCHEDULE
            }
            else -> {
                TYPE_IN_PROGRESS_SCHEDULE
            }
        }
    }

    companion object {
        private const val TYPE_IN_PROGRESS_SCHEDULE = 0
        private const val TYPE_END_SCHEDULE = 1

        private val SCHEDULE_DIFF_UTIL = object : DiffUtil.ItemCallback<ScheduleCard>() {
            override fun areItemsTheSame(
                oldItem: ScheduleCard,
                newItem: ScheduleCard
            ) = oldItem.getScheduleId() == newItem.getScheduleId()

            override fun areContentsTheSame(
                oldItem: ScheduleCard,
                newItem: ScheduleCard
            ) = oldItem == newItem
        }
    }
}

interface OnItemClickListener {
    fun onClickEmptySchedule()
    fun onClickScheduleInformation(scheduleId: Int)
    fun onClickAttendanceInfoButton(scheduleId: Int)
}
