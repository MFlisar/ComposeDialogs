package com.michaelflisar.composedialogs.dialogs.gdpr

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.*

@Composable
fun DialogGDPR(
    state: DialogState,
    // custom settings
    // ...
    title: DialogTitle = DialogDefaults.title(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        Text(text = "TODO")
    }
}