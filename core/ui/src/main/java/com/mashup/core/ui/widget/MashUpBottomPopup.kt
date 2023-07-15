package com.mashup.core.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mashup.core.common.utils.getDrawableResIdByName
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.SubTitle1

@Composable
fun MashUpBottomPopupScreen(
    uiState: MashUpBottomPopupUiState,
    onClickLeftButton: () -> Unit = {},
    onClickRightButton: () -> Unit = {}
) {
    (uiState as? MashUpBottomPopupUiState.Success)?.let {
        MashUpBottomPopupContent(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            popupEntity = it.popupEntity,
            onClickLeftButton = onClickLeftButton,
            onClickRightButton = onClickRightButton
        )
    }
}


@Composable
fun MashUpBottomPopupContent(
    popupEntity: MashUpPopupEntity,
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
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp),
            color = Color(0xFFD9D9D9),
            thickness = 3.dp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 20.dp),
            text = popupEntity.title,
            style = SubTitle1,
            color = Gray950,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            text = popupEntity.description,
            style = Body4,
            color = Gray500,
            textAlign = TextAlign.Center
        )

        val context = LocalContext.current
        val imageResId = remember(popupEntity.imageResName) {
            context.getDrawableResIdByName(popupEntity.imageResName)
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
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MashUpButton(
                modifier = Modifier.wrapContentWidth(),
                text = popupEntity.leftButtonText,
                buttonStyle = ButtonStyle.INVERSE,
                onClick = onClickLeftButton
            )

            MashUpButton(
                modifier = Modifier.weight(1f),
                text = popupEntity.rightButtonText,
                buttonStyle = ButtonStyle.PRIMARY,
                onClick = onClickRightButton
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

sealed interface MashUpBottomPopupUiState {
    object Loading : MashUpBottomPopupUiState

    data class Success(
        val popupEntity: MashUpPopupEntity
    ) : MashUpBottomPopupUiState
}

data class MashUpPopupEntity(
    val title: String,
    val description: String,
    val imageResName: String,
    val leftButtonText: String,
    val rightButtonText: String,
)
