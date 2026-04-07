package com.mashup.ui.error

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mashup.base.BaseComposeActivity
import com.mashup.ui.error.route.NetworkDisconnectRoute
import com.mashup.ui.error.vm.NetworkDisconnectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkDisconnectActivity : BaseComposeActivity() {
    private val viewModel: NetworkDisconnectViewModel by viewModels()

    @Composable
    override fun MainContainer(modifier: Modifier) {
        LaunchedEffect(viewModel.sideEffect) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is NetworkDisconnectViewModel.Effect.FinishView -> finish()
                }
            }
        }

        NetworkDisconnectRoute(
            viewModel = viewModel,
            modifier = modifier
        )
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, NetworkDisconnectActivity::class.java)
        }
    }
}
