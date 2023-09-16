package com.mashup.ui.mypage.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemMypageAttendanceScoreBinding
import com.mashup.ui.model.AttendanceModel

class MyPageScoreViewHolder(
    private val binding: ItemMypageAttendanceScoreBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
