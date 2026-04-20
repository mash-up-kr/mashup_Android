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
import androidx.lifecycle.lifecycleScope
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.core.common.bridge.MashupBridge
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.data.network.WEB_HOST
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.webview.WebViewScreen
import com.mashup.ui.webview.WebViewUiState
import com.mashup.ui.webview.WebViewViewModel
import com.mashup.util.setFullScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MashongActivity : ComponentActivity() {

    private val webViewViewModel by viewModels<WebViewViewModel>()

    @Inject
    lateinit var userPreferenceRepository: UserPreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val prefs = userPreferenceRepository.getUserPreference().first()
            intent.putExtra(EXTRA_URL_KEY, WEB_HOST + "mashong/" + prefs.platform)

            setContent {
                MashUpTheme {
                    val webViewUiState by webViewViewModel.webViewUiState.collectAsState(
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
            Intent(context, MashongActivity::class.java).apply {
                putExtra(EXTRA_TITLE_KEY, "mashong")
            }
    }
}
