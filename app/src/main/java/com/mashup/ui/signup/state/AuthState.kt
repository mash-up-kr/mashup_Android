package com.mashup.ui.signup.state

data class AuthState(
    val id: String = "",
    val pwd: String = "",
    val pwdCheck: String = "",
    val isWrongId: Boolean = false,
    val isWrongPwd: Boolean = false,
    val isWrongPwdCheck: Boolean = false,
    val validationState: Boolean = false
)
