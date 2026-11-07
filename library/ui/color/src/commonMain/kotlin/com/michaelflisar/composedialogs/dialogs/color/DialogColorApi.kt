package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorStateSaver

@Stable
object DialogColor {

    @Immutable
    internal enum class Page {
        Custom,
        Presets
    }

    @Immutable
    enum class LabelStyle {
        Value,
        Percent
    }

    @Immutable
    class Texts internal constructor(
        val presets: String,
        val custom: String
    )
}

@Composable
fun rememberDialogColor(color: Color): MutableState<Color> =
    rememberSaveable(saver = ColorStateSaver) { mutableStateOf(color) }
