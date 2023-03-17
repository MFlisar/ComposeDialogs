package com.michaelflisar.composedialogs.dialogs.color.classes

internal class GroupedColor(
    private val indexOfMainColor: Int,
    val colors: List<Color>
) : IColor {

    override val color: Int
        get() = colors[indexOfMainColor].color

    override val label = ""
}