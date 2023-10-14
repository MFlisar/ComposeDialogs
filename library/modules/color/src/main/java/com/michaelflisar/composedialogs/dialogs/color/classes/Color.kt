package com.michaelflisar.composedialogs.dialogs.color.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal class Color(
    override val color: Int,
    override val label: String
) : IColor, Parcelable