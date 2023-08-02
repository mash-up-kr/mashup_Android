package com.mashup.network.interceptor

import android.os.Build
import android.util.Base64.NO_WRAP
import android.util.Base64.encode
import com.mashup.BuildConfig
import okhttp3.Interceptor
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class BaseInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request()
            .newBuilder()
            .apply {
                header("cipher", encrypt())
                header("Content-Type", "application/json")
            }
            .build()
    )

    private fun encrypt(): String {
        val apikey = BuildConfig.API_KEY
        val currentTime = System.currentTimeMillis() / 1000
        val secretKey = SecretKeySpec(apikey.toByteArray(), "AES")

        // AES 하나만 쓰지 않으면 값이 달라짐 한개만 사용시에 (ECB)
        // 여러개 사용시에 CBC ex) "AES/ECB/PKCS5Padding" (CBC)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encoded = cipher.doFinal(currentTime.toString().toByteArray())
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(encoded)
        } else {
            encode(encoded, NO_WRAP).toString(Charsets.UTF_8)
        }
    }
}
