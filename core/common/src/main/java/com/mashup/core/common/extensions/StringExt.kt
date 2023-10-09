package com.mashup.core.common.extensions

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String.fromHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

fun String.toBirthdayFormat(): String {
    return if (length == 8) {
        val year = substring(0, 4)
        val month = substring(4, 6)
        val day = substring(6, 8)

        "$year-$month-$day"
    } else {
        ""
    }
}
