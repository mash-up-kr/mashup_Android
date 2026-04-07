package com.mashup.ui.error.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mashup.ui.error.screen.NetworkDisconnectScreen
import com.mashup.ui.error.vm.NetworkDisconnectViewModel

@Composable
internal fun NetworkDisconnectRoute(
    viewModel: NetworkDisconnectViewModel,
    modifier: Modifier = Modifier
) {
    val showLoadingState by viewModel.loadingState.collectAsStateWithLifecycle()

    NetworkDisconnectScreen(
        showLoading = showLoadingState,
        modifier = modifier,
        onClickRetry = viewModel::onClickRetry
    )
}
