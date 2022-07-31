package com.mashup.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemEventTimelineContentBinding
import com.mashup.databinding.ItemEventTimelineHeaderBinding
import com.mashup.ui.event.model.EventDetail
import com.mashup.ui.event.model.EventDetailType

class EventDetailAdapter :
    ListAdapter<EventDetail, RecyclerView.ViewHolder>(EventComparator) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.num
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EventDetailType.HEADER.num -> {
                TitleViewHolder(parent)
            }
            else -> {
                ContentViewHolder(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> {
                holder.bind(getItem(position))
            }
            is ContentViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class TitleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        ItemEventTimelineHeaderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).root
    ) {
        private val binding: ItemEventTimelineHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(item: EventDetail) {
            binding?.model = item
            if (item.header?.eventId == 1) {
                binding?.line?.visibility = View.GONE
            }
        }
    }

    class ContentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        ItemEventTimelineContentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).root
    ) {
        private val binding: ItemEventTimelineContentBinding? =
            DataBindingUtil.bind(itemView)

        fun bind(item: EventDetail) {
            binding?.model = item
        }
    }

    interface OnItemEventListener {
        fun onExitEventClick()
    }

    private var mListener: OnItemEventListener? = null

    fun setOnItemClickListener(listener: OnItemEventListener?) {
        mListener = listener
    }
}


object EventComparator : DiffUtil.ItemCallback<EventDetail>() {
    override fun areItemsTheSame(
        oldItem: EventDetail,
        newItem: EventDetail
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: EventDetail,
        newItem: EventDetail
    ): Boolean {
        return oldItem == newItem
    }
}