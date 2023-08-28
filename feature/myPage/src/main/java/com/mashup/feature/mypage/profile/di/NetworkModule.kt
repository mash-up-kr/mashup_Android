package com.mashup.feature.mypage.profile.di

import com.mashup.feature.mypage.profile.data.MyProfileDao
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
    fun provideMyProfileDao(
        retrofit: Retrofit
    ): MyProfileDao {
        return retrofit.create()
    }
}