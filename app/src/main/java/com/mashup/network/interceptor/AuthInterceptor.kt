package com.mashup.network.interceptor

import com.mashup.data.datastore.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userDataSource: UserDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request()
            .newBuilder()
            .apply {
                runBlocking(Dispatchers.IO) {
                    userDataSource.token?.let { token ->
                        header(AUTHORIZATION_KEY, "Bearer $token")
                    }
                }
            }
            .build()
    )

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}
