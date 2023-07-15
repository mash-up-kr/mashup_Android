package com.mashup.core.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.mashup.core.common.R
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.SubTitle2

@Composable
fun MashUpToolbar(
    modifier: Modifier = Modifier,
    title: String,
    showBackButton: Boolean = false,
    showActionButton: Boolean = false,
    showBottomDivider: Boolean = false,
    onClickBackButton: () -> Unit = {},
    onClickActionButton: () -> Unit = {},
    @DrawableRes actionButtonDrawableRes: Int = R.drawable.ic_close,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    painter = painterResource(id = R.drawable.ic_chevron_left),
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

            if (showActionButton) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .noRippleClickable { onClickActionButton() }
                        .padding(8.dp),
                    painter = painterResource(id = actionButtonDrawableRes),
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }
        }

        if (showBottomDivider) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Gray100)
            )
        }
    }
}

@Preview
@Composable
private fun MashupToolbarOnlyTitlePrev() {
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
private fun MashupToolbarIncludeBackButtonPrev() {
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
                showActionButton = true
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
                showActionButton = true,
                showBackButton = true
            )
        }
    }
}

@Preview("divider를 포함한 toolbar")
@Composable
fun MashupToolbarIncludeDividerPrev() {
    MashUpTheme {
        Surface {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "테스트",
                showBottomDivider = true
            )
        }
    }
}

@Preview("actionButton 아이콘을 변경한 toolbar")
@Composable
fun MashupToolbarActionButtonPrev() {
    MashUpTheme {
        Surface {
            MashUpToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = "테스트",
                showActionButton = true,
                actionButtonDrawableRes = R.drawable.ic_info
            )
        }
    }
}
