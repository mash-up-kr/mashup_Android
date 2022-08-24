package com.mashup

import com.mashup.extensions.getListTypeAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Test

class GsonTest {

    private val moshi: Moshi by lazy { Moshi.Builder().build() }

    @Test
    fun intArray_to_jsonString() {
        val jsonString = "[1,2,3]"
        val adapter = moshi.getListTypeAdapter<Int>(Int::class)
        assertEquals(adapter.toJson(listOf(1, 2, 3)), jsonString)
    }

    @Test
    fun jsonString_to_List_int() {
        val jsonString = "[1,2,3]"
        val adapter = moshi.getListTypeAdapter<Int>(Int::class)
        assertEquals(adapter.fromJson(jsonString), listOf(1, 2, 3))
    }
}