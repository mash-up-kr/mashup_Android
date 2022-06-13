package com.mashup.ui.signup

import com.mashup.base.BaseViewModel
import com.mashup.ui.model.Validation
import com.mashup.ui.signup.state.AuthState
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
    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")
    private val pwdCheck = MutableStateFlow("")

    val authState = combine(id, pwd, pwdCheck) { id, pwd, pwdCheck ->
        AuthState(
            id = id,
            pwd = pwd,
            pwdCheck = pwdCheck
        )
    }.map { authState ->
        val validationId = validationId(authState.id)
        val validationPwd = validationPwd(authState.pwd)
        val validationPwdCheck = validationPwdCheck(authState.pwd, authState.pwdCheck)

        authState.copy(
            validationId = validationId,
            validationPwd = validationPwd,
            validationPwdCheck = validationPwdCheck,
            isValidationState = validationId == Validation.SUCCESS
                && validationPwd == Validation.SUCCESS
                && validationPwdCheck == Validation.SUCCESS
        )
    }

    private val _platform = MutableStateFlow("")
    val platform: StateFlow<String> = _platform

    private val _isCheckedTerm = MutableStateFlow(false)
    val isCheckedTerm: StateFlow<Boolean> = _isCheckedTerm

    private val userName = MutableStateFlow("")
    val memberState = userName.combine(platform) { name, platform ->
        MemberState(
            name = name,
            platform = platform
        )
    }.map { memberState ->
        memberState.copy(
            isValidationState = validationName(memberState.name) == Validation.SUCCESS
                && validationPlatform(memberState.platform) == Validation.SUCCESS
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

    fun setId(id: String) {
        this.id.value = id
    }

    fun setPwd(pwd: String) {
        this.pwd.value = pwd
    }

    fun setPwdCheck(pwdCheck: String) {
        this.pwdCheck.value = pwdCheck
    }

    fun setPlatform(platform: String) {
        _platform.value = platform
    }

    fun setUserName(userName: String) {
        this.userName.value = userName
    }

    fun setCode(code: String) {
        signUpCode.value = code
    }

    fun updatedTerm() {
        _isCheckedTerm.value = !isCheckedTerm.value
    }
}