package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButton
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow

@Composable
fun VariousDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Various Dialogs")
    DemoRow {
        DemoButtonColors(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoButtonColors(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    if (state.visible) {
        DialogInfo(
            state = state,
            title = { Text("Edit item") },
            info = "Edit or delete item",
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Save"),
                negative = DialogButton(
                    text = "Delete",
                    //contentColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    containerColor = MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.small
                )
            ),
            icon = icon,
            style = style,
            onEvent = { event ->
                showInfo("Event $event")
            }
        )
    }
    DemoButton(
        Icons.Default.Info,
        "Edit and delete item Dialog",
        "Shows a dialog with a different colored delete button"
    ) {
        state.show()
    }
}