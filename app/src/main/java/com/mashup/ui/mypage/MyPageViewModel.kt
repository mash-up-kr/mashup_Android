package com.mashup.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mashup.base.BaseViewModel
import com.mashup.data.repository.MemberRepository
import com.mashup.data.repository.MyPageRepository
import com.mashup.ui.model.ActivityHistory
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.model.Platform
import com.mashup.ui.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val myPageRepository: MyPageRepository
) : BaseViewModel() {

    private val _attendanceList = MutableLiveData<List<AttendanceModel>>()
    val attendanceList: LiveData<List<AttendanceModel>> = _attendanceList

    private val _errorCode = MutableSharedFlow<String>()
    val errorCode: SharedFlow<String> = _errorCode

    init {
        getMember()
    }

    private fun getMember() = mashUpScope {
        try {

            val response = memberRepository.getMember()

            if (response.isSuccess()) {
                getScore(
                    Profile(
                        platform = Platform.getPlatform(response.data?.platform),
                        name = response.data?.name.toString(),
                        score = 0.0
                    )
                )
            } else {
                handleErrorCode(response.code)
            }
        } catch (ignore: Exception) {
        }
    }

    private fun getScore(profile: Profile) = mashUpScope {
        try {
            val response = myPageRepository.getScoreHistory()
            if (response.isSuccess()) {
                val list = mutableListOf<AttendanceModel>()
                if (response.data?.scoreHistoryResponseList?.isNotEmpty() == true) {
                    list += AttendanceModel(
                        0,
                        MyPageAdapterType.TITLE,
                        profile.copy(
                            score = response.data.scoreHistoryResponseList.first().totalScore
                        ), null, null
                    )

                    list += AttendanceModel(
                        0, MyPageAdapterType.SCORE,
                        profile.copy(
                            score = response.data.scoreHistoryResponseList.first().totalScore
                        ), null, null
                    )
                    response.data.scoreHistoryResponseList.forEach {
                        list += AttendanceModel(
                            0, MyPageAdapterType.LIST_LEVEL, null, it.generationNumber, null
                        )
                        list += it.scoreDetails.map { score ->
                            AttendanceModel(
                                id = 0,
                                MyPageAdapterType.LIST_ITEM,
                                null,
                                it.generationNumber,
                                ActivityHistory(
                                    scoreName = score.scoreName,
                                    attendanceType = AttendanceType.getAttendanceType(score.scoreType),
                                    cumulativeScore = score.cumulativeScore,
                                    totalScore = score.score,
                                    detail = score.scheduleName,
                                    date = score.date
                                )
                            )
                        }
                    }
                } else {
                    list.addAll(
                        listOf(
                            AttendanceModel(
                                0, MyPageAdapterType.TITLE, profile, null, null
                            ),
                            AttendanceModel(
                                0, MyPageAdapterType.SCORE, profile, null, null
                            ),
                            AttendanceModel(
                                0, MyPageAdapterType.LIST_NONE, profile, null, null
                            )
                        )
                    )

                }
                _attendanceList.postValue(list)
            } else {
                handleErrorCode(response.code)
            }
        } catch (ignore: Exception) {
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _errorCode.emit(code)
        }
    }
}
