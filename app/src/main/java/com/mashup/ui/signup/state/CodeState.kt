package com.mashup.ui.signup.state

data class CodeState(
    val code: String = "",
    val isWrongCode: Boolean = false,
    val validationState: Boolean = false
)