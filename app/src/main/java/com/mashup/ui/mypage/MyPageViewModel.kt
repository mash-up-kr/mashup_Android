package com.mashup.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MyPageRepository
import com.mashup.ui.model.ActivityHistory
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val userDataSource: UserDataSource
) : BaseViewModel() {

    private val _attendanceList = MutableLiveData<List<AttendanceModel>>()
    val attendanceList: LiveData<List<AttendanceModel>> = _attendanceList


    init {
        val test = listOf<AttendanceModel>(
            AttendanceModel(0,0, Profile.EMPTY,12,null),
            AttendanceModel(0,1, Profile.EMPTY,12,null),
            AttendanceModel(0,4, Profile.EMPTY,12,null),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.PLACEHOLDER_HISTORY, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.PRESENTATION, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.PROJECT_FAIL, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.PROJECT_SUCCESS, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.ATTENDANCE, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.ABSENT, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.LATE, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.HACKATHON_PREPARE, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.PROJECT_LEADER, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.PROJECT_SUB_LEADER, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,4, Profile.EMPTY,13,null),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.STAFF, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.MASHUP_LEADER, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.MASHUP_SUB_LEADER, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.TECH_BLOG_WRITE, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
            AttendanceModel(0,3, Profile.EMPTY,12, ActivityHistory(attendanceType = AttendanceType.MASHUP_CONTENTS_WRITE, totalScore = 103, detail = "3차 전체 세미나", date = "2022.02.03",)),
        )
        _attendanceList.value = test
    }
}
