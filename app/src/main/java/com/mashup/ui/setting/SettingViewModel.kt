package com.mashup.ui.setting

import com.mashup.base.BaseViewModel
import com.mashup.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _onSuccessLogout = MutableSharedFlow<Unit>()
    val onSuccessLogout: SharedFlow<Unit> = _onSuccessLogout

    fun requestLogout() = mashUpScope {
        userRepository.clearUserData()
        _onSuccessLogout.emit(Unit)
    }

    override fun handleErrorCode(code: String) {
    }
}