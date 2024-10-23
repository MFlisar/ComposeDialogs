package com.michaelflisar.composedialogs.dialogs.color.classes

import dev.icerock.moko.parcelize.IgnoredOnParcel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Parcelize
internal class GroupedColor(
    private val indexOfMainColor: Int,
    val colors: List<Color>
) : IColor, Parcelable {

    override val color: Int
        get() = colors[indexOfMainColor].color

    @IgnoredOnParcel
    override val label = ""
}