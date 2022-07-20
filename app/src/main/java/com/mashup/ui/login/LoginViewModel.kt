package com.mashup.ui.login

import androidx.lifecycle.viewModelScope
import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MemberRepository
import com.mashup.common.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    private val _loginUiState = MutableSharedFlow<LoginState>()
    val loginUiState: SharedFlow<LoginState> = _loginUiState

    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")

    val loginValidation = id.combine(pwd) { id, pwd ->
        if (id.isNotBlank() && pwd.isNotBlank()) {
            Validation.SUCCESS
        } else {
            Validation.EMPTY
        }
    }

    var isReady: Boolean = false
        private set

    init {
        ready()
    }

    /**
     * Splash API 2초 딜레이를 위한 로직
     */
    private fun ready() = viewModelScope.launch {
        delay(2000L)
        isReady = true
        checkAutoLogin()
    }

    private fun checkAutoLogin() = mashUpScope {
        if (userDataSource.token != null) {
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
        val response = memberRepository.login(
            identification = id,
            password = pwd
        )

        if (!response.isSuccess()) {
            handleSignUpError(response.code, response.message)
            return@mashUpScope
        }

        userDataSource.token = response.data?.token
        _loginUiState.emit(LoginState.Success)
    }

    private fun handleSignUpError(errorCode: String, message: String?) = mashUpScope {
        _loginUiState.emit(LoginState.Error(errorCode, message))
    }
}

sealed interface LoginState {
    object Success : LoginState
    data class Error(val code: String, val message: String?) : LoginState
}