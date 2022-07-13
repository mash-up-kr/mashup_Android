package com.mashup.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.ui.model.Event

class EventViewPagerAdapter(idolList: ArrayList<Event>) :
    RecyclerView.Adapter<EventViewPagerAdapter.PagerViewHolder>() {
    var item = idolList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val viewHolder: PagerViewHolder = holder
        viewHolder.onBind(item[position])
    }

    class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (
        LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
    ) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val btnAttendance: TextView = itemView.findViewById(R.id.btn_attendance_list)

        var data: Event? = null
        fun onBind(data: Event) {
            this.data = data
            title.text = (data.title)
            btnAttendance.setOnClickListener {
                EventDetailActivity.start(it.context)
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