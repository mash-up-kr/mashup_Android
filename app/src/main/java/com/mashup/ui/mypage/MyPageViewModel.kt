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
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val myPageRepository: MyPageRepository
) : BaseViewModel() {

    private val _attendanceList = MutableLiveData<List<AttendanceModel>>()
    val attendanceList: LiveData<List<AttendanceModel>> = _attendanceList

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
                        profile.also {
                            it.score = response.data.scoreHistoryResponseList.first().totalScore
                        }, null, null
                    )

                    list += AttendanceModel(
                        0, MyPageAdapterType.SCORE,
                        profile.also {
                            it.score = response.data.scoreHistoryResponseList.first().totalScore
                        }, null, null
                    )
                    response.data.scoreHistoryResponseList.forEach { it ->
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
                                    attendanceType = AttendanceType.getAttendanceType(score.scoreType),
                                    totalScore = score.cumulativeScore,
                                    detail = score.scoreName,
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

            }
        } catch (ignore: Exception) {
        }
    }

    override fun handleErrorCode(code: String) {
    }
}
