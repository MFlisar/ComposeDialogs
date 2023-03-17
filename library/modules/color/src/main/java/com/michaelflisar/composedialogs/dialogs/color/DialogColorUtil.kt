package com.michaelflisar.composedialogs.dialogs.color

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorDefinitions
import com.michaelflisar.composedialogs.dialogs.color.classes.GroupedColor

internal object DialogColorUtil {

    fun drawCheckerboard(drawScope: DrawScope, pixelSize: Int) {
        val color1 = Color(0xFFC2C2C2)
        val color2 = Color(0xFFF3F3F3)
        val sizePixel = Size(pixelSize.toFloat(), pixelSize.toFloat())
        for (c in 0 until drawScope.size.width.toInt() step pixelSize) {
            for (r in 0 until drawScope.size.height.toInt() step pixelSize) {
                val color = if ((c / pixelSize + r / pixelSize) % 2 == 0) color1 else color2
                drawScope.drawRect(color, topLeft = Offset(c.toFloat(), r.toFloat()), sizePixel)
            }
        }
    }

    fun drawBlackWhite(drawScope: DrawScope, alpha: Float) {
        drawScope.drawRect(Color.Black.copy(alpha = alpha), size = Size(drawScope.size.width / 2, drawScope.size.height))
        drawScope.drawRect(Color.White.copy(alpha = alpha), Offset(x = drawScope.size.width / 2, y = 0f), size = Size(drawScope.size.width / 2, drawScope.size.height))
    }

    fun getNearestColorGroup(context: Context, color: Color): GroupedColor {
        val solidColor = color.copy(alpha = 1f)
        var bestMatch = ColorDefinitions.COLORS_BW
        var minDiff: Double? = null
        for (c in ColorDefinitions.COLORS) {
            val isMono = color.red == color.green && color.red == color.blue
            if (isMono) {
                return ColorDefinitions.COLORS_BW
            }
            val diff = calcColorDifference(solidColor, c.getColor(context))
            if (minDiff == null || minDiff > diff) {
                minDiff = diff
                bestMatch = c
            }
        }
        return bestMatch
    }

    fun getBestTextColor(background: Color): Color {
        if (background.alpha <= 0.4f) {
            return Color.Black
        }
        return if (getDarknessFactor(background) > 0.2f) {
            Color.White
        } else {
            Color.Black
        }
    }

    fun getDarknessFactor(color: Color): Double {
        return 1.0 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    }

    fun calcColorDifference(c1: Color, c2: Color): Double {
        val r1 = c1.red
        val g1 = c1.green
        val b1 = c1.blue
        val r2 = c2.red
        val g2 = c2.green
        val b2 = c2.blue
        val diffRed = Math.abs(r1 - r2)
        val diffGreen = Math.abs(g1 - g2)
        val diffBlue = Math.abs(b1 - b2)
        val pctDiffRed = diffRed.toDouble()
        val pctDiffGreen = diffGreen.toDouble()
        val pctDiffBlue = diffBlue.toDouble()
        return (pctDiffRed + pctDiffGreen + pctDiffBlue) / 3f
    }
}