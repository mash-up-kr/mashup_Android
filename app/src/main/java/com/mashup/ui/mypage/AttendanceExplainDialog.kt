package com.mashup.ui.mypage

import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.databinding.BottomSheetAttendanceInfoBinding

class AttendanceExplainDialog : BaseBottomSheetDialogFragment<BottomSheetAttendanceInfoBinding>() {

    override fun initViews() {
        super.initViews()
        setTitle("매시업 활동 점수 제도")
        setVisibleCloseButton(true)
    }

    override val layoutId: Int
        get() = R.layout.bottom_sheet_attendance_info
}