package com.mashup.ui.mypage.viewholder

import com.mashup.databinding.ItemMypageAttendanceScoreBinding
import com.mashup.ui.model.AttendanceModel

class MyPageScoreViewHolder(
    private val binding: ItemMypageAttendanceScoreBinding,
) : MyPageBaseViewHolder(binding) {

    override fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
