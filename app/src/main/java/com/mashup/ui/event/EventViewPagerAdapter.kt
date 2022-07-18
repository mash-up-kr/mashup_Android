package com.mashup.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.databinding.ItemEventBinding
import com.mashup.databinding.ItemEventTimelineHeaderBinding
import com.mashup.ui.model.Event

class EventViewPagerAdapter(idolList: ArrayList<Event>) :
    RecyclerView.Adapter<EventViewPagerAdapter.PagerViewHolder>() {
    var item = idolList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(parent,mListener)

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val viewHolder: PagerViewHolder = holder
        viewHolder.onBind(item[position])
    }

    class PagerViewHolder(parent: ViewGroup, val listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).root
        ) {
        private val binding: ItemEventBinding? = androidx.databinding.DataBindingUtil.bind(itemView)

        fun onBind(data: Event) {
            binding?.let {
                it.tvTitle.text = (data.title)
                it.btnAttendanceList.setOnClickListener {
                    listener?.onClickAttendanceList()
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onClickAttendanceList()
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }
}