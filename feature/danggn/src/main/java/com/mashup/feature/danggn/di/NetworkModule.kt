package com.mashup.feature.danggn.di

import com.mashup.feature.danggn.data.DanggnDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideDanggnDao(
        retrofit: Retrofit
    ): DanggnDao {
        return retrofit.create()
    }
}
