package com.mashup.ui.signup.state

import com.mashup.ui.signup.model.Validation

data class CodeState(
    val code: String = "",
    val validationCode: Validation = Validation.EMPTY,
    val isValidationState: Boolean = false
)