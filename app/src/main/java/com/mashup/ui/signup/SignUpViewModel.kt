package com.mashup.ui.signup

import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MemberRepository
import com.mashup.ui.model.Platform
import com.mashup.ui.model.Validation
import com.mashup.ui.signup.state.AuthState
import com.mashup.ui.signup.state.CodeState
import com.mashup.ui.signup.state.MemberState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val dataSource: UserDataSource
) : BaseViewModel() {
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

    private val _platform = MutableStateFlow(Platform.NONE)
    val platform: StateFlow<Platform> = _platform

    private val _isCheckedTerm = MutableStateFlow(false)
    val isCheckedTerm: StateFlow<Boolean> = _isCheckedTerm

    private val userName = MutableStateFlow("")
    val memberState = userName.combine(platform) { name, platform ->
        MemberState(
            name = name,
            platform = platform.detailName
        )
    }.map { memberState ->
        memberState.copy(
            isValidationState = validationName(memberState.name) == Validation.SUCCESS
                && validationPlatform(memberState.platform) == Validation.SUCCESS
        )
    }

    private val signUpCode = MutableStateFlow("")
    val codeState = signUpCode
        .map { code ->
            CodeState(
                code = code,
                isValidationState = validationCode(code) == Validation.SUCCESS
            )
        }

    private val _signUpState = MutableSharedFlow<SignUpState>()
    val signUpState: SharedFlow<SignUpState> = _signUpState

    private fun requestSignup() = mashUpScope {
        val response = memberRepository.signup(
            identification = id.value,
            inviteCode = signUpCode.value,
            name = userName.value,
            password = pwd.value,
            platform = platform.value.name,
            privatePolicyAgreed = isCheckedTerm.value
        )

        if (!response.isSuccess()) {
            handleSignUpError(response.code, response.message)
            return@mashUpScope
        }

        dataSource.token = response.data?.token
        _signUpState.emit(SignUpState.SUCCESS)
    }

    fun requestInvalidSignUpCode() = mashUpScope {
        val response = memberRepository.validateSignUpCode(
            inviteCode = signUpCode.value,
            platform = platform.value.name
        )

        if (!response.isSuccess()) {
            _signUpState.emit(SignUpState.InvalidCode)
            return@mashUpScope
        }

        requestSignup()
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

    fun setPlatform(platform: Platform) {
        _platform.value = platform
    }

    fun setUserName(userName: String) {
        this.userName.value = userName
    }

    fun setCode(code: String) {
        signUpCode.value = code
    }

    fun updatedTerm(value: Boolean? = null) {
        _isCheckedTerm.value = value ?: !isCheckedTerm.value
    }

    private fun handleSignUpError(errorCode: String, message: String?) = mashUpScope {
        _signUpState.emit(SignUpState.Error(errorCode, message))
    }
}

sealed interface SignUpState {
    object SUCCESS : SignUpState
    object InvalidCode : SignUpState
    data class Error(val code: String, val message: String?) : SignUpState
}