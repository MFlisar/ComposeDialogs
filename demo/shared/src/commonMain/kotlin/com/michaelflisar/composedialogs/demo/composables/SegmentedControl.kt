package com.michaelflisar.composedialogs.demo.composables

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SegmentedControl(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedIndex: Int = 0,
    useFixedWidth: Boolean = false,
    itemWidth: Dp = 120.dp,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    color: Color = MaterialTheme.colorScheme.primary,
    colorOnColor: Color = MaterialTheme.colorScheme.onPrimary,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableIntStateOf(selectedIndex) }

    val cornerSize0 = CornerSize(0)

    Row(
        modifier = modifier
    ) {
        items.forEachIndexed { index, item ->

            val keepCornersStart = index == 0
            val keepCornersEnd = index == items.size - 1
            val shapeOfIndex = if (keepCornersStart && keepCornersEnd) {
                shape
            } else if (keepCornersStart) {
                shape.copy(topEnd = cornerSize0, bottomEnd = cornerSize0)
            } else if (keepCornersEnd) {
                shape.copy(topStart = cornerSize0, bottomStart = cornerSize0)
            } else shape.copy(all = cornerSize0)

            val transitionState = remember {
                MutableTransitionState(selectedIndex).apply {
                    targetState = selectedIndex
                }
            }
            val transition = rememberTransition(transitionState, label = "transition")
            val colorBackground by transition.animateColorTween("background") {
                if (it.value == index) color else Color.Transparent
            }
            val colorBorder by transition.animateColorTween("border") {
                if (it.value == index) color else color.copy(alpha = 0.75f)
            }
            val colorForeground by transition.animateColorTween("background") {
                if (it.value == index) colorOnColor else color.copy(alpha = 0.9f)
            }

            OutlinedButton(
                modifier = when (index) {
                    0 -> {
                        if (useFixedWidth) {
                            Modifier
                                .width(itemWidth)
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.intValue == index) 1f else 0f)
                        } else {
                            Modifier
                                .wrapContentSize()
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.intValue == index) 1f else 0f)
                        }
                    }

                    else -> {
                        if (useFixedWidth)
                            Modifier
                                .width(itemWidth)
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.intValue == index) 1f else 0f)
                        else Modifier
                            .wrapContentSize()
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedIndex.intValue == index) 1f else 0f)
                    }
                },
                onClick = {
                    selectedIndex.intValue = index
                    onItemSelection(selectedIndex.intValue)
                },
                shape = shapeOfIndex,
                border = BorderStroke(1.dp, colorBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colorBackground
                )
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Normal,
                    color = colorForeground
                )
            }
        }
    }
}