package com.mashup.ui.login

import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class LoginViewModel(
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    val loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() = mashUpScope {
        if (userDataSource.token != null) {
            loginUiState.emit(LoginUiState.LoginSuccess)
        }
    }

    fun login(id: String, pwd: String) {

    }
}

sealed interface LoginUiState {
    object Loading : LoginUiState
    object LoginSuccess : LoginUiState
    object Empty : LoginUiState
}