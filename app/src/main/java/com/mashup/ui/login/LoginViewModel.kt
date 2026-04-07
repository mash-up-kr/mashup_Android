package com.mashup.ui.login

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.model.Validation
import com.mashup.core.firebase.FirebaseRepository
import com.mashup.core.model.Platform
import com.mashup.data.repository.MemberRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val firebaseRepository: FirebaseRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {
    private val _loginUiState = MutableStateFlow<LoginState>(LoginState.Empty)
    val loginUiState: StateFlow<LoginState> = _loginUiState

    private val id = MutableStateFlow("")
    private val pwd = MutableStateFlow("")

    init {
        checkAutoLogin()
    }

    val inputFieldState = id.combine(pwd) { id, pwd ->
        Pair(id, pwd)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = Pair("", "")
    )

    val loginValidation = id.combine(pwd) { id, pwd ->
        if (id.isNotBlank() && pwd.isNotBlank()) {
            Validation.SUCCESS
        } else {
            Validation.EMPTY
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = Validation.NONE
    )

    private fun checkAutoLogin() = mashUpScope {
        if (userPreferenceRepository.getUserPreference().first().token.isNotBlank()) {
            _loginUiState.emit(LoginState.Success(LoginType.AUTO))
        }
    }

    fun setId(id: String) {
        this.id.value = id
    }

    fun setPwd(pwd: String) {
        this.pwd.value = pwd
    }

    fun requestLogin(
        id: String,
        pwd: String
    ) = mashUpScope {
        _loginUiState.emit(LoginState.Loading)
        val response = memberRepository.login(
            identification = id,
            password = pwd,
            fcmToken = firebaseRepository.getFcmToken()
        )

        if (!response.isSuccess()) {
            handleErrorCode(response.code)
            return@mashUpScope
        }

        response.data?.apply {
            userPreferenceRepository.updateUserPreference(
                id = memberId,
                token = token,
                name = name,
                platform = Platform.getPlatform(platform),
                generationNumbers = generationNumbers,
                pushNotificationAgreed = pushNotificationAgreed
            )
        }
        _loginUiState.emit(LoginState.Success(LoginType.LOGIN))
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
    data class Success(val loginType: LoginType) : LoginState
    data class Error(val code: String) : LoginState
}
