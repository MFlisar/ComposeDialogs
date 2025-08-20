package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow

@Composable
fun CustomDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Custom Dialogs")
    DemoRow {
        DemoCustom1(style, icon, showInfo)
        DemoCustom2(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoCustom1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    if (state.visible) {
        Dialog(
            state = state,
            style = style,
            icon = icon,
            title = { Text("Custom Dialog") },
            onEvent = { event ->
                showInfo("Event $event")
            }
        ) {
            Column {
                var checked by rememberSaveable { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                    Text(text = "Show Details")
                }
                AnimatedVisibility(visible = checked) {
                    Column {
                        Text(text = "Detail 1...")
                        Text(text = "Detail 2...")
                        Text(text = "Detail 3...")
                    }
                }
            }
        }
    }
    DemoButton(
        icon = Icons.Default.Build,
        label = "Show Custom Dialog (Resizing)",
        description = "Shows a custom dialog with resizeable content"
    ) {
        state.show()
    }
}

@Composable
private fun RowScope.DemoCustom2(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    if (state.visible) {
        Dialog(
            state = state,
            style = style,
            icon = icon,
            title = { Text("Custom Dialog") },
            onEvent = { event ->
                showInfo("Event $event")
            }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                for (i in 1..50)
                    Text(modifier = Modifier.fillMaxWidth(), text = "Row $i")
            }
        }
    }
    DemoButton(
        Icons.Default.Build,
        "Show Custom Dialog (Nested Scrolling)",
        "Shows a custom dialog with scrollable content"
    ) {
        state.show()
    }
}