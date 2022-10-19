package com.mashup.core.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MemberRepository
import com.mashup.core.main.model.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    private val _isShowCongratsAttendanceScreen = mutableStateOf(false)
    val isShowCongratsAttendanceScreen: State<Boolean>
        get() = _isShowCongratsAttendanceScreen

    init {
        refreshToken()
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
            userDataSource.token = result.data?.token
        }
    }

    override fun handleErrorCode(code: String) {
    }
}
