package com.mashup.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    //TODO: replace base Url
    private const val BASE_URL = "https://sample.com"
}