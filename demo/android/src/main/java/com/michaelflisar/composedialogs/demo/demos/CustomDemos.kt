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
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow

@Composable
fun CustomDemos(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Custom Dialogs")
    DemoDialogRow {
        DemoDialogCustom1(style, icon)
        DemoDialogCustom2(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogCustom1(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    val state = rememberDialogState()
    if (state.showing) {
        Dialog(
            state = state,
            style = style,
            icon = icon,
            title = { Text("Custom Dialog") },
        ) {
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
    DemoDialogButton(
        state,
        Icons.Default.Build,
        "Show Custom Dialog (Resizing)",
        "Shows a custom dialog with resizeable content"
    )
}

@Composable
private fun RowScope.DemoDialogCustom2(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    val state = rememberDialogState()
    if (state.showing) {
        Dialog(
            state = state,
            style = style,
            icon = icon,
            title = { Text("Custom Dialog") },
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                for (i in 1..50)
                    Text(modifier = Modifier.fillMaxWidth(), text = "Row $i")
            }
        }
    }
    DemoDialogButton(
        state,
        Icons.Default.Build,
        "Show Custom Dialog (Nested Scrolling)",
        "Shows a custom dialog with scrollable content"
    )
}