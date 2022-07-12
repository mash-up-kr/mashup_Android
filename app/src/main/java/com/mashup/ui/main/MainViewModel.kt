package com.mashup.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mashup.base.BaseViewModel
import com.mashup.ui.main.model.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    private val _isShowCongratsAttendanceScreen = mutableStateOf(false)
    val isShowCongratsAttendanceScreen: State<Boolean>
        get() = _isShowCongratsAttendanceScreen

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
}