package com.mashup.feature.danggn.reward

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.core.common.utils.safeShow
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpBottomPopupScreen
import com.mashup.core.ui.widget.MashUpBottomPopupUiState
import com.mashup.core.ui.widget.MashUpPopupEntity

class DanggnFirstPlaceBottomPopup : BottomSheetDialogFragment() {

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lastRound = arguments?.getInt(KEY_LAST_ROUND)

        // TODO: storage api로 받아오기
        val entity = MashUpPopupEntity(
            title = "당근 흔들기 ${lastRound}회차 랭킹 1위를 축하합니다!\n" +
                    "리워드로 전체 공지 작성 기회가 생겼어요!",
            description = "다음 랭킹 시작 전까지 공지를 작성해서\n모두에게 한 마디 할 수 있는 기회를 놓치지 마세요!",
            imageResName = "img_carrot_reward_bottom_popup",
            leftButtonText = "다음에",
            rightButtonText = "한 마디 공지하기"
        )

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MashUpTheme {
                    MashUpBottomPopupScreen(
                        uiState = MashUpBottomPopupUiState.Success(entity),
                        onClickLeftButton = {
                            showRewardInformationDialog()
                            dismiss()
                        },
                        onClickRightButton = {
                            DanggnRewardPopup.getNewInstance(onClickSubmitButton = {}).safeShow(parentFragmentManager)
                            dismiss()
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.run {
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
            }

        addGlobalLayoutListener(view)
    }

    private fun addGlobalLayoutListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (this@DanggnFirstPlaceBottomPopup.view?.height == 0) return
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                behavior?.peekHeight = this@DanggnFirstPlaceBottomPopup.view?.height ?: 0
            }
        })
    }

    private fun showRewardInformationDialog() {
        CommonDialog(requireContext()).apply {
            setTitle(text = "다음 회차 랭킹이 시작되기 전까지 언제든 공지를 작성할 수 있어요!")
            setMessage(text = "다음 회차 랭킹이 시작되면 공지 등록권이 소멸되니 기간 안에 꼭 등록해주세요!")
            setPositiveButton("확인")
            show()
        }
    }

    companion object {
        private const val KEY_LAST_ROUND = "KEY_LAST_ROUND"

        fun getNewInstance(lastRound: Int): DanggnFirstPlaceBottomPopup {
            return DanggnFirstPlaceBottomPopup().apply {
                arguments = Bundle().apply {
                    putInt(KEY_LAST_ROUND, lastRound)
                }
            }
        }
    }
}
