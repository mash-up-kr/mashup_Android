package com.mashup.ui.signup

import com.mashup.base.BaseViewModel
import com.mashup.ui.signup.state.MemberState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : BaseViewModel() {

    private val userName = MutableStateFlow("")
    private val platform = MutableStateFlow("")

    val memberState = userName.combine(platform) { name, platform ->
        MemberState(
            name = name,
            platform = platform
        )
    }.map { memberState ->
        memberState.copy(
            isValidationState = memberState.name.isNotBlank()
                && memberState.platform.isNotBlank()
        )
    }


    fun setPlatform(platform: String) {
        this.platform.value = platform
    }

    fun setUserName(userName: String) {
        this.userName.value = userName
    }
}