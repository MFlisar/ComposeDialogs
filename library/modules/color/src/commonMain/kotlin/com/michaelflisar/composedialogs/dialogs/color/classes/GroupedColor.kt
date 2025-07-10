package com.michaelflisar.composedialogs.dialogs.color.classes

import com.michaelflisar.parcelize.IgnoredOnParcel
import com.michaelflisar.parcelize.Parcelable
import com.michaelflisar.parcelize.Parcelize

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