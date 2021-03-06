package com.mashup.ui.signup.state

import com.mashup.common.Validation

data class CodeState(
    val code: String = "",
    val validationCode: Validation = Validation.EMPTY,
    val isValidationState: Boolean = false
)