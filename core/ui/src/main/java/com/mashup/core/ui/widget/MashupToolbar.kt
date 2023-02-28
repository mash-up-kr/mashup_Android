package com.mashup.core.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.SubTitle2
import com.mashup.core.common.R as CR

@Composable
fun MashUpToolbar(
    modifier: Modifier = Modifier,
    title: String,
    showBackButton: Boolean = false,
    showCloseButton: Boolean = false,
    onClickBackButton: () -> Unit = {},
    onClickCloseButton: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable { onClickBackButton() }
                    .padding(8.dp),
                painter = painterResource(id = CR.drawable.ic_chevron_left),
                contentDescription = null,
                tint = Gray800
            )
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }

        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            style = SubTitle2,
            color = Gray800,
            textAlign = TextAlign.Center
        )

        if (showCloseButton) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .noRippleClickable { onClickCloseButton() }
                    .padding(8.dp),
                painter = painterResource(id = CR.drawable.ic_close),
                contentDescription = null
            )
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }
    }
}

@Preview
@Composable
fun MashupToolbarOnlyTitlePrev() {
    MashUpTheme {
        Surface {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "테스트"
            )
        }
    }
}

@Preview("back Button을 포함한 toolbar")
@Composable
fun MashupToolbarIncludeBackButtonPrev() {
    MashUpTheme {
        Surface {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "테스트",
                showBackButton = true
            )
        }
    }
}

@Preview("close Button을 포함한 toolbar")
@Composable
fun MashupToolbarIncludeCloseButtonPrev() {
    MashUpTheme {
        Surface {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "테스트",
                showCloseButton = true
            )
        }
    }
}

@Preview("back, close Button을 포함한 toolbar")
@Composable
fun MashupToolbarIncludeAllButtonPrev() {
    MashUpTheme {
        Surface {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "테스트",
                showCloseButton = true,
                showBackButton = true
            )
        }
    }
}