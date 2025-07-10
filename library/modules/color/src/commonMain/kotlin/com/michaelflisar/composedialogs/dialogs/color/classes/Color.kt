package com.michaelflisar.composedialogs.dialogs.color.classes

import com.michaelflisar.parcelize.Parcelable
import com.michaelflisar.parcelize.Parcelize

@Parcelize
internal class Color(
    override val color: Int,
    override val label: String
) : IColor, Parcelable