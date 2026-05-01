package com.mashup.ui.moremenu

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moremenu.MoreMenuRoute
import com.example.moremenu.model.Menu
import com.example.moremenu.model.MenuType
import com.example.moremenu.model.MoreMenuSideEffect
import com.mashup.base.BaseComposeActivity
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.notice.NoticeActivity
import com.mashup.ui.setting.SettingActivity
import com.mashup.ui.webview.birthday.BirthdayActivity
import com.mashup.ui.webview.mashong.MashongActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreMenuActivity : BaseComposeActivity() {
    private val moreMenuViewModel: MoreMenuViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        moreMenuViewModel.getMoreMenuState()
    }

    override fun initObserves() {
        super.initObserves()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moreMenuViewModel.moreMenuEvent.collect { sideEffect ->
                    when (sideEffect) {
                        is MoreMenuSideEffect.NavigateMenu -> onNavigateMenu(sideEffect.menu)
                        is MoreMenuSideEffect.NavigateBackStack -> finish()
                    }
                }
            }
        }
    }

    @Composable
    override fun MainContainer(modifier: Modifier) {
        val moreMenuState by moreMenuViewModel.moreMenuState.collectAsState()
        MoreMenuRoute(
            modifier = modifier,
            moreMenuState = moreMenuState,
            onBackPressed = moreMenuViewModel::onClickBackButton,
            onClickMenu = moreMenuViewModel::onClickMenuButton
        )
    }

    private fun onNavigateMenu(menu: Menu) {
        val intent = when (menu.type) {
            MenuType.NOTI -> NoticeActivity.newIntent(this)
            MenuType.DANGGN -> ShakeDanggnActivity.newIntent(this)
            MenuType.MASHONG -> MashongActivity.newIntent(this)
            MenuType.SETTING -> SettingActivity.newIntent(this)
            MenuType.BIRTHDAY -> BirthdayActivity.newIntent(this)
        }
        startActivity(intent)
    }
}
