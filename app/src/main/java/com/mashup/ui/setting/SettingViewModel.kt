package com.mashup.ui.setting

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.data.local.UserPreference
import com.mashup.data.repository.MemberRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val memberRepository: MemberRepository
) : BaseViewModel() {
    val userPreference = userPreferenceRepository.getUserPreference().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UserPreference.getDefaultInstance()
    )

    private val _onSuccessLogout = MutableSharedFlow<Unit>()
    val onSuccessLogout: SharedFlow<Unit> = _onSuccessLogout

    fun requestLogout() = mashUpScope {
        userPreferenceRepository.clearUserPreference()
        _onSuccessLogout.emit(Unit)
    }

    fun patchPushNotification(
        pushNotificationAgreed: Boolean
    ) = mashUpScope {
        val danggnPushAgreed = userPreference.value.danggnPushNotificationAgreed
        val result = memberRepository.patchPushNotification(
            pushNotificationAgreed = pushNotificationAgreed,
            danggnPushNotificationAgreed = danggnPushAgreed
        )
        if (result.isSuccess()) {
            userPreferenceRepository.updateUserPushNotificationAgreed(
                pushNotificationAgreed = pushNotificationAgreed,
                danggnPushNotificationAgreed = danggnPushAgreed
            )
        }
    }

    fun patchDanggnPushNotification(
        danggnPushNotificationAgreed: Boolean
    ) = mashUpScope {
        val pushNotificationAgreed = userPreference.value.pushNotificationAgreed
        val result = memberRepository.patchPushNotification(
            pushNotificationAgreed = pushNotificationAgreed,
            danggnPushNotificationAgreed = danggnPushNotificationAgreed
        )
        if (result.isSuccess()) {
            userPreferenceRepository.updateUserPushNotificationAgreed(
                pushNotificationAgreed = pushNotificationAgreed,
                danggnPushNotificationAgreed = danggnPushNotificationAgreed
            )
        }
    }

    override fun handleErrorCode(code: String) {
    }
}
