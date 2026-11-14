package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

internal fun getNearestColorGroupIndex(color: Color): Int {
    val blackAndWhiteIndex = ColorDefinitions.COLORS.indexOf(ColorDefinitions.COLORS_BW)
    if (color.red == color.green && color.red == color.blue) {
        return blackAndWhiteIndex
    }

    val solidColor = color.copy(alpha = 1f)
    var bestMatchIndex = blackAndWhiteIndex
    var minDiff: Double? = null
    ColorDefinitions.COLORS.forEachIndexed { index, group ->
        val diff = calcColorDifference(solidColor, group.mainColor)
        if (minDiff == null || minDiff > diff) {
            minDiff = diff
            bestMatchIndex = index
        }
    }
    return bestMatchIndex
}

private fun calcColorDifference(first: Color, second: Color): Double {
    val diffRed = abs(first.red - second.red).toDouble()
    val diffGreen = abs(first.green - second.green).toDouble()
    val diffBlue = abs(first.blue - second.blue).toDouble()
    return (diffRed + diffGreen + diffBlue) / 3f
}
