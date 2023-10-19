package com.michaelflisar.composedialogs.dialogs.color.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedialogs.dialogs.color.DialogColorUtil
import kotlin.math.ceil

@Composable
internal fun ColorGrid(
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
                    val onColor = DialogColorUtil.getBestTextColor(colorWithAlpha)
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
                            DialogColorUtil.drawCheckerboard(this, density)
                        }
                        if (color == Color.Black && level == 0) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape)
                            ) {
                                DialogColorUtil.drawBlackWhite(this, selectedAlpha)
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(colorWithAlpha, shape)
                            )
                            val label = labels?.getOrNull(index)
                            if (label != null && !selected) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = onColor
                                )
                            }
                        }
                        if (selected) {
                            Icon(
                                imageVector = Icons.Default.Check,
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