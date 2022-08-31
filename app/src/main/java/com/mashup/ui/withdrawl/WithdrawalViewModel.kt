package com.mashup.ui.withdrawl

import com.mashup.base.BaseViewModel
import com.mashup.common.model.Validation
import com.mashup.data.repository.MemberRepository
import com.mashup.ui.signup.state.CodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : BaseViewModel() {
    private val withdrawalCode = MutableStateFlow("")
    val codeState = withdrawalCode
        .debounce(250)
        .map {
            val state = when (it) {
                "탈퇴할게요" -> Validation.SUCCESS
                "" -> Validation.EMPTY
                else -> Validation.FAILED
            }
            val codeState = CodeState(
                code = it,
                validationCode = state,
                isValidationState = state == Validation.SUCCESS
            )
            codeState
        }

    private val _withdrawalState = MutableSharedFlow<WithdrawalState>()
    val withdrawalState: SharedFlow<WithdrawalState> = _withdrawalState

    fun setCode(code: String) {
        withdrawalCode.value = code
    }

    fun deleteMember() = mashUpScope {
        _withdrawalState.emit(WithdrawalState.Loading)
        val result = memberRepository.deleteMember()
        if (!result.isSuccess()) {
            handleErrorCode(result.code)
        }

        _withdrawalState.emit(WithdrawalState.Success)
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _withdrawalState.emit(WithdrawalState.Error(code))
        }
    }
}

sealed interface WithdrawalState {
    object Loading : WithdrawalState
    object Success : WithdrawalState
    data class Error(val code: String) : WithdrawalState
}