package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI

@Composable
fun rememberDialogInput(text: String): MutableState<String> =
    rememberSaveable { mutableStateOf(text) }

@Stable
object DialogInput {

    sealed class SelectionState {
        data object Default : SelectionState()
        data object CursorEnd : SelectionState()
        data object SelectAll : SelectionState()
        class Selection(val start: Int, val end: Int) : SelectionState()
    }
}

object DialogInputDefaults {
    @Composable
    fun textStyle(): TextStyle = ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.Body)
}
