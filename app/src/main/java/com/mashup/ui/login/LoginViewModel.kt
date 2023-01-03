package com.mashup.ui.login

import com.mashup.base.BaseViewModel
import com.mashup.core.common.model.Validation
import com.mashup.data.repository.MemberRepository
import com.mashup.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _loginUiState = MutableStateFlow<LoginState>(LoginState.Empty)
    val loginUiState: SharedFlow<LoginState> = _loginUiState

    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")

    init {
        checkAutoLogin()
    }

    val loginValidation = id.combine(pwd) { id, pwd ->
        if (id.isNotBlank() && pwd.isNotBlank()) {
            Validation.SUCCESS
        } else {
            Validation.EMPTY
        }
    }

    private fun checkAutoLogin() = mashUpScope {
        if (userRepository.getUserToken().isNullOrBlank().not()) {
            _loginUiState.emit(LoginState.Success)
        }
    }

    fun setId(id: String) {
        this.id.value = id
    }

    fun setPwd(pwd: String) {
        this.pwd.value = pwd
    }

    fun requestLogin(id: String, pwd: String) = mashUpScope {
        _loginUiState.emit(LoginState.Loading)
        val response = memberRepository.login(
            identification = id,
            password = pwd
        )

        if (!response.isSuccess()) {
            handleErrorCode(response.code)
            return@mashUpScope
        }

        userRepository.setUserData(
            token = response.data?.token,
            memberId = response.data?.memberId,
            generationNumbers = response.data?.generationNumbers

        )
        _loginUiState.emit(LoginState.Success)
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _loginUiState.emit(LoginState.Error(code))
        }
    }
}

sealed interface LoginState {
    object Empty : LoginState
    object Loading : LoginState
    object Success : LoginState
    data class Error(val code: String) : LoginState
}
