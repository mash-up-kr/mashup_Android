package com.mashup.feature.danggn.di

import android.content.Context
import android.hardware.SensorManager
import com.mashup.feature.danggn.data.ShakeDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SensorModule {
    @Provides
    @Singleton
    fun providesShakeDetector(
        @ApplicationContext context: Context
    ): ShakeDetector {
        return ShakeDetector(
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        )
    }
}