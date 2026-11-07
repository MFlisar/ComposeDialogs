package com.michaelflisar.composedialogs.dialogs.color.classes

import androidx.compose.ui.graphics.toArgb
import com.michaelflisar.composecolors.ColorGroup
import com.michaelflisar.composecolors.material.MaterialColor
import com.michaelflisar.composecolors.material.palette.Amber
import com.michaelflisar.composecolors.material.palette.BW
import com.michaelflisar.composecolors.material.palette.Blue
import com.michaelflisar.composecolors.material.palette.BlueGray
import com.michaelflisar.composecolors.material.palette.Brown
import com.michaelflisar.composecolors.material.palette.Cyan
import com.michaelflisar.composecolors.material.palette.DeepOrange
import com.michaelflisar.composecolors.material.palette.DeepPurple
import com.michaelflisar.composecolors.material.palette.Gray
import com.michaelflisar.composecolors.material.palette.Green
import com.michaelflisar.composecolors.material.palette.Indigo
import com.michaelflisar.composecolors.material.palette.LightBlue
import com.michaelflisar.composecolors.material.palette.LightGreen
import com.michaelflisar.composecolors.material.palette.Lime
import com.michaelflisar.composecolors.material.palette.Orange
import com.michaelflisar.composecolors.material.palette.Pink
import com.michaelflisar.composecolors.material.palette.Purple
import com.michaelflisar.composecolors.material.palette.Red
import com.michaelflisar.composecolors.material.palette.Teal
import com.michaelflisar.composecolors.material.palette.Yellow

internal object ColorDefinitions {

    private fun ColorGroup.asGroupedColor(indexOfMainColor: Int) = GroupedColor(
        indexOfMainColor,
        colors.map {
            Color(it.color.toArgb(), it.name)
        }
    )

    val COLORS_BW = MaterialColor.BW.asGroupedColor(0)
    val COLORS_AMBER = MaterialColor.Amber.asGroupedColor(5)
    val COLORS_BLUE = MaterialColor.Blue.asGroupedColor(5)
    val COLORS_BLUE_GREY = MaterialColor.BlueGray.asGroupedColor(5)
    val COLORS_BROWN = MaterialColor.Brown.asGroupedColor(5)
    val COLORS_CYAN = MaterialColor.Cyan.asGroupedColor(5)
    val COLORS_DEEP_ORANGE = MaterialColor.DeepOrange.asGroupedColor(5)
    val COLORS_DEEP_PURPLE = MaterialColor.DeepPurple.asGroupedColor(5)
    val COLORS_GREEN = MaterialColor.Green.asGroupedColor(5)
    val COLORS_GRAY = MaterialColor.Gray.asGroupedColor(5)
    val COLORS_INDIGO = MaterialColor.Indigo.asGroupedColor(5)
    val COLORS_LIGHT_BLUE = MaterialColor.LightBlue.asGroupedColor(5)
    val COLORS_LIGHT_GREEN = MaterialColor.LightGreen.asGroupedColor(5)
    val COLORS_LIME = MaterialColor.Lime.asGroupedColor(5)
    val COLORS_ORANGE = MaterialColor.Orange.asGroupedColor(5)
    val COLORS_PINK = MaterialColor.Pink.asGroupedColor(5)
    val COLORS_PURPLE = MaterialColor.Purple.asGroupedColor(5)
    val COLORS_RED = MaterialColor.Red.asGroupedColor(5)
    val COLORS_TEAL = MaterialColor.Teal.asGroupedColor(5)
    val COLORS_YELLOW = MaterialColor.Yellow.asGroupedColor(5)

    val COLORS = listOf(
        COLORS_RED,
        COLORS_PINK,
        COLORS_PURPLE,
        COLORS_DEEP_PURPLE,
        COLORS_INDIGO,
        COLORS_BLUE,
        COLORS_LIGHT_BLUE,
        COLORS_CYAN,
        COLORS_TEAL,
        COLORS_GREEN,
        COLORS_LIGHT_GREEN,
        COLORS_LIME,
        COLORS_YELLOW,
        COLORS_AMBER,
        COLORS_ORANGE,
        COLORS_DEEP_ORANGE,
        COLORS_BROWN,
        COLORS_GRAY,
        COLORS_BLUE_GREY,
        COLORS_BW
    )
}