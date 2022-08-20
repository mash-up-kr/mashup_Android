package com.mashup.extensions

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

fun <T> Moshi.getListTypeAdapter(dataType: Class<T>): JsonAdapter<List<T>> {
    val listIntType = Types.newParameterizedType(
        List::class.java,
        dataType
    )
    return adapter(listIntType)
}