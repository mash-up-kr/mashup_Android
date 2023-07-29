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
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.core.common.utils.safeShow
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpBottomPopupScreen
import com.mashup.core.ui.widget.MashUpBottomPopupUiState
import com.mashup.core.ui.widget.MashUpPopupEntity
import com.mashup.feature.danggn.reward.model.DanggnPopupType

class DanggnFirstPlaceBottomPopup : BottomSheetDialogFragment() {

    private val bottomPopupViewModel: DanggnBottomPopupViewModel by viewModels()

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomPopupViewModel.patchPopupViewed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val entity = MashUpPopupEntity(
            title = arguments?.getString(EXTRA_POPUP_TITLE).orEmpty(),
            description = arguments?.getString(EXTRA_POPUP_DESCRIPTION).orEmpty(),
            imageResName = arguments?.getString(EXTRA_POPUP_IMG_RES_NAME).orEmpty(),
            leftButtonText = arguments?.getString(EXTRA_POPUP_LEFT_BUTTON_TEXT).orEmpty(),
            rightButtonText = arguments?.getString(EXTRA_POPUP_RIGHT_BUTTON_TEXT).orEmpty(),
        )

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MashUpTheme {
                    MashUpBottomPopupScreen(
                        uiState = MashUpBottomPopupUiState.Success(entity),
                        onClickLeftButton = {
                            showRewardInformationDialog()
                        },
                        onClickRightButton = {
                            DanggnRewardPopup.getNewInstance().safeShow(parentFragmentManager)
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

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        showRewardInformationDialog()
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
            setTitle(text = "다음에 공지하시겠어요?")
            setMessage(text = "다음 회차 랭킹이 시작되면 공지 등록권이 소멸되니 기간 안에 꼭 등록해주세요!")
            setPositiveButton("네") {
                this@DanggnFirstPlaceBottomPopup.dismiss()
            }
            setNegativeButton("아니오")
            show()
        }
    }

    companion object {
        private const val EXTRA_POPUP_KEY = "EXTRA_POPUP_KEY"
        private const val EXTRA_POPUP_TITLE = "EXTRA_POPUP_TITLE"
        private const val EXTRA_POPUP_DESCRIPTION = "EXTRA_POPUP_DESCRIPTION"
        private const val EXTRA_POPUP_IMG_RES_NAME = "EXTRA_POPUP_IMG_RES_NAME"
        private const val EXTRA_POPUP_LEFT_BUTTON_TEXT = "EXTRA_POPUP_LEFT_BUTTON_TEXT"
        private const val EXTRA_POPUP_RIGHT_BUTTON_TEXT = "EXTRA_POPUP_RIGHT_BUTTON_TEXT"

        fun getNewInstance(entity: MashUpPopupEntity): DanggnFirstPlaceBottomPopup =
            DanggnFirstPlaceBottomPopup().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_POPUP_KEY, DanggnPopupType.DANGGN_REWARD.name)

                    putString(EXTRA_POPUP_TITLE, entity.title)
                    putString(EXTRA_POPUP_DESCRIPTION, entity.description)
                    putString(EXTRA_POPUP_IMG_RES_NAME, entity.imageResName)
                    putString(EXTRA_POPUP_LEFT_BUTTON_TEXT, entity.leftButtonText)
                    putString(EXTRA_POPUP_RIGHT_BUTTON_TEXT, entity.rightButtonText)
                }
            }
    }
}
