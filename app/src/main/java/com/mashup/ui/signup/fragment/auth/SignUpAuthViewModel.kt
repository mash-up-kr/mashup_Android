package com.mashup.ui.signup.fragment.auth

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.constant.INVALID_MEMBER_ID
import com.mashup.core.common.constant.MEMBER_DUPLICATED_IDENTIFICATION
import com.mashup.core.common.model.Validation
import com.mashup.data.repository.MemberRepository
import com.mashup.ui.signup.validationId
import com.mashup.ui.signup.validationPwd
import com.mashup.ui.signup.validationPwdCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpAuthViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : BaseViewModel() {

    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")
    private val pwdCheck = MutableStateFlow("")

    private val idState = MutableStateFlow<SignUpIdState>(SignUpIdState.Empty)

    private val pwdState = pwd.map {
        when (validationPwd(it)) {
            Validation.SUCCESS -> {
                SignUpPwdState.Success
            }

            Validation.FAILED -> {
                SignUpPwdState.Error
            }

            else -> {
                SignUpPwdState.Empty
            }
        }
    }

    private val pwdCheckState = pwd.combine(pwdCheck) { pwd, pwdCheck ->
        when (validationPwdCheck(pwd, pwdCheck)) {
            Validation.SUCCESS -> {
                SignUpPwdCheckState.Success
            }

            Validation.FAILED -> {
                SignUpPwdCheckState.Error
            }

            else -> {
                SignUpPwdCheckState.Empty
            }
        }
    }

    private val buttonState = MutableStateFlow<SignUpButtonState>(SignUpButtonState.Empty)

    val authScreenState: StateFlow<SignUpAuthScreenState> =
        combine(
            idState,
            pwdState,
            pwdCheckState,
            buttonState
        ) { idState, pwdState, pwdCheckState, buttonState ->
            val validId = (idState as? SignUpIdState.Success)?.validId == true
            val newButtonState = if (validId) {
                if (
                    pwdState == SignUpPwdState.Success &&
                    pwdCheckState == SignUpPwdCheckState.Success
                ) {
                    SignUpButtonState.Enable
                } else {
                    SignUpButtonState.Disable
                }
            } else {
                buttonState
            }

            SignUpAuthScreenState(
                idState,
                pwdState,
                pwdCheckState,
                newButtonState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SignUpAuthScreenState(
                SignUpIdState.Empty,
                SignUpPwdState.Empty,
                SignUpPwdCheckState.Empty,
                SignUpButtonState.Empty
            )
        )

    private val _moveToNextScreen = MutableSharedFlow<Unit>()
    val moveToNextScreen: SharedFlow<Unit> = _moveToNextScreen

    init {
        viewModelScope.launch {
            id.collectLatest {
                idState.emit(
                    when (validationId(it)) {
                        Validation.SUCCESS -> {
                            buttonState.emit(SignUpButtonState.Enable)
                            SignUpIdState.Success(false)
                        }

                        Validation.EMPTY -> {
                            buttonState.emit(SignUpButtonState.Disable)
                            SignUpIdState.Empty
                        }

                        else -> {
                            buttonState.emit(SignUpButtonState.Disable)
                            SignUpIdState.Error(code = INVALID_MEMBER_ID)
                        }
                    }
                )
            }
        }
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

    fun onClickNextButton() = mashUpScope {
        if (authScreenState.value.idState is SignUpIdState.Success &&
            authScreenState.value.pwdState is SignUpPwdState.Success &&
            authScreenState.value.pwdCheckState is SignUpPwdCheckState.Success
        ) {
            _moveToNextScreen.emit(Unit)
        } else {
            checkValidId()
        }
    }

    private fun checkValidId() = mashUpScope {
        buttonState.emit(SignUpButtonState.Loading)
        try {
            memberRepository.validateId(id.value)
                .onSuccess {
                    buttonState.emit(SignUpButtonState.Enable)
                    idState.emit(SignUpIdState.Success(validId = true))
                }.onFailure {
                    buttonState.emit(SignUpButtonState.Disable)
                    idState.emit(SignUpIdState.Error(code = MEMBER_DUPLICATED_IDENTIFICATION))
                }
        } catch (ignore: Exception) {
            buttonState.emit(SignUpButtonState.Disable)
        }
    }

    override fun handleErrorCode(code: String) {
    }
}

sealed interface SignUpIdState {
    object Empty : SignUpIdState
    data class Success(val validId: Boolean) : SignUpIdState
    data class Error(val code: String) : SignUpIdState
}

sealed interface SignUpPwdState {
    object Empty : SignUpPwdState
    object Success : SignUpPwdState
    object Error : SignUpPwdState
}

sealed interface SignUpPwdCheckState {
    object Empty : SignUpPwdCheckState
    object Success : SignUpPwdCheckState
    object Error : SignUpPwdCheckState
}

sealed interface SignUpButtonState {
    object Empty : SignUpButtonState
    object Enable : SignUpButtonState
    object Disable : SignUpButtonState
    object Loading : SignUpButtonState
}

data class SignUpAuthScreenState(
    val idState: SignUpIdState,
    val pwdState: SignUpPwdState,
    val pwdCheckState: SignUpPwdCheckState,
    val buttonState: SignUpButtonState
)
