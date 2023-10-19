package com.michaelflisar.composedialogs.dialogs.color.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object ColorStateSaver : Saver<MutableState<Color>, Int> {
    override fun restore(value: Int): MutableState<Color> {
        return mutableStateOf(Color(value))
    }
    override fun SaverScope.save(value: MutableState<Color>): Int {
        return value.value.toArgb()
    }

}