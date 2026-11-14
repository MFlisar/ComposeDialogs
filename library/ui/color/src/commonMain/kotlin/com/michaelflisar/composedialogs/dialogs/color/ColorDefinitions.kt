package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.michaelflisar.composecolors.ColorGroup
import com.michaelflisar.composecolors.material.MaterialColor
import com.michaelflisar.composecolors.material.palette.*

internal data class ColorPickerPreset(
    val color: Color,
    val label: String
)

internal data class ColorPickerPresetGroup(
    val mainColorIndex: Int,
    val colors: List<ColorPickerPreset>
) {
    val mainColor: Color
        get() = colors[mainColorIndex].color
}

internal object ColorDefinitions {

    private fun ColorGroup.asPickerGroup(indexOfMainColor: Int) = ColorPickerPresetGroup(
        indexOfMainColor,
        colors.map {
            ColorPickerPreset(Color(it.color.toArgb()), it.name)
        }
    )

    val COLORS_BW = MaterialColor.BW.asPickerGroup(0)
    private val COLORS_AMBER = MaterialColor.Amber.asPickerGroup(5)
    private val COLORS_BLUE = MaterialColor.Blue.asPickerGroup(5)
    private val COLORS_BLUE_GREY = MaterialColor.BlueGray.asPickerGroup(5)
    private val COLORS_BROWN = MaterialColor.Brown.asPickerGroup(5)
    private val COLORS_CYAN = MaterialColor.Cyan.asPickerGroup(5)
    private val COLORS_DEEP_ORANGE = MaterialColor.DeepOrange.asPickerGroup(5)
    private val COLORS_DEEP_PURPLE = MaterialColor.DeepPurple.asPickerGroup(5)
    private val COLORS_GREEN = MaterialColor.Green.asPickerGroup(5)
    private val COLORS_GRAY = MaterialColor.Gray.asPickerGroup(5)
    private val COLORS_INDIGO = MaterialColor.Indigo.asPickerGroup(5)
    private val COLORS_LIGHT_BLUE = MaterialColor.LightBlue.asPickerGroup(5)
    private val COLORS_LIGHT_GREEN = MaterialColor.LightGreen.asPickerGroup(5)
    private val COLORS_LIME = MaterialColor.Lime.asPickerGroup(5)
    private val COLORS_ORANGE = MaterialColor.Orange.asPickerGroup(5)
    private val COLORS_PINK = MaterialColor.Pink.asPickerGroup(5)
    private val COLORS_PURPLE = MaterialColor.Purple.asPickerGroup(5)
    private val COLORS_RED = MaterialColor.Red.asPickerGroup(5)
    private val COLORS_TEAL = MaterialColor.Teal.asPickerGroup(5)
    private val COLORS_YELLOW = MaterialColor.Yellow.asPickerGroup(5)

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
