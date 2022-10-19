package com.mashup.core.signup.state

import com.mashup.common.model.Validation

data class CodeState(
    val code: String = "",
    val validationCode: Validation = Validation.EMPTY,
    val isValidationState: Boolean = false
)
