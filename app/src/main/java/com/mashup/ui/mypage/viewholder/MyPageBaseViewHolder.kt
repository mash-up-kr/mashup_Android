package com.mashup.ui.mypage.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mashup.ui.model.AttendanceModel

abstract class MyPageBaseViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: AttendanceModel)
}
