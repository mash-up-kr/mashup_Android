package com.mashup.ui.setting

import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.repository.MemberRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val memberRepository: MemberRepository
) : BaseViewModel() {
    val userPreference = userPreferenceRepository.getUserPreference()

    private val _onSuccessLogout = MutableSharedFlow<Unit>()
    val onSuccessLogout: SharedFlow<Unit> = _onSuccessLogout

    fun requestLogout() = mashUpScope {
        userPreferenceRepository.clearUserPreference()
        _onSuccessLogout.emit(Unit)
    }

    fun patchPushNotification(
        pushNotificationAgreed: Boolean,
        danggnPushNotificationAgreed: Boolean
    ) = mashUpScope {
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
