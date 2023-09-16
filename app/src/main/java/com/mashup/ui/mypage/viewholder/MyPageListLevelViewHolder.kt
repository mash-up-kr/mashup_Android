package com.mashup.ui.mypage.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemMypageAttendanceHistoryLevelBinding
import com.mashup.ui.model.AttendanceModel

class MyPageListLevelViewHolder(
    private val binding: ItemMypageAttendanceHistoryLevelBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
