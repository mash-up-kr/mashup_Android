package com.mashup.ui.mypage.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemMypageAttendanceHistoryListBinding
import com.mashup.ui.model.AttendanceModel

class MyPageListItemViewHolder(
    private val binding: ItemMypageAttendanceHistoryListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
