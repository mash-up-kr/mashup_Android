package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private var team by mutableStateOf("")
    private var staff by mutableStateOf("")

    private val profileCardEditLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            team = result.data?.getStringExtra(EXTRA_CARD_TEAM).orEmpty()
            staff = result.data?.getStringExtra(EXTRA_CARD_STAFF).orEmpty()
            setResult(RESULT_OK) // 마이페이지 데이터 업데이트를 위해 RESULT_OK 설정해줌
        }
    }

    override fun initViews() {
        super.initViews()

        val cardData = intent.extras ?: return
        team = cardData.getString(EXTRA_CARD_TEAM).orEmpty()
        staff = cardData.getString(EXTRA_CARD_STAFF).orEmpty()

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
                        team = team,
                        staff = staff,
                        onBackPressed = { finish() },
                        onDownLoadClicked = ::downloadProfileCardImage,
                        onEditClicked = ::startMyProfileCardActivity
                    )
                }
            }
        }
    }

    private fun startMyProfileCardActivity() {
        val intent = MyProfileEditActivity.newIntent(this, MyProfileEditActivity.TYPE_EDIT_PROFILE_CARD)
        profileCardEditLauncher.launch(intent)
    }

    private fun downloadProfileCardImage() {
    }

    companion object {
        private const val EXTRA_CARD_NAME = "EXTRA_CARD_NAME"
        private const val EXTRA_CARD_GENERATION = "EXTRA_CARD_GENERATION"
        private const val EXTRA_CARD_PLATFORM = "EXTRA_CARD_PLATFORM"
        private const val EXTRA_CARD_IS_RUNNING = "EXTRA_CARD_IS_RUNNING"
        const val EXTRA_CARD_TEAM = "EXTRA_CARD_TEAM"
        const val EXTRA_CARD_STAFF = "EXTRA_CARD_STAFF"

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
