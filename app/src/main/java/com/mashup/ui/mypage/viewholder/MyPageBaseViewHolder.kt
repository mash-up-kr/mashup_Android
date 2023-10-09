package com.mashup.ui.mypage.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mashup.ui.model.AttendanceModel

abstract class MyPageBaseViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: AttendanceModel)
}
