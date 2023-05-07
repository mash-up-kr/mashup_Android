package com.mashup.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.mashup.core.model.data.local.AppPreference
import com.mashup.core.model.data.local.DanggnPreference
import com.mashup.core.model.data.local.UserPreference
import com.mashup.datastore.data.source.AppPreferenceSerializer
import com.mashup.datastore.data.source.DanggnPreferenceSerializer
import com.mashup.datastore.data.source.UserPreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {

    companion object {
        private const val PATH_PB_USER = "mashup_user.preferences_pb"
        private const val PATH_PB_APP = "mashup_app.preferences_pb"
        private const val PATH_PB_DANGGN = "mashup_danggn.preferences_pb"
    }

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserPreference> {
        return DataStoreFactory.create(
            serializer = UserPreferenceSerializer()
        ) {
            File("${context.cacheDir.path}/$PATH_PB_USER")
        }
    }

    @Provides
    @Singleton
    fun providesAppPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<AppPreference> {
        return DataStoreFactory.create(
            serializer = AppPreferenceSerializer()
        ) {
            File("${context.cacheDir.path}/$PATH_PB_APP")
        }
    }

    @Provides
    @Singleton
    fun providesDanggnPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<DanggnPreference> {
        return DataStoreFactory.create(
            serializer = DanggnPreferenceSerializer()
        ) {
            File("${context.cacheDir.path}/$PATH_PB_DANGGN")
        }
    }
}
