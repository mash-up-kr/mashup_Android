package com.mashup.ui.webview.mashong

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.webview.WebViewScreen
import com.mashup.ui.webview.WebViewUiState
import com.mashup.util.setFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MashongActivity : ComponentActivity() {

    private val mashongViewModel by viewModels<MashongViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MashUpTheme {
                val webViewUiState by mashongViewModel.webViewUiState.collectAsState(
                    WebViewUiState.Loading
                )
                WebViewScreen(
                    modifier = Modifier.fillMaxSize(),
                    webViewUiState = webViewUiState,
                    mashupBridge = MashupBridge(
                        this@MashongActivity,
                        onBackPressed = ::finish,
                        onNavigateDanggn = {
                            startActivity(ShakeDanggnActivity.newIntent(this@MashongActivity))
                        }
                    ),
                    isShowMashUpToolbar = false
                )
            }
        }

        setFullScreen()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, MashongActivity::class.java)
    }
}
