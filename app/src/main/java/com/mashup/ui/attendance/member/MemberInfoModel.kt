package com.mashup.ui.attendance.member

import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData

data class MemberInfoModel(
    val score: Double,
    val generationList: List<ProfileCardData>,
    val profile: ProfileData
)
