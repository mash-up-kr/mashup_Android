package com.mashup.ui.mypage

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.model.ScoreDetails
import com.mashup.data.repository.MyPageRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.datastore.model.UserPreference
import com.mashup.feature.mypage.profile.data.MyProfileRepository
import com.mashup.feature.mypage.profile.data.dto.MemberGenerationsResponse
import com.mashup.feature.mypage.profile.data.dto.MemberProfileResponse
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.model.ActivityHistory
import com.mashup.ui.model.AttendanceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val myPageRepository: MyPageRepository,
    private val profileRepository: MyProfileRepository
) : BaseViewModel() {

    private var userData: UserPreference? = null
    private val userDataFlow = MutableSharedFlow<UserPreference>(replay = 1)
    private val profileDataFlow = MutableSharedFlow<ProfileData>(replay = 1)
    private val profileCardDataFlow = MutableSharedFlow<List<ProfileCardData>>(replay = 1)
    private val scoreHistoryDataFlow = MutableSharedFlow<Pair<Double, List<ActivityHistory>>>(replay = 1)

    val myPageData: SharedFlow<List<AttendanceModel>> = combine(
        userDataFlow,
        profileDataFlow,
        profileCardDataFlow,
        scoreHistoryDataFlow
    ) { userData, profileData, profileCardData, scoreHistoryData ->
        combineMyPageData(userData, profileData, profileCardData, scoreHistoryData)
    }.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000)
    )

    init {
        getMyPageData()
    }

    fun getMyPageData() {
        getUserAndScoreData()
        getProfileData()
        getProfileCardData()
    }

    private fun getUserAndScoreData() = mashUpScope {
        userPreferenceRepository.getUserPreference().first().let { userPreference ->
            userData = userPreference
            userData?.let { userDataFlow.emit(it) }

            getScoreHistoryData(userPreference.generationNumbers.last())
        }
    }

    private fun getProfileData() = mashUpScope {
        val response: MemberProfileResponse = profileRepository.getMyProfile().data ?: return@mashUpScope
        profileDataFlow.emit(mapToProfileData(response))
    }

    private fun getProfileCardData() = mashUpScope {
        val response = profileRepository.getMemberGenerations().data ?: return@mashUpScope
        userData?.let { user ->
            val profileCardData = response.memberGenerations.map {
                mapToProfileCardData(
                    user.name,
                    user.generationNumbers.last(),
                    it
                )
            }

            profileCardDataFlow.emit(profileCardData)
        }
    }

    private fun getScoreHistoryData(currentGenerationNum: Int) = mashUpScope {
        val response =
            myPageRepository.getScoreHistory().data?.scoreHistoryResponseList ?: return@mashUpScope
        val historyList = mutableListOf<ActivityHistory>()

        // 현재 기수만 사용(필터링)
        val filteredList = response.filter { it.generationNumber == currentGenerationNum }
        val totalScore = filteredList.first().totalScore
        filteredList.forEach {
            it.scoreDetails.forEach { score ->
                historyList.add(mapToActivityHistory(score))
            }
        }

        scoreHistoryDataFlow.emit(Pair(totalScore, historyList.toList()))
    }

    private fun combineMyPageData(
        userData: UserPreference,
        profileData: ProfileData,
        generationData: List<ProfileCardData>,
        scoreHistoryData: Pair<Double, List<ActivityHistory>>
    ): List<AttendanceModel> {
        val attendanceModelList = mutableListOf<AttendanceModel>()

        attendanceModelList.add(AttendanceModel.Title(0, userData.name))
        attendanceModelList.add(AttendanceModel.MyProfile(1, profileData))
        attendanceModelList.add(AttendanceModel.Score(2, scoreHistoryData.first))

        // 활동 카드
        attendanceModelList.add(AttendanceModel.ProfileCard(3, generationData))

        // 활동 히스토리
        attendanceModelList.add(AttendanceModel.HistoryLevel(4, userData.generationNumbers.last()))
        if (scoreHistoryData.second.isEmpty()) {
            attendanceModelList.add(AttendanceModel.None(5))
        } else {
            scoreHistoryData.second.forEach { activityHistory ->
                attendanceModelList.add(
                    AttendanceModel.HistoryItem(
                        attendanceModelList.size,
                        activityHistory
                    )
                )
            }
        }

        return attendanceModelList
    }

    private fun mapToProfileData(response: MemberProfileResponse) = ProfileData(
        birthDay = response.birthDate.orEmpty(),
        work = response.job.orEmpty(),
        company = response.company.orEmpty(),
        introduceMySelf = response.introduction.orEmpty(),
        location = response.residence.orEmpty(),
        instagram = response.socialNetworkServiceLink.orEmpty(),
        github = response.githubLink.orEmpty(),
        behance = response.portfolioLink.orEmpty(),
        linkedIn = response.linkedInLink.orEmpty(),
        tistory = response.blogLink.orEmpty()
    )

    private fun mapToProfileCardData(
        name: String,
        currentGenerationNum: Int,
        response: MemberGenerationsResponse.MemberGeneration
    ) = ProfileCardData(
        id = response.id,
        name = name,
        isRunning = response.number == currentGenerationNum,
        generationNumber = response.number,
        platform = response.platform,
        projectTeamName = response.projectTeamName.orEmpty(),
        role = response.role.orEmpty()
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
