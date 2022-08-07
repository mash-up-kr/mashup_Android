package com.mashup.ui.signup

import com.mashup.base.BaseViewModel
import com.mashup.common.Validation
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MemberRepository
import com.mashup.ui.model.Platform
import com.mashup.ui.signup.state.CodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")
    private val pwdCheck = MutableStateFlow("")

    private val _platform = MutableStateFlow(Platform.NONE)
    val platform: StateFlow<Platform> = _platform

    private val _isCheckedTerm = MutableStateFlow(false)
    val isCheckedTerm: StateFlow<Boolean> = _isCheckedTerm

    private val _showToolbarDivider = MutableSharedFlow<Boolean>()
    val showToolbarDivider: SharedFlow<Boolean> = _showToolbarDivider

    private val userName = MutableStateFlow("")
    val memberState = userName.combine(platform) { name, platform ->
        val validationName = validationName(name)
        MemberState(
            name = name,
            platform = platform.detailName,
            isValidationName = validationName,
            isValidationState = validationName == Validation.SUCCESS
                && validationPlatform(platform.detailName) == Validation.SUCCESS
        )
    }.debounce(250)

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
            handleErrorCode(response.code)
            return@mashUpScope
        }

        userDataSource.token = response.data?.token
        userDataSource.memberId = response.data?.memberId
        userDataSource.generateNumber = response.data?.generationNumber
        _signUpState.emit(SignUpState.Success)
    }

    fun requestInvalidSignUpCode() = mashUpScope {
        _signUpState.emit(SignUpState.Loading)
        val response = memberRepository.validateSignUpCode(
            inviteCode = signUpCode.value,
            platform = platform.value.name
        )

        if (!response.isSuccess() || response.data?.valid != true) {
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

    fun setToolbarDividerVisible(isVisible: Boolean) {
        mashUpScope {
            _showToolbarDivider.emit(isVisible)
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _signUpState.emit(SignUpState.Error(code))
        }
    }
}

sealed interface SignUpState {
    object Loading : SignUpState
    object Success : SignUpState
    object InvalidCode : SignUpState
    data class Error(val code: String) : SignUpState
}

data class MemberState(
    val name: String = "",
    val platform: String = "",
    val isValidationName: Validation,
    val isValidationState: Boolean = false
)