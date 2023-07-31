package com.mashup.ui.signup

import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.model.Validation
import com.mashup.core.firebase.FirebaseRepository
import com.mashup.core.model.Platform
import com.mashup.data.repository.MemberRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.signup.state.CodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val firebaseRepository: FirebaseRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {
    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")
    private val pwdCheck = MutableStateFlow("")

    private val _platform = MutableStateFlow(Platform.UNKNOWN)
    val platform: StateFlow<Platform> = _platform

    private val _isCheckedTerm = MutableStateFlow(false)
    val isCheckedTerm: StateFlow<Boolean> = _isCheckedTerm

    private val _showToolbarDivider = MutableSharedFlow<Boolean>()
    val showToolbarDivider: SharedFlow<Boolean> = _showToolbarDivider

    private val _showToolbarClose = MutableStateFlow(false)
    val showToolbarClose: SharedFlow<Boolean> = _showToolbarClose

    private val userName = MutableStateFlow("")
    val memberState = userName.combine(platform) { name, platform ->
        val validationName = validationName(name)
        MemberState(
            name = name,
            platform = platform.detailName,
            isValidationName = validationName,
            isValidationState = validationName == Validation.SUCCESS &&
                validationPlatform(platform.detailName) == Validation.SUCCESS
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
            fcmToken = firebaseRepository.getFcmToken(),
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

        response.data?.run {
            userPreferenceRepository.updateUserPreference(
                id = memberId,
                token = token,
                name = name,
                platform = Platform.getPlatform(platform),
                generationNumbers = generationNumbers,
                pushNotificationAgreed = pushNotificationAgreed
            )
        }
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

    fun setToolbarCloseVisible(isVisible: Boolean) {
        mashUpScope {
            _showToolbarClose.emit(isVisible)
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _signUpState.emit(SignUpState.Error(code))
        }
    }

    fun isDataEmpty(): Boolean {
        return id.value.isEmpty() &&
            pwd.value.isEmpty() &&
            pwdCheck.value.isEmpty() &&
            platform.value == Platform.UNKNOWN &&
            signUpCode.value.isEmpty()
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
