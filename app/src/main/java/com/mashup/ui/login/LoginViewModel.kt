package com.mashup.ui.login

import androidx.lifecycle.viewModelScope
import com.mashup.base.BaseViewModel
import com.mashup.common.model.Validation
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
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

    /**
     * Splash API 2초 딜레이를 위한 로직
     */
    fun ready() = viewModelScope.launch {
        delay(2000L)
        isReady = true
        checkAutoLogin()
    }

    private fun checkAutoLogin() = mashUpScope {
        if (!userDataSource.token.isNullOrBlank()) {
            _loginUiState.emit(LoginState.Success)
        }
    }

    fun setId(id: String) {
        this.id.value = id
    }

    fun setPwd(pwd: String) {
        this.pwd.value = pwd
    }

    fun clearUserData() {
        userDataSource.token = ""
        userDataSource.memberId = null
        userDataSource.generateNumbers = null
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

        userDataSource.token = response.data?.token
        userDataSource.generateNumbers = response.data?.generationNumbers
        userDataSource.memberId = response.data?.memberId
        _loginUiState.emit(LoginState.Success)
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _loginUiState.emit(LoginState.Error(code))
        }
    }
}

sealed interface LoginState {
    object Loading : LoginState
    object Success : LoginState
    data class Error(val code: String) : LoginState
}