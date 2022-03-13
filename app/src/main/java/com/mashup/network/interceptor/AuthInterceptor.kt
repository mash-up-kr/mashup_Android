package com.mashup.network.interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request()
            .newBuilder()
            .apply {
                runBlocking(Dispatchers.IO) {
                    //TODO: auth token local DB 에서 가져오기
                    header(AUTHORIZATION_KEY, "token")
                }
            }
            .build()
    )

    companion object {
        private const val AUTHORIZATION_KEY = "AUTHORIZATION"
    }
}