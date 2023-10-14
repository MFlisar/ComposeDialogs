package com.michaelflisar.composedialogs.dialogs.color.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal class GroupedColor(
    private val indexOfMainColor: Int,
    val colors: List<Color>
) : IColor, Parcelable {

    override val color: Int
        get() = colors[indexOfMainColor].color

    override val label = ""
}