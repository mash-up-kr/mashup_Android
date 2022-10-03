package com.mashup.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.attendanceDataStore: DataStore<Preferences> by preferencesDataStore(name = "attendance")

class AttendanceDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val attendanceDataStore: DataStore<Preferences> by lazy { context.attendanceDataStore }

    companion object {
        private val KEY_COACH_MARK = booleanPreferencesKey("coachMark")
    }

    var coachMark: Boolean
        get() = read(KEY_COACH_MARK, false) ?: false
        set(value) {
            write(KEY_COACH_MARK, value)
        }

    private fun <T> read(key: Preferences.Key<T>, default: T? = null) = runBlocking {
        attendanceDataStore.data.map {
            it[key] ?: default
        }.first()
    }

    private fun <T> write(key: Preferences.Key<T>, value: T?) = runBlocking {
        attendanceDataStore.edit { preference ->
            value?.let {
                preference[key] = value
            }
        }
    }
}
