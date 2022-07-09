package com.mashup.di

import com.mashup.BuildConfig.DEBUG_MODE
import com.mashup.network.API_HOST
import com.mashup.network.TIME_OUT_REQUEST_API
import com.mashup.network.interceptor.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)

        if (DEBUG_MODE) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor())
        }
        return okHttpClient
            .readTimeout(TIME_OUT_REQUEST_API, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_REQUEST_API, TimeUnit.SECONDS)
            .callTimeout(TIME_OUT_REQUEST_API, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_HOST)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}