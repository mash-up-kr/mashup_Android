package com.mashup.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}