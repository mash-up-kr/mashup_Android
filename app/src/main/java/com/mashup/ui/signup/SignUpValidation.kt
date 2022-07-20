package com.mashup.ui.signup

import com.mashup.common.Validation

fun validationId(id: String): Validation {
    if (id.isEmpty()) return Validation.EMPTY
    return if ("[a-zA-Z]{1,15}".toRegex().matches(id)) {
        Validation.SUCCESS
    } else {
        Validation.FAILED
    }
}

fun validationPwd(pwd: String): Validation {
    if (pwd.isEmpty()) return Validation.EMPTY
    return if ("""
  ^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$
        """.trimIndent().toRegex().matches(pwd)
    ) {
        Validation.SUCCESS
    } else {
        Validation.FAILED
    }
}

fun validationPwdCheck(pwd: String, pwdCheck: String): Validation {
    if (pwdCheck.isEmpty()) return Validation.EMPTY
    return if (pwd == pwdCheck) {
        Validation.SUCCESS
    } else {
        Validation.FAILED
    }
}

fun validationName(name: String): Validation {
    if (name.isEmpty()) return Validation.EMPTY
    return if (name.isNotBlank()) {
        Validation.SUCCESS
    } else {
        Validation.FAILED
    }
}

fun validationPlatform(platform: String): Validation {
    if (platform.isEmpty()) return Validation.EMPTY
    return if (platform.isNotBlank()) {
        Validation.SUCCESS
    } else {
        Validation.FAILED
    }
}

fun validationCode(code: String): Validation {
    if (code.isEmpty()) return Validation.EMPTY
    return if ("[a-zA-Z]{6}".toRegex().matches(code)) {
        Validation.SUCCESS
    } else {
        Validation.FAILED
    }
}