package com.mashup.ui.register.ui.signin.fragment

fun validationId(id: String): Boolean {
    return "[a-zA-Z]{1,15}".toRegex().matches(id)
}

fun validationPwd(pwd: String): Boolean {
    return """
  ^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$
        """.trimIndent().toRegex().matches(pwd)
}