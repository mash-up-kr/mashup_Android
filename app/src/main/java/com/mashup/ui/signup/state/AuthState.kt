package com.mashup.ui.signup.state

import com.mashup.common.Validation

data class AuthState(
    val id: String = "",
    val pwd: String = "",
    val pwdCheck: String = "",
    val validationId: Validation = Validation.EMPTY,
    val validationPwd: Validation = Validation.EMPTY,
    val validationPwdCheck: Validation = Validation.EMPTY,
    val isValidationState: Boolean = false
)
