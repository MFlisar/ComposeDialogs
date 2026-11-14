package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal object NullableColorStateSaver : Saver<MutableState<Color?>, String> {
    override fun restore(value: String): MutableState<Color?> {
        return mutableStateOf(value.takeIf { it.isNotEmpty() }?.toInt()?.let { Color(it) })
    }

    override fun SaverScope.save(value: MutableState<Color?>): String {
        return value.value?.toArgb()?.toString() ?: ""
    }
}

private object ColorStateSaver : Saver<MutableState<Color>, Int> {
    override fun restore(value: Int): MutableState<Color> = mutableStateOf(Color(value))

    override fun SaverScope.save(value: MutableState<Color>): Int = value.value.toArgb()
}

/** Remembers a saveable color value for a color picker. */
@Composable
fun rememberColorPickerColor(color: Color): MutableState<Color> {
    return rememberSaveable(saver = ColorStateSaver) { mutableStateOf(color) }
}
