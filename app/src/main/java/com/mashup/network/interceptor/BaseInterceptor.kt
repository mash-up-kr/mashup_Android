package com.mashup.network.interceptor

import okhttp3.Interceptor
import javax.inject.Inject

class BaseInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request()
            .newBuilder()
            .apply {
                header("Content-Type", "application/json")
            }
            .build()
    )
}
