package com.michaelflisar.composedialogs.dialogs.color.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object ColorStateSaverNullable : Saver<MutableState<Color?>, String> {
    override fun restore(value: String): MutableState<Color?> {
        return mutableStateOf(value.takeIf { it.isNotEmpty() }?.toInt()?.let { Color(it) })
    }

    override fun SaverScope.save(value: MutableState<Color?>): String {
        return value.value?.toArgb()?.toString() ?: ""
    }

}