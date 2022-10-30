package com.mashup.ui.schedule

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScheduleLayoutManager(context: Context) : LinearLayoutManager(context) {

    private var initPosition: Int? = null

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        initPosition?.run {
            scrollToPosition(this)
            initPosition = null
        }
    }

    fun setInitPosition(position: Int) {
        initPosition = position
    }
}
