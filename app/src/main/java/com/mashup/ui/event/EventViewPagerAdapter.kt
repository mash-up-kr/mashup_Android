package com.mashup.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.databinding.ItemEventBinding
import com.mashup.ui.event.model.Event

class EventViewPagerAdapter(idolList: ArrayList<Event>) :
    RecyclerView.Adapter<EventViewPagerAdapter.PagerViewHolder>() {
    var item = idolList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(parent, mListener)

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
                it.tvDDay.text = (data.getDDay())
                it.tvCalender.text = (data.getDate())
                it.tvTimeLine.text = (data.getTimeLine())
                it.tvName.apply {
                    text = String.format(
                        context.resources.getString(R.string.event_list_card_title),
                        "홍길동"
                    )
                }
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