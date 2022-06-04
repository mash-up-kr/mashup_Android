package com.mashup.ui.signup.state

data class MemberState(
    val name: String = "",
    val platform: String = "",
    val isValidationState: Boolean = false
)