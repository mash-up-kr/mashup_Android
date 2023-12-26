package com.mashup.ui.mypage.viewholder

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mashup.core.common.R
import com.mashup.core.common.extensions.dp
import com.mashup.databinding.ItemMypageProfileBinding
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.AttendanceListAdapter

class MyPageProfileViewHolder(
    private val binding: ItemMypageProfileBinding,
    private val listener: AttendanceListAdapter.OnItemEventListener?
) : MyPageBaseViewHolder(binding.root) {

    init {
        binding.btnEditSelfIntroduce.setOnClickListener {
            listener?.onStartEditProfileActivity()
        }
    }

    override fun bind(item: AttendanceModel) {
        val profile = (item as AttendanceModel.MyProfile).data

        binding.data = profile

        binding.tvBirthday.isVisible = profile.birthDay.isNotEmpty()
        binding.tvIntroduceMyself.isVisible = profile.introduceMySelf.isNotEmpty()
        binding.tvSelfIntroduceEmpty.isVisible = profile.introduceMySelf.isEmpty()

        getChipList(profile).also { chipList ->
            binding.chipGroup.removeAllViews()
            binding.chipGroup.isVisible = chipList.isNotEmpty()

            chipList.forEach {
                binding.chipGroup.addChip(it)
            }
        }
    }

    // 표시되는 순서대로 ProfileData List 생성 (빈 값일 경우 표시하지 않는다)
    private fun getChipList(profile: ProfileData) = listOf(
        ProfileChip.Text(profile.work),
        ProfileChip.Text(profile.company),
        ProfileChip.Text(profile.location),
        ProfileChip.Link(
            displayText = if (profile.github.isNotEmpty()) "Github" else "",
            link = profile.github
        ),
        ProfileChip.Link(
            displayText = if (profile.behance.isNotEmpty()) "LinkedIn" else "",
            link = profile.linkedIn
        ),
        ProfileChip.Link(
            displayText = if (profile.behance.isNotEmpty()) "Behance" else "",
            link = profile.behance
        ),
        ProfileChip.Instagram(
            displayText = if (profile.instagram.isNotEmpty()) "@${profile.instagram}" else ""
        )
    ).filter { it.displayText.isNotEmpty() }

    private fun ChipGroup.addChip(chipData: ProfileChip) {
        val context = binding.root.context
        addView(
            Chip(context).apply {
                text = chipData.displayText

                chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.gray50)
                chipStrokeColor = ContextCompat.getColorStateList(context, R.color.gray200)
                chipStrokeWidth = 1.dp(context).toFloat()

                chipStartPadding = 4.dp(context).toFloat()
                chipEndPadding = 4.dp(context).toFloat()

                chipSpacingHorizontal = 6.dp(context)
                chipSpacingVertical = (-8).dp(context)

                if (chipData is ProfileChip.Link) {
                    chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_link)
                    chipIconSize = 14.dp(context).toFloat()
                    iconEndPadding = -4.dp(context).toFloat()

                    chipStartPadding = 10.dp(context).toFloat()
                    chipEndPadding = 10.dp(context).toFloat()
                }

                setTextAppearanceResource(R.style.TextAppearance_Mashup_Caption1_13_M)
                setOnClickListener {
                    if (chipData.clickable) {
                        listener?.onStartExternalLink(chipData.link)
                    }
                }
            }
        )
    }

    sealed class ProfileChip(
        open val displayText: String,
        open val link: String,
        val clickable: Boolean
    ) {
        data class Text(
            override val displayText: String
        ) : ProfileChip(displayText, "", false)

        data class Link(
            override val displayText: String,
            override val link: String
        ) : ProfileChip(displayText, link, true)

        data class Instagram(
            override val displayText: String
        ) : ProfileChip(displayText, "https://www.instagram.com/$displayText", true)
    }
}
