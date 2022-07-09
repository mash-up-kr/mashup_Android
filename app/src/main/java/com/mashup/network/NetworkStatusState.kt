package com.mashup.network

sealed class NetworkStatusState {
    object NetworkConnected : NetworkStatusState()
    object NetworkDisconnected : NetworkStatusState()
}