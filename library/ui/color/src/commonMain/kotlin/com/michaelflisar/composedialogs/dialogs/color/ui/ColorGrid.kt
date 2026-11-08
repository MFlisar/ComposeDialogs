package com.michaelflisar.composedialogs.dialogs.color.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerIcon
import com.michaelflisar.composedialogs.dialogs.color.ColorTextStyle
import com.michaelflisar.composedialogs.dialogs.color.ColorUIProvider
import com.michaelflisar.composedialogs.dialogs.color.utils.drawBlackWhite
import com.michaelflisar.composedialogs.dialogs.color.utils.drawCheckerboard
import com.michaelflisar.composedialogs.dialogs.color.utils.getBestTextColor
import kotlin.math.ceil

@Composable
internal fun ColorGrid(
    uiProvider: ColorUIProvider,
    level: Int,
    colors: List<Color>,
    labels: List<String>?,
    selectedAlpha: Float,
    selectedColor: Color?,
    gridSize: Int,
    shape: Shape,
    space: Dp,
    size: Dp,
    density: Density,
    onClick: (index: Int) -> Unit
) {
    val ui = uiProvider
    val rows = ceil(colors.size.toFloat() / gridSize.toFloat()).toInt()
    for (r in 0 until rows) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space)
        ) {
            for (c in 0 until gridSize) {
                val index = r * gridSize + c
                val color = colors.getOrNull(index)

                if (color == null) {
                    Spacer(
                        modifier = Modifier
                            .height(size)
                            .weight(1f)
                    )
                } else {
                    val colorWithAlpha = color.copy(alpha = selectedAlpha)
                    val onColor = getBestTextColor(colorWithAlpha)
                    val selected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .height(size)
                            .weight(1f)
                            .background(colorWithAlpha, shape)
                            .clip(shape)
                            .clickable {
                                onClick(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape)
                        ) {
                            drawCheckerboard(this, density)
                        }
                        if (color == Color.Black && level == 0) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape)
                            ) {
                                drawBlackWhite(this, selectedAlpha)
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(colorWithAlpha, shape)
                            )
                            val label = labels?.getOrNull(index)
                            if (label != null && !selected) {
                                ui.Text(
                                    text = label,
                                    modifier = Modifier,
                                    style = ColorTextStyle.BodySmall,
                                    color = onColor,
                                    fontWeight = null
                                )
                            }
                        }
                        if (selected) {
                            ui.Icon(
                                icon = ColorPickerIcon.Check,
                                contentDescription = "",
                                tint = onColor
                            )
                        }
                    }
                }
            }
        }
    }
}