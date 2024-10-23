package com.michaelflisar.composedialogs.dialogs.color.classes

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Parcelize
internal class Color(
    override val color: Int,
    override val label: String
) : IColor, Parcelable