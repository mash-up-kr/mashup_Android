package com.mashup.datastore.data.repository

import androidx.datastore.core.DataStore
import com.mashup.core.model.data.local.DanggnPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanggnPreferenceRepository @Inject constructor(
    private val danggnPreference: DataStore<DanggnPreference>
) {
    fun getDanggnPreference() = danggnPreference.data

    suspend fun updatePersonalFirstRanking(ranking: Int) {
        danggnPreference.updateData { savedDanggnPreferences ->
            savedDanggnPreferences.copy(
                personalFirstRanking = ranking
            )
        }
    }

    suspend fun updatePlatformFirstRanking(ranking: Int) {
        danggnPreference.updateData { savedDanggnPreferences ->
            savedDanggnPreferences.copy(
                platformFirstRanking = ranking
            )
        }
    }
}
