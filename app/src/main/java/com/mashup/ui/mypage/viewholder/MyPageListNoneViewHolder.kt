package com.mashup.ui.mypage.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemMypageAttendanceHistoryPlaceholderEmpthyBinding
import com.mashup.ui.model.AttendanceModel

class MyPageListNoneViewHolder(
    private val binding: ItemMypageAttendanceHistoryPlaceholderEmpthyBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
