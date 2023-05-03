package com.mashup.core.common.utils

import android.content.Context

fun Context.getDrawableResIdByName(resourceName: String): Int {
    return resources.getIdentifier(resourceName, "drawable", packageName)
}