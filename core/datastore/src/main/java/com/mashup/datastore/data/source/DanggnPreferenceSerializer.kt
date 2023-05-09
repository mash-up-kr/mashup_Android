package com.mashup.datastore.data.source

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.mashup.core.model.data.local.DanggnPreference
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class DanggnPreferenceSerializer : Serializer<DanggnPreference> {
    override val defaultValue: DanggnPreference
        get() = DanggnPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): DanggnPreference =
        try {
            Json.decodeFromString(
                DanggnPreference.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPreference", serialization)
        }

    override suspend fun writeTo(t: DanggnPreference, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(DanggnPreference.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}
