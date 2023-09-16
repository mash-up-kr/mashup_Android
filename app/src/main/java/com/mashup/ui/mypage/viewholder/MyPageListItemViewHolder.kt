package com.mashup.ui.mypage.viewholder

import com.mashup.databinding.ItemMypageAttendanceHistoryListBinding
import com.mashup.ui.model.AttendanceModel

class MyPageListItemViewHolder(
    private val binding: ItemMypageAttendanceHistoryListBinding
) : MyPageBaseViewHolder(binding) {

    override fun bind(item: AttendanceModel) {
        binding.model = item as AttendanceModel.HistoryItem
    }
}
