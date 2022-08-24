package com.mashup.extensions

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlin.reflect.KClass

fun <T> Moshi.getListTypeAdapter(dataType: KClass<*>): JsonAdapter<List<T?>> {
    val listIntType = Types.newParameterizedType(
        List::class.java,
        dataType.javaObjectType
    )
    return adapter<List<T?>>(listIntType).nullSafe()
}