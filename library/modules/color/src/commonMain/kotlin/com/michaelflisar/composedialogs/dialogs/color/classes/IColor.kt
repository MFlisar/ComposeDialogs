package com.michaelflisar.composedialogs.dialogs.color.classes

internal sealed interface IColor {

    val color: Int
    val label: String

    fun getColor() = androidx.compose.ui.graphics.Color(color)
}