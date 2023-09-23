package com.mashup.ui.mypage.viewholder

import com.mashup.databinding.ItemMypageAttendanceHistoryLevelBinding
import com.mashup.ui.model.AttendanceModel

class MyPageListLevelViewHolder(
    private val binding: ItemMypageAttendanceHistoryLevelBinding
) : MyPageBaseViewHolder(binding) {

    override fun bind(item: AttendanceModel) {
        binding.model = item as AttendanceModel.HistoryLevel
    }
}
