package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityMyProfileCardEditBinding
import com.mashup.feature.mypage.profile.card.ProfileCardDetailContent
import com.mashup.feature.mypage.profile.model.ProfileCardData

class MyProfileCardEditActivity : BaseActivity<ActivityMyProfileCardEditBinding>() {
    override val layoutId = R.layout.activity_my_profile_card_edit

    override fun initViews() {
        super.initViews()

        val cardData = intent.extras ?: return

        viewBinding.composeView.setContent {
            MashUpTheme {
                Surface(
                    color = Gray50
                ) {
                    ProfileCardDetailContent(
                        modifier = Modifier.fillMaxSize(),
                        generationNumber = cardData.getInt(EXTRA_CARD_GENERATION),
                        name = cardData.getString(EXTRA_CARD_NAME).orEmpty(),
                        platform = Platform.getPlatform(cardData.getString(EXTRA_CARD_PLATFORM).orEmpty()),
                        isRunning = cardData.getBoolean(EXTRA_CARD_IS_RUNNING),
                        team = cardData.getString(EXTRA_CARD_TEAM).orEmpty(),
                        staff = cardData.getString(EXTRA_CARD_STAFF).orEmpty(),
                        onBackPressed = { finish() },
                        onDownLoadClicked = {},
                        onEditClicked = {}
                    )
                }
            }
        }
    }

    companion object {
        private const val EXTRA_CARD_NAME = "EXTRA_CARD_NAME"
        private const val EXTRA_CARD_GENERATION = "EXTRA_CARD_GENERATION"
        private const val EXTRA_CARD_PLATFORM = "EXTRA_CARD_PLATFORM"
        private const val EXTRA_CARD_IS_RUNNING = "EXTRA_CARD_IS_RUNNING"
        private const val EXTRA_CARD_TEAM = "EXTRA_CARD_TEAM"
        private const val EXTRA_CARD_STAFF = "EXTRA_CARD_STAFF"

        fun newIntent(context: Context, card: ProfileCardData): Intent {
            return Intent(context, MyProfileCardEditActivity::class.java).apply {
                putExtra(EXTRA_CARD_NAME, card.name)
                putExtra(EXTRA_CARD_GENERATION, card.generationNumber)
                putExtra(EXTRA_CARD_PLATFORM, card.platform)
                putExtra(EXTRA_CARD_IS_RUNNING, card.isRunning)
                putExtra(EXTRA_CARD_TEAM, card.projectTeamName)
                putExtra(EXTRA_CARD_STAFF, card.role)
            }
        }
    }
}
