package com.michaelflisar.composedialogs.dialogs.info

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.*

@Composable
fun DialogInfo(
    state: DialogState,
    info: String,
    infoTitle: String = "",
    title: String = "",
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.dialogButtons(),
    options: Options = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        if (infoTitle.isNotEmpty()) {
            Text(text = infoTitle, style = MaterialTheme.typography.titleSmall)
        }
        Text(text = info)
    }
}