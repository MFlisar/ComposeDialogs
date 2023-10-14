package com.michaelflisar.composedialogs.dialogs.billing

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.*

@Composable
fun DialogBilling(
    state: DialogState,
    // custom settings
    // ...
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, titleStyle, icon, style, buttons, options, onEvent = onEvent) {
        Text(text = "TODO")
    }
}