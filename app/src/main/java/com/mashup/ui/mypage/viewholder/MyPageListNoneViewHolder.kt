package com.mashup.ui.mypage.viewholder

import com.mashup.databinding.ItemMypageAttendanceHistoryPlaceholderEmpthyBinding
import com.mashup.ui.model.AttendanceModel

class MyPageListNoneViewHolder(
    private val binding: ItemMypageAttendanceHistoryPlaceholderEmpthyBinding
) : MyPageBaseViewHolder(binding) {

    override fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
