package com.mashup.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MyPageRepository
import com.mashup.ui.model.AttendanceModel
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
            AttendanceModel(0,0),
            AttendanceModel(0,1),
            AttendanceModel(0,4),
            AttendanceModel(0,3),
            AttendanceModel(0,3),
            AttendanceModel(0,3),
            AttendanceModel(0,3),
            AttendanceModel(0,3),
            AttendanceModel(0,4),
            AttendanceModel(0,3),
            AttendanceModel(0,3),
            AttendanceModel(0,3),
            AttendanceModel(0,3)
        )
        _attendanceList.value = test
    }
}
