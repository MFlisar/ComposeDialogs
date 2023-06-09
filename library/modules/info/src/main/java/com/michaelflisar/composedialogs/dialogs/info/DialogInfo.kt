package com.michaelflisar.composedialogs.dialogs.info

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.michaelflisar.composedialogs.core.*

@Composable
fun DialogInfo(
    state: DialogState,
    // Custom - Required
    info: String,
    // Custom - Optional
    infoTitle: String = "",
    // Base Dialog - Optional
    title: DialogTitle = DialogDefaults.title(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        if (infoTitle.isNotEmpty()) {
            Text(text = infoTitle, style = MaterialTheme.typography.titleSmall)
        }
        Text(text = info)
    }
}