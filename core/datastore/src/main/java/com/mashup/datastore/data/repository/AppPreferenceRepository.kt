package com.mashup.datastore.data.repository

import androidx.datastore.core.DataStore
import com.mashup.core.model.data.local.AppPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceRepository @Inject constructor(
    private val appPreferenceDataSource: DataStore<AppPreference>
) {
    fun getAppPreference() = appPreferenceDataSource.data

    suspend fun updateCoachMarkScheduleList(
        isShowCoachMarkInScheduleList: Boolean
    ) {
        appPreferenceDataSource.updateData { savedAppPreferences ->
            savedAppPreferences.copy(
                isShowCoachMarkInScheduleList = isShowCoachMarkInScheduleList
            )
        }
    }
}