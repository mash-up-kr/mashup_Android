package com.mashup.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.databinding.ItemEventTimelineContentBinding
import com.mashup.databinding.ItemEventTimelineHeaderBinding
import com.mashup.ui.model.EventDetail

class EventDetailAdapter :
    ListAdapter<EventDetail, RecyclerView.ViewHolder>(EventComparator) {

    companion object {
        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_CONTENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_TITLE -> {
                TitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_event_timeline_header, parent, false
                    )
                )
            }
            else -> {
                ContentViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_event_timeline_content, parent, false
                    )
                )
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

    class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemEventTimelineHeaderBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: EventDetail) {
            binding?.model = item
        }
    }

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemEventTimelineContentBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

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