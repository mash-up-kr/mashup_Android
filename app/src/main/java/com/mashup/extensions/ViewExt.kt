package com.mashup.extensions

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun View.onDebouncedClick(
    viewLifecycleScope: CoroutineScope,
    clickListener: () -> Unit
) = callbackFlow {
    setOnClickListener {
        trySend(Unit)
    }
    awaitClose { setOnClickListener(null) }
}.onEach { clickListener() }
    .launchIn(viewLifecycleScope)