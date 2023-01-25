package com.mashup.datastore.data.source

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mashup.core.model.data.local.AppPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class AppPreferenceSerializer : Serializer<AppPreference> {
    override val defaultValue: AppPreference
        get() = AppPreference(
            showCoachMarkInScheduleList = true
        )

    override suspend fun readFrom(input: InputStream): AppPreference =
        try {
            Json.decodeFromString(
                AppPreference.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPreference", serialization)
        }

    override suspend fun writeTo(t: AppPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(AppPreference.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}