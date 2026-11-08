package com.michaelflisar.composedialogs.dialogs.color.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

internal fun drawCheckerboard(drawScope: DrawScope, density: Density) {
    val pixelSize = with(density) { 4.dp.toPx().toInt() }
    val color1 = Color(0xFFC2C2C2)
    val color2 = Color(0xFFF3F3F3)
    val sizePixel = Size(pixelSize.toFloat(), pixelSize.toFloat())
    for (column in 0 until drawScope.size.width.toInt() step pixelSize) {
        for (row in 0 until drawScope.size.height.toInt() step pixelSize) {
            val color = if ((column / pixelSize + row / pixelSize) % 2 == 0) color1 else color2
            drawScope.drawRect(
                color,
                topLeft = Offset(column.toFloat(), row.toFloat()),
                size = sizePixel
            )
        }
    }
}

internal fun drawBlackWhite(drawScope: DrawScope, alpha: Float) {
    drawScope.drawRect(
        Color.Black.copy(alpha = alpha),
        size = Size(drawScope.size.width / 2, drawScope.size.height)
    )
    drawScope.drawRect(
        Color.White.copy(alpha = alpha),
        Offset(x = drawScope.size.width / 2, y = 0f),
        size = Size(drawScope.size.width / 2, drawScope.size.height)
    )
}

internal fun getBestTextColor(background: Color): Color {
    if (background.alpha <= 0.4f) {
        return Color.Black
    }
    val darkness = 1.0 - (
        0.299 * background.red +
            0.587 * background.green +
            0.114 * background.blue
        )
    return if (darkness > 0.2) Color.White else Color.Black
}

internal fun parseColor(color: String, alphaSupported: Boolean): Color? {
    val hex = color.removePrefix("#")
    val validLength = hex.length == 6 || (alphaSupported && hex.length == 8)
    if (!validLength) {
        return null
    }
    return try {
        val argb = hex.toLong(16)
        val colorArgb = if (alphaSupported && hex.length == 8) argb else argb or 0xFF000000
        Color(colorArgb.toInt())
    } catch (_: NumberFormatException) {
        null
    }
}
