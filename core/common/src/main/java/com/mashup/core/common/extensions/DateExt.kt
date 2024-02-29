package com.mashup.core.common.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.format(pattern: String, locale: Locale = Locale.getDefault()): String {
    val dateFormat = SimpleDateFormat(pattern, locale)
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
    return dateFormat.format(this)
}

fun Date.getTimeFormat(): String {
    return try {
        val timeLineFormat = SimpleDateFormat("a hh:mm", Locale.ENGLISH)
        timeLineFormat.format(this)
    } catch (ignore: Exception) {
        "??:??"
    }
}
