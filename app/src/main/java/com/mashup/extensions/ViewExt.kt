package com.mashup.extensions

import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


fun View.onDebouncedClick(
    viewLifecycleScope: CoroutineScope,
    duration: Long = 1000L,
    clickListener: () -> Unit
) = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}.debounce(duration)
    .onEach { clickListener() }
    .launchIn(viewLifecycleScope)

fun View.findYPositionInView(targetView: View, yCumulative: Int): Int {
    if (this === targetView) {
        return yCumulative
    }
    if (this is ViewGroup) {
        val parentView = this as ViewGroup
        for (i in 0 until parentView.childCount) {
            val child = parentView.getChildAt(i)
            val yChild = yCumulative + child.y.toInt()
            val yNested = child.findYPositionInView(targetView, yChild)
            if (yNested != -1) return yNested
        }
    }
    return -1 // not found
}