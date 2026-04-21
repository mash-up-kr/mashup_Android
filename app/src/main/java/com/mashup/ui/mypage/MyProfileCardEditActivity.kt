package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.mashup.base.BaseViewBindingActivity
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.model.Platform
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityMyProfileCardEditBinding
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel
import com.mashup.feature.mypage.profile.edit.MyPageEditCardScreen
import com.mashup.feature.mypage.profile.model.LoadState
import com.mashup.feature.mypage.profile.model.MyProfileCardEntity
import com.mashup.feature.mypage.profile.model.ProfileCardData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyProfileCardEditActivity : BaseViewBindingActivity<ActivityMyProfileCardEditBinding>() {
    override val viewBinding by lazy { ActivityMyProfileCardEditBinding.inflate(layoutInflater) }

    private val editViewModel: MyPageProfileEditViewModel by viewModels()
    private var team = ""
    private var staff = ""

    override fun initViews() {
        super.initViews()

        val extra = intent.extras ?: return

        team = extra.getString(EXTRA_CARD_TEAM).orEmpty()
        staff = extra.getString(EXTRA_CARD_STAFF).orEmpty()

        val profileCard = MyProfileCardEntity(
            id = extra.getInt(EXTRA_CARD_ID),
            generationNumber = extra.getInt(EXTRA_CARD_GENERATION),
            platform = Platform.getPlatform(extra.getString(EXTRA_CARD_PLATFORM) ?: return),
            team = team,
            staff = staff
        )

        viewBinding.composeView.setContent {
            MashUpTheme {
                MyPageEditCardScreen(
                    profileCard = profileCard,
                    onBackPressed = ::finish,
                    patchMemberProfileCard = ::patchMemberProfileCard,
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                )
            }
        }

        setObserver()
    }

    private fun setObserver() {
        flowLifecycleScope {
            editViewModel.profileCardState.collectLatest {
                if (it is LoadState.Loaded) {
                    ToastUtil.showToast(this@MyProfileCardEditActivity, "저장 완료!")

                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_CARD_TEAM, team)
                        putExtra(EXTRA_CARD_STAFF, staff)
                    }

                    setResult(RESULT_OK, resultIntent)
                }
            }
        }
    }

    private fun patchMemberProfileCard(id: Long, team: String, staff: String) {
        this.team = team
        this.staff = staff
        editViewModel.patchMemberProfileCard(id, team, staff)
    }

    companion object {
        private const val EXTRA_CARD_ID = "EXTRA_CARD_ID"
        private const val EXTRA_CARD_GENERATION = "EXTRA_CARD_GENERATION"
        private const val EXTRA_CARD_PLATFORM = "EXTRA_CARD_PLATFORM"
        const val EXTRA_CARD_TEAM = "EXTRA_CARD_TEAM"
        const val EXTRA_CARD_STAFF = "EXTRA_CARD_STAFF"

        fun newIntent(context: Context, card: ProfileCardData): Intent {
            return Intent(context, MyProfileCardEditActivity::class.java).apply {
                putExtra(EXTRA_CARD_ID, card.id)
                putExtra(EXTRA_CARD_GENERATION, card.generationNumber)
                putExtra(EXTRA_CARD_PLATFORM, card.platform.detailName)
                putExtra(EXTRA_CARD_TEAM, card.projectTeamName)
                putExtra(EXTRA_CARD_STAFF, card.role)
            }
        }
    }
}
