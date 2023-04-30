package com.mashup.ui.main.popup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton

class MainBottomPopup : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        }
    }
}

@Composable
fun MainBottomPopupScreen(
    onClickLeftButton: () -> Unit = {},
    onClickRightButton: () -> Unit = {}
) {
    MainBottomPopupContent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}


@Composable
fun MainBottomPopupContent(
    title: String,
    description: String,
    @DrawableRes imageRes: Int,
    leftButtonText: String,
    rightButtonText: String,
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
            modifier = Modifier.width(24.dp)
                .padding(vertical = 10.dp),
            color = Color(0xFFD9D9D9),
            thickness = 1.dp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 20.dp),
            text = title,
            style = SubTitle1,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            text = description,
            style = SubTitle1,
            textAlign = TextAlign.Center
        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            painter = painterResource(id = imageRes),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MashUpButton(
                text = leftButtonText,
                buttonStyle = ButtonStyle.INVERSE,
                onClick = onClickLeftButton
            )

            MashUpButton(
                text = rightButtonText,
                buttonStyle = ButtonStyle.INVERSE,
                onClick = onClickRightButton
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}