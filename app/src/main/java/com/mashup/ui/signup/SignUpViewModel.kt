package com.mashup.ui.signup

import com.mashup.base.BaseViewModel
import com.mashup.ui.signup.state.CodeState
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
            isValidationState = validationName(memberState.name)
                && validationPlatform(memberState.platform)
        )
    }

    private val signUpCode = MutableStateFlow("")
    val codeState = signUpCode
        .map {
            CodeState(
                code = it,
                isValidationState = true // TODO: validation to api
            )
        }


    fun setPlatform(platform: String) {
        this.platform.value = platform
    }

    fun setUserName(userName: String) {
        this.userName.value = userName
    }

    fun setCode(code: String) {
        signUpCode.value = code
    }
}