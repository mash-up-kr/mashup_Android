package com.mashup.core.ui.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand500

@Composable
fun MashUpSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    enabled: Boolean = true,
) {
    val trackBackgroundColor by animateColorAsState(
        targetValue = if (checked) Brand500 else Color(0xFFD9D9D9)
    )
    val trackWidthDp = 37.dp
    val trackHeightDp = 24.dp
    val thumbRadiusDp = 10.dp
    val thumbPaddingDp = 2.dp

    val animateThumbPosition = animateFloatAsState(
        targetValue = with(LocalDensity.current) {
            if (checked) {
                (trackWidthDp - thumbRadiusDp - thumbPaddingDp).toPx()
            } else {
                (thumbRadiusDp + thumbPaddingDp).toPx()
            }
        }
    )

    Canvas(
        modifier = modifier
            .semantics {
                contentDescription = "MashUpSwitch"
            }
            .size(width = trackWidthDp, height = trackHeightDp)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = MutableInteractionSource(),
                indication = null
            )
    ) {
        drawRoundRect(
            color = trackBackgroundColor,
            cornerRadius = CornerRadius(x = 12.dp.toPx(), y = 12.dp.toPx()),
        )

        drawCircle(
            color = Color.White,
            radius = thumbRadiusDp.toPx(),
            center = Offset(
                x = animateThumbPosition.value,
                y = size.height / 2
            )
        )
    }
}

@Preview
@Composable
fun SwitchCheckedPrev() {
    var state by remember { mutableStateOf(true) }
    MashUpSwitch(
        checked = state,
        onCheckedChange = { isChecked -> state = isChecked }
    )
}