package com.mashup.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_LOGIN_TYPE
import com.mashup.constant.EXTRA_MAIN_TAB
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.Platform
import com.mashup.data.repository.MemberRepository
import com.mashup.data.repository.PopUpRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.login.LoginType
import com.mashup.ui.main.model.MainPopupType
import com.mashup.ui.main.model.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val popUpRepository: PopUpRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val _isShowCongratsAttendanceScreen = mutableStateOf(false)
    val isShowCongratsAttendanceScreen: State<Boolean>
        get() = _isShowCongratsAttendanceScreen

    private val _onAttendance = MutableSharedFlow<Unit>()
    val onAttendance: SharedFlow<Unit> = _onAttendance

    private val _showPopupType = MutableSharedFlow<MainPopupType>()
    val showPopupType: SharedFlow<MainPopupType> = _showPopupType.asSharedFlow()

    private val _onClickPopupConfirm = MutableSharedFlow<MainPopupType>()
    val onClickPopupConfirm: SharedFlow<MainPopupType> = _onClickPopupConfirm.asSharedFlow()


    init {
        savedStateHandle.get<LoginType>(EXTRA_LOGIN_TYPE)?.run {
            handleLoginType(this)
        }

        savedStateHandle.get<MainTab>(EXTRA_MAIN_TAB)?.run {
            setMainTab(this)
        }

        getMainPopup()
    }

    private val _mainTab = MutableStateFlow(MainTab.EVENT)
    val mainTab: StateFlow<MainTab> = _mainTab

    fun confirmAttendance() = mashUpScope {
        _onAttendance.emit(Unit)
    }

    fun successAttendance() = mashUpScope {
        _isShowCongratsAttendanceScreen.value = true
        delay(2000L)
        _isShowCongratsAttendanceScreen.value = false
    }

    fun setMainTab(mainTab: MainTab) = mashUpScope {
        _mainTab.emit(mainTab)
    }

    private fun refreshToken() = mashUpScope {
        val result = memberRepository.refreshToken()
        if (result.isSuccess() && result.data != null) {
            userPreferenceRepository.updateUserToken(result.data.token)
        }
    }

    private fun handleLoginType(loginType: LoginType) {
        when (loginType) {
            LoginType.AUTO -> {
                refreshToken()
                refreshUserData()
            }
            else -> {
            }
        }
    }

    private fun refreshUserData() = mashUpScope {
        val result = memberRepository.getMember()
        if (result.isSuccess() && result.data != null) {
            userPreferenceRepository.updateUserPreference(
                name = result.data.name,
                platform = Platform.getPlatform(result.data.platform),
                generationNumbers = result.data.generationNumbers,
                pushNotificationAgreed = result.data.pushNotificationAgreed
            )
        }
    }

    private fun getMainPopup() = mashUpScope {
        val result = popUpRepository.getPopupKeyList()
        if (result.isSuccess()) {
            val popupType =
                MainPopupType.getMainPopupType(result.data?.firstOrNull() ?: return@mashUpScope)
            if (popupType == MainPopupType.UNKNOWN) return@mashUpScope
            _showPopupType.emit(popupType)
        }
    }

    fun disablePopup(popupKey: MainPopupType) = mashUpScope {
        if (popupKey == MainPopupType.UNKNOWN) return@mashUpScope
        popUpRepository.patchPopupDisabled(popupKey.name)
    }

    fun onClickPopup(popupKey: String) = mashUpScope {
        val popupType =
            MainPopupType.getMainPopupType(popupKey)
        if (popupType == MainPopupType.UNKNOWN) return@mashUpScope
        _onClickPopupConfirm.emit(popupType)
    }

    override fun handleErrorCode(code: String) {
    }
}
