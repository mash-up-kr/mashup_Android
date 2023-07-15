package com.mashup.core.network.adapter

import com.mashup.core.model.Platform
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class PlatformJsonAdapter: JsonAdapter<Platform>() {

    override fun fromJson(reader: JsonReader): Platform {
        return Platform.getPlatform(reader.nextString())
    }

    override fun toJson(writer: JsonWriter, value: Platform?) {
        writer.value(value?.name ?: Platform.UNKNOWN.name)
    }
}