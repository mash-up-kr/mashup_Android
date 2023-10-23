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
