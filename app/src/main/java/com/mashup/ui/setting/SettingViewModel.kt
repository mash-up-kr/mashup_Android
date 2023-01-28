package com.mashup.ui.setting

import com.mashup.base.BaseViewModel
import com.mashup.datastore.data.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {
    private val _onSuccessLogout = MutableSharedFlow<Unit>()
    val onSuccessLogout: SharedFlow<Unit> = _onSuccessLogout

    fun requestLogout() = mashUpScope {
        userPreferenceRepository.clearUserPreference()
        _onSuccessLogout.emit(Unit)
    }

    override fun handleErrorCode(code: String) {
    }
}