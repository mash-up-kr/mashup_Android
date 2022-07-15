package com.mashup.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val userDataStore: DataStore<Preferences> by lazy { context.userDataStore }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("accessToken")
    }

    var token: String?
        get() = read(KEY_TOKEN, null)
        set(value) {
            write(KEY_TOKEN, value)
        }

    private fun <T> read(key: Preferences.Key<T>, default: T? = null) = runBlocking {
        userDataStore.data.map {
            it[key] ?: default
        }.first()
    }

    private fun <T> write(key: Preferences.Key<T>, value: T?) = runBlocking {
        userDataStore.edit { preference ->
            value?.let {
                preference[key] = value
            }
        }
    }
}