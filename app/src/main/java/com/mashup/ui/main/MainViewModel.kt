package com.mashup.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.base.BaseViewModel
import com.mashup.constant.EXTRA_LOGIN_TYPE
import com.mashup.data.repository.MemberRepository
import com.mashup.data.repository.UserRepository
import com.mashup.ui.login.LoginType
import com.mashup.ui.main.model.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val _isShowCongratsAttendanceScreen = mutableStateOf(false)
    val isShowCongratsAttendanceScreen: State<Boolean>
        get() = _isShowCongratsAttendanceScreen

    init {
        savedStateHandle.get<LoginType>(EXTRA_LOGIN_TYPE)?.run {
            handleLoginType(this)
        }
    }

    private val _mainTab = MutableStateFlow(MainTab.EVENT)
    val mainTab: StateFlow<MainTab> = _mainTab

    fun successAttendance() = mashUpScope {
        _isShowCongratsAttendanceScreen.value = true
        delay(1000L)
        _isShowCongratsAttendanceScreen.value = false
    }

    fun setMainTab(mainTab: MainTab) = mashUpScope {
        _mainTab.emit(mainTab)
    }

    private fun refreshToken() = mashUpScope {
        val result = memberRepository.refreshToken()
        if (result.isSuccess()) {
            userRepository.setUserToken(result.data?.token)
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
        val userInfo = memberRepository.getMember()
        if (userInfo.isSuccess()) {
        }
    }

    override fun handleErrorCode(code: String) {
    }
}
