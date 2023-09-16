package com.mashup.ui.mypage.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemMypageAttendanceTitleBinding
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.AttendanceListAdapter

class MyPageTitleViewHolder(
    private val binding: ItemMypageAttendanceTitleBinding,
    val listener: AttendanceListAdapter.OnItemEventListener?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnSetting.setOnClickListener {
            listener?.onStartSettingActivity()
        }
    }

    fun bind(item: AttendanceModel) {
        binding.model = item
    }
}
