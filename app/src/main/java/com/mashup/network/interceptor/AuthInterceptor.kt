package com.mashup.network.interceptor

import com.mashup.data.datastore.UserDataSource
import com.mashup.network.errorcode.UNAUTHORIZED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userDataSource: UserDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(
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
        val responseJson = extractResponseJson(response)
        checkApiStatusCode(responseJson)

        return response.newBuilder()
            .body(responseJson.toString().toResponseBody())
            .build()
    }

    private fun extractResponseJson(response: Response): JSONObject {
        val jsonString = response.body?.string() ?: "{}"
        return JSONObject(jsonString)
    }

    private fun checkApiStatusCode(jsonObject: JSONObject) {
        when (jsonObject.get("code")) {
            UNAUTHORIZED -> {
                userDataSource.token = ""
            }
        }
    }

    companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}
