package com.mashup.ui.password

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : BaseViewModel() {

    private val _id = MutableStateFlow("")
    private val _pwd = MutableStateFlow("")
    private val _pwdCheck = MutableStateFlow("")

    private val idState = MutableStateFlow<IdState>(IdState.Empty)
    private val pwdState = _pwd.map {
        when (validationPwd(it)) {
            Validation.SUCCESS -> {
                PwdState.Success
            }
            Validation.FAILED -> {
                PwdState.Error
            }
            else -> {
                PwdState.Empty
            }
        }
    }
    private val pwdCheckState = _pwd.combine(_pwdCheck) { pwd, pwdCheck ->
        when (validationPwdCheck(pwd, pwdCheck)) {
            Validation.SUCCESS -> {
                updateButtonState(button = ButtonState.Enable)
                PwdCheckState.Success
            }

            Validation.FAILED -> {
                updateButtonState(button = ButtonState.Disable)
                PwdCheckState.Error
            }

            else -> {
                updateButtonState(button = ButtonState.Disable)
                PwdCheckState.Empty
            }
        }
    }

    private val buttonState = MutableStateFlow<ButtonState>(ButtonState.Empty)

    private val _moveToNextScreen = MutableSharedFlow<Unit>()
    val moveToNextScreen = _moveToNextScreen.asSharedFlow()

    val passwordScreenState: StateFlow<PassWordState> =
        combine(
            idState,
            pwdState,
            pwdCheckState,
            buttonState,
        ) { idState, pwdState, pwdCheckState, buttonState ->
            val validId = (idState as? IdState.Success)?.validId == true
            val newButtonState = if (validId) {
                if (
                    pwdState == PwdState.Success &&
                    pwdCheckState == PwdCheckState.Success
                ) {
                    ButtonState.Enable
                } else {
                    ButtonState.Disable
                }
            } else {
                buttonState
            }
            PassWordState(
                idState,
                pwdState,
                pwdCheckState,
                newButtonState,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PassWordState.Empty,
        )

    init {
        viewModelScope.launch {
            _id.collectLatest {
                idState.emit(
                    when (validationId(it)) {
                        Validation.SUCCESS -> {
                            buttonState.emit(ButtonState.Enable)
                            IdState.Success(false)
                        }

                        Validation.EMPTY -> {
                            buttonState.emit(ButtonState.Disable)
                            IdState.Empty
                        }

                        else -> {
                            buttonState.emit(ButtonState.Disable)
                            IdState.Error(code = INVALID_MEMBER_ID)
                        }
                    },
                )
            }
        }
    }

    fun updateId(id: String) {
        _id.update {
            id
        }
    }

    fun updatePassword(password: String) {
        _pwd.update {
            password
        }
    }

    fun updatePasswordCheck(password: String) {
        _pwdCheck.update {
            password
        }
    }

    private fun updateScreenState() = mashUpScope {
        _moveToNextScreen.emit(Unit)
    }

    fun updateButtonState(button: ButtonState) {
        buttonState.update {
            button
        }
    }

    fun onClickNextButton(
        screen: ScreenState,
    ) = mashUpScope {
        when (screen) {
            is ScreenState.EnterId -> {
                if (passwordScreenState.value.idState is IdState.Success) {
                    updateButtonState(button = ButtonState.Disable)
                    updateScreenState()
                } else {
                    checkValidId()
                }
            }
            is ScreenState.ChangePassword -> {
                if (passwordScreenState.value.pwdState is PwdState.Success &&
                    passwordScreenState.value.pwdCheckState is PwdCheckState.Success
                ) {
                    checkValidPassword(id = _id.value, pwd = _pwdCheck.value)
                }
            }
            else -> {}
        }
    }

    private fun checkValidPassword(id: String, pwd: String) = mashUpScope {
        buttonState.emit(ButtonState.Loading)
        try {
            memberRepository.updatePassword(id = id, pwd = pwd).onSuccess {
                buttonState.emit(ButtonState.Enable)
                updateScreenState()
            }.onFailure {
                buttonState.emit(ButtonState.Disable)
            }
        } catch (ignore: Exception) {
            buttonState.emit(ButtonState.Disable)
        }
    }

    private fun checkValidId() = mashUpScope {
        buttonState.emit(ButtonState.Loading)
        try {
            memberRepository.validateId(_id.value)
                .onSuccess { validResponse ->
                    val (updatedButtonState, updatedIdState) = if (validResponse.valid) {
                        ButtonState.Enable to IdState.Success(true)
                    } else {
                        ButtonState.Disable to IdState.Error(code = MEMBER_DUPLICATED_IDENTIFICATION)
                    }
                    buttonState.emit(updatedButtonState)
                    idState.emit(updatedIdState)
                }.onFailure {
                    buttonState.emit(ButtonState.Disable)
                    idState.emit(IdState.Error(code = MEMBER_DUPLICATED_IDENTIFICATION))
                }
        } catch (ignore: Exception) {
            buttonState.emit(ButtonState.Disable)
        }
    }
}

data class PassWordState(
    val idState: IdState,
    val pwdState: PwdState,
    val pwdCheckState: PwdCheckState,
    val buttonState: ButtonState,
) {
    companion object {
        val Empty = PassWordState(
            idState = IdState.Empty,
            pwdState = PwdState.Empty,
            pwdCheckState = PwdCheckState.Empty,
            buttonState = ButtonState.Empty,
        )
    }
}

sealed interface ScreenState {
    object EnterId : ScreenState
    object ChangePassword : ScreenState
    object Auth : ScreenState
}
sealed interface IdState {
    object Empty : IdState
    data class Success(val validId: Boolean) : IdState
    data class Error(val code: String) : IdState
}

sealed interface PwdState {
    object Empty : PwdState
    object Success : PwdState
    object Error : PwdState
}

sealed interface PwdCheckState {
    object Empty : PwdCheckState
    object Success : PwdCheckState
    object Error : PwdCheckState
}

sealed interface ButtonState {
    object Empty : ButtonState
    object Enable : ButtonState
    object Disable : ButtonState
    object Loading : ButtonState
}
