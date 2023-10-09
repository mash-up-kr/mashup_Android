package com.mashup.ui.mypage.viewholder

import com.mashup.databinding.ItemMypageAttendanceTitleBinding
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.AttendanceListAdapter

class MyPageTitleViewHolder(
    private val binding: ItemMypageAttendanceTitleBinding,
    val listener: AttendanceListAdapter.OnItemEventListener?
) : MyPageBaseViewHolder(binding.root) {

    init {
        binding.btnSetting.setOnClickListener {
            listener?.onStartSettingActivity()
        }
    }

    override fun bind(item: AttendanceModel) {
        binding.model = item as AttendanceModel.Title
    }
}
