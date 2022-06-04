package com.mashup.ui.signup

fun validationId(id: String): Boolean {
    if (id.isEmpty()) return true
    return "[a-zA-Z]{1,15}".toRegex().matches(id)
}

fun validationPwd(pwd: String): Boolean {
    if (pwd.isEmpty()) return true
    return """
  ^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$
        """.trimIndent().toRegex().matches(pwd)
}

fun validationName(name: String): Boolean {
    return name.isNotBlank()
}

fun validationPlatform(platform: String): Boolean {
    return platform.isNotBlank()
}