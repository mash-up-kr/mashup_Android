package com.mashup.ui.mypage

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.model.ScoreDetails
import com.mashup.data.repository.MyPageRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.datastore.model.UserPreference
import com.mashup.feature.mypage.profile.data.MyProfileRepository
import com.mashup.feature.mypage.profile.data.dto.MemberProfileResponse
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.model.ActivityHistory
import com.mashup.ui.model.AttendanceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val myPageRepository: MyPageRepository,
    private val profileRepository: MyProfileRepository,
) : BaseViewModel() {

    private val userData = MutableStateFlow<UserPreference?>(null)
    private val profileData = MutableStateFlow<ProfileData?>(null)
    private val scoreHistoryData = MutableStateFlow<Pair<Double, List<ActivityHistory>>?>(null)

    // 각각의 Repository에서 데이터를 조회해온 후 조합해서 한 번에 화면에 보여줌
    private var _myPageData: List<AttendanceModel> = emptyList()
    val myPageData: StateFlow<List<AttendanceModel>> = combine(userData, profileData, scoreHistoryData) { userData, profileData, scoreHistoryData ->
        if (userData != null && profileData != null && scoreHistoryData != null) {
            _myPageData = combineMyPageData(userData, profileData, scoreHistoryData)
        }

        _myPageData
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        _myPageData
    )

    init {
        getMyPageData()
    }

    fun getMyPageData() {
        getUserData()
        getProfileData()
    }

    private fun getUserData() = mashUpScope {
        userPreferenceRepository.getUserPreference().first().let { userPreference ->
            userData.value = userPreference
            getScoreHistoryData(userPreference.generationNumbers.last())
        }
    }

    private fun getProfileData() = mashUpScope {
        val response: MemberProfileResponse = profileRepository.getMyProfile().data ?: return@mashUpScope
        profileData.value = mapToProfileData(response)
    }

    private fun getScoreHistoryData(currentGenerationNum: Int) = mashUpScope {
        val response = myPageRepository.getScoreHistory().data?.scoreHistoryResponseList ?: return@mashUpScope
        val historyList = mutableListOf<ActivityHistory>()

        // 현재 기수만 사용(필터링)
        val filteredList = response.filter { it.generationNumber == currentGenerationNum }
        val totalScore = filteredList.first().totalScore
        filteredList.forEach {
            it.scoreDetails.forEach { score ->
                historyList.add(mapToActivityHistory(score))
            }
        }

        scoreHistoryData.value = Pair(totalScore, historyList.toList())
    }

    private fun combineMyPageData(
        userData: UserPreference,
        profileData: ProfileData,
        scoreHistoryData: Pair<Double, List<ActivityHistory>>,
    ): List<AttendanceModel> {
        return mutableListOf<AttendanceModel>().apply {
            add(AttendanceModel.Title(0, userData.name))
            add(AttendanceModel.MyProfile(1, profileData))
            add(AttendanceModel.Score(2, scoreHistoryData.first))
            add(AttendanceModel.ActivityCard(3, emptyList())) // TODO: 기수 활동카드 가져오기

            // 활동 히스토리
            add(AttendanceModel.HistoryLevel(4, userData.generationNumbers.last()))

            val startIndex = 5
            if (scoreHistoryData.second.isEmpty()) {
                add(AttendanceModel.None(startIndex))
            } else {
                scoreHistoryData.second.forEachIndexed { index, activityHistory ->
                    add(AttendanceModel.HistoryItem(index + startIndex, activityHistory))
                }
            }
        }
    }

    private fun mapToProfileData(response: MemberProfileResponse) = ProfileData(
        birthDay = response.birthDate.orEmpty(),
        work = response.job.orEmpty(),
        company = response.company.orEmpty(),
        introduceMySelf = response.introduction.orEmpty(),
        location = response.residence.orEmpty(),
        instagram = response.blogLink.orEmpty(), // FIXME
        github = response.githubLink.orEmpty(),
        behance = response.githubLink.orEmpty(),
        linkedIn = response.linkedInLink.orEmpty(),
        tistory = response.blogLink.orEmpty(), // FIXME
    )

    private fun mapToActivityHistory(response: ScoreDetails) = ActivityHistory(
        scoreName = response.scoreName,
        attendanceType = AttendanceType.getAttendanceType(response.scoreType),
        cumulativeScore = response.cumulativeScore,
        score = response.score,
        detail = response.scheduleName,
        date = response.date
    )

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _errorCode.emit(code)
        }
    }
}
