package com.mashup.common.extensions

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat

fun Activity.setStatusBarColorRes(@ColorRes colorRes: Int) {
    window.statusBarColor = ResourcesCompat.getColor(resources, colorRes, null)
}

fun Activity.setStatusBarDarkTextColor(isDark: Boolean) {
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = isDark
}