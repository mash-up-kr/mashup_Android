package com.mashup.network.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.RemoteException
import com.mashup.network.NetworkStatusState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NetworkStatusDetector(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var callback: ConnectivityManager.NetworkCallback? = null
    private var receiver: ConnectivityReceiver? = null

    private val _state = MutableStateFlow(getCurrentNetwork())
    val state: StateFlow<NetworkStatusState> = _state

    init {
        _state
            .subscriptionCount
            .map { count -> count > 0 } // map count into active/inactive flag
            .distinctUntilChanged()
            .onEach { isActive ->
                if (isActive) {
                    subscribe()
                } else {
                    unsubscribe()
                }
            }
            .launchIn(coroutineScope)
    }

    fun hasNetworkConnection() = getCurrentNetwork() == NetworkStatusState.NetworkConnected

    private fun getCurrentNetwork(): NetworkStatusState {
        return try {
            cm.getNetworkCapabilities(cm.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .let { connected ->
                    if (connected == true) {
                        NetworkStatusState.NetworkConnected
                    } else {
                        NetworkStatusState.NetworkDisconnected
                    }
                }
        } catch (e: RemoteException) {
            NetworkStatusState.NetworkDisconnected
        }
    }

    private fun subscribe() {
        if (callback != null || receiver != null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback = NetworkCallbackImpl().also { cm.registerDefaultNetworkCallback(it) }
        } else {
            receiver = ConnectivityReceiver().also {
                context.registerReceiver(it, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }

        emitNetworkState(getCurrentNetwork())
    }

    private fun unsubscribe() {
        if (callback == null && receiver == null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback?.run { cm.unregisterNetworkCallback(this) }
            callback = null
        } else {
            receiver?.run { context.unregisterReceiver(this) }
            receiver = null
        }
    }

    private fun emitNetworkState(newState: NetworkStatusState) {
        coroutineScope.launch {
            _state.emit(newState)
        }
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            intent
                .getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
                ?.isConnectedOrConnecting
                .let { connected ->
                    if (connected == true) {
                        emitNetworkState(NetworkStatusState.NetworkConnected)
                    } else {
                        emitNetworkState(getCurrentNetwork())
                    }
                }
        }
    }

    private inner class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) =
            emitNetworkState(NetworkStatusState.NetworkConnected)

        override fun onLost(network: Network) =
            emitNetworkState(getCurrentNetwork())
    }
}
