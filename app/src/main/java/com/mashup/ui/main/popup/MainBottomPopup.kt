package com.mashup.ui.main.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.constant.EXTRA_POPUP_KEY
import com.mashup.core.common.utils.getDrawableResIdByName
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.ui.main.model.MainPopupEntity
import com.mashup.ui.main.model.MainPopupType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainBottomPopup : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(popupType: MainPopupType) = MainBottomPopup().apply {
            arguments = bundleOf(
                EXTRA_POPUP_KEY to popupType.name
            )
        }
    }


    private val viewModel: MainBottomPopupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MashUpTheme {
                    MainBottomPopupScreen(
                        viewModel = viewModel,
                        onClickLeftButton = {
                            dismiss()
                        },
                        onClickRightButton = {
                            dismiss()
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.run {
                post {
                    // post 안하면 작동 안됨
                    BottomSheetBehavior.from(this).apply {
                        peekHeight = 0
                        state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
            }
    }
}

@Composable
fun MainBottomPopupScreen(
    viewModel: MainBottomPopupViewModel,
    onClickLeftButton: () -> Unit = {},
    onClickRightButton: () -> Unit = {}
) {
    val uiState: MainBottomPopupUiState by viewModel.uiState

    (uiState as? MainBottomPopupUiState.Success)?.let {
        MainBottomPopupContent(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            mainPopupEntity = it.mainPopupEntity,
            onClickLeftButton = onClickLeftButton,
            onClickRightButton = onClickRightButton
        )
    }
}


@Composable
fun MainBottomPopupContent(
    mainPopupEntity: MainPopupEntity,
    modifier: Modifier = Modifier,
    onClickLeftButton: () -> Unit = {},
    onClickRightButton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(
                color = White,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Divider(
            modifier = Modifier
                .width(24.dp)
                .align(CenterHorizontally)
                .padding(vertical = 10.dp),
            color = Color(0xFFD9D9D9),
            thickness = 3.dp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 20.dp),
            text = mainPopupEntity.title,
            style = SubTitle1,
            color = Gray950,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            text = mainPopupEntity.description,
            style = Body4,
            color = Gray500,
            textAlign = TextAlign.Center
        )

        val context = LocalContext.current
        val imageResId = remember(mainPopupEntity.imageResName) {
            context.getDrawableResIdByName(mainPopupEntity.imageResName)
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            painter = painterResource(id = imageResId),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MashUpButton(
                modifier = Modifier.wrapContentWidth(),
                text = mainPopupEntity.leftButtonText,
                buttonStyle = ButtonStyle.INVERSE,
                onClick = onClickLeftButton
            )

            MashUpButton(
                modifier = Modifier.weight(1f),
                text = mainPopupEntity.rightButtonText,
                buttonStyle = ButtonStyle.PRIMARY,
                onClick = onClickRightButton
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}