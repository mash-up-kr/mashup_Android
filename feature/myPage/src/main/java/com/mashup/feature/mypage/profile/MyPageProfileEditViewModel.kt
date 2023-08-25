package com.mashup.feature.mypage.profile

import androidx.lifecycle.ViewModel
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.Platform
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.mypage.profile.data.MyProfileRepository
import com.mashup.feature.mypage.profile.edit.EditedProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MyPageProfileEditViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val myProfileRepository: MyProfileRepository,
) : BaseViewModel() {

    init {
        getMyProfileCard()
    }

    private val _myProfileCard = MutableStateFlow(MyProfileCardEntity())
    val myProfileCard = _myProfileCard.asStateFlow()

    private val _myPageCardEntity = MutableStateFlow(EditedProfile())
    val myPageCardEntity = _myPageCardEntity.asStateFlow()

    private val _loadState: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.Initial)
    val loadState = _loadState.asStateFlow()

    fun getMyProfileCard() = mashUpScope {
        val myProfile = myProfileRepository.getMyProfile().data
        _myProfileCard.value = MyProfileCardEntity(
            generationNumber = userPreferenceRepository.getCurrentGenerationNumber(),
            platform = userPreferenceRepository.getUserPreference().first().platform,
            team = myProfile?.company.orEmpty(),  // 여기 두개는 후에 바꾸셈~ 아직 api가 안나온듯합니다 ?
            staff = myProfile?.company.orEmpty()
        )
    }

    fun postMyProfileEntity(
        editedProfileData: EditedProfile
    ) = mashUpScope {
        _loadState.emit(LoadState.Loading)
        myProfileRepository.postMyProfile(editedProfileData)
        _loadState.emit(LoadState.Loaded)
    }

    fun getMemberGenerationList() = mashUpScope {
        myProfileRepository.getMemberGenerations()
    }

    fun postMemberGenerations(id: Long, projectTeamName: String, staff: String) = mashUpScope {
        myProfileRepository.postMemberGenerations(id, projectTeamName, staff)
    }

}

sealed class LoadState {
    object Initial : LoadState()
    object Loading : LoadState()
    object Loaded : LoadState()
}

data class MyProfileCardEntity(
    val generationNumber: Int = 0,
    val platform: Platform = Platform.UNKNOWN,
    val team: String = "",
    val staff: String = "",
)
