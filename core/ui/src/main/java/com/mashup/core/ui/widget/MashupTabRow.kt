package com.mashup.core.ui.widget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMaxBy
import androidx.compose.ui.util.fastSumBy

@Composable
fun MashUpTabRow(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = {},
    horizontalSpace: Dp = 0.dp,
    tabs: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.selectableGroup()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        color = backgroundColor
    ) {
        SubcomposeLayout { constraints ->
            val tabMeasurables = subcompose(TabSlots.Tabs, tabs)
            val tabPlaceables = tabMeasurables.map {
                it.measure(constraints)
            }
            val tabCount = tabMeasurables.size
            val tabPositions = List(tabCount) { index ->
                TabPosition(
                    (tabPlaceables.take(index)
                        .fastSumBy { it.width } + horizontalSpace.toPx() * index).toDp(),
                    tabPlaceables[index].width.toDp()
                )
            }

            val tabRowHeight = (tabPlaceables.fastMaxBy { it.height }?.height ?: 0) + spaceOfIndicatorBetweenTab.toPx().toInt()
            val tabRowWidth =
                (tabPlaceables.fastSumBy { it.width } + (horizontalSpace.toPx() * (tabCount - 1))).toInt()

            layout(tabRowWidth, tabRowHeight) {
                tabPlaceables.fastForEachIndexed { index, placeable ->
                    placeable.place(tabPositions[index].left.toPx().toInt(), 0)
                }

                subcompose(TabSlots.Indicator) {
                    indicator(tabPositions)
                }.fastForEach {
                    it.measure(Constraints.fixed(tabRowWidth, tabRowHeight)).place(0, 0)
                }
            }
        }
    }
}

fun Modifier.mashupTabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier = composed {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )

    wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

private enum class TabSlots {
    Tabs,
    Indicator
}

data class TabPosition(
    val left: Dp,
    val width: Dp
)

/**
 * indicator 사이 패딩 + indicator 높이
 */
private val spaceOfIndicatorBetweenTab = 5.dp