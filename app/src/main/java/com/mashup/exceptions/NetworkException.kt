package com.mashup.exceptions

sealed class NetworkException(open val code: Int, open val message: String)

data class ClientNetworkException(
    override val code: Int, override val message: String
) : NetworkException(code, message)

data class ServerNetworkException(
    override val code: Int, override val message: String
) : NetworkException(code, message)

data class UnknownException(
    override val code: Int, override val message: String
) : NetworkException(code, message)