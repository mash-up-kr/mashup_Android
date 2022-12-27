package com.mashup.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mashup.extensions.getListTypeAdapter
import com.squareup.moshi.Moshi
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
    private val moshi: Moshi by lazy { Moshi.Builder().build() }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("accessToken")
        private val KEY_MEMBER_ID = stringPreferencesKey("memberId")
        private val KEY_GENERATE_NUMBERS = stringPreferencesKey("generateNumber")
    }

    var token: String?
        get() = read(KEY_TOKEN, null)
        set(value) {
            write(KEY_TOKEN, value)
        }

    var memberId: Int?
        get() = read(KEY_MEMBER_ID, null)?.toIntOrNull()
        set(value) {
            write(KEY_MEMBER_ID, value?.toString())
        }

    var generateNumbers: List<Int>?
        get() {
            val adapter = moshi.getListTypeAdapter<Int>(Int::class)
            return adapter.fromJson(read(KEY_GENERATE_NUMBERS, "[]").toString())?.map { it ?: 0 }
        }
        set(value) {
            val adapter = moshi.getListTypeAdapter<Int>(Int::class)
            write(KEY_GENERATE_NUMBERS, adapter.toJson(value))
        }

    private fun <T> read(key: Preferences.Key<T>, default: T? = null) = runBlocking {
        userDataStore.data.map {
            it[key] ?: default
        }.first()
    }

    private fun <T> write(key: Preferences.Key<T>, value: T?) = runBlocking {
        userDataStore.edit { preference ->
            if (value == null) {
                preference.remove(key)
            } else {
                preference[key] = value
            }
        }
    }
}
