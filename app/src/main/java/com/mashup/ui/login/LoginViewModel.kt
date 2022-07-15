package com.mashup.ui.login

import androidx.lifecycle.viewModelScope
import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    val loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)

    var isReady: Boolean = false
        private set

    init {
        ready()
        checkAutoLogin()
    }

    private fun ready() = viewModelScope.launch {
        delay(2000L)
        isReady = true
    }

    private fun checkAutoLogin() = mashUpScope {
        if (userDataSource.token != null) {
            loginUiState.emit(LoginUiState.LoginSuccess)
        }
    }

    fun login(id: String, pwd: String) = mashUpScope {
        loginUiState.emit(LoginUiState.LoginSuccess)
    }
}

sealed interface LoginUiState {
    object Loading : LoginUiState
    object LoginSuccess : LoginUiState
    object Empty : LoginUiState
}