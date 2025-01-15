package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InfoDemos(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Info Dialogs")
    DemoDialogRow {
        DemoDialogInfo1(style, icon)
    }
    DemoDialogRow {
        DemoDialogInfo2(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogInfo1(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    val context = LocalContext.current
    val state = rememberDialogState()
    /* --8<-- [start: demo] */
    if (state.visible) {
        DialogInfo(
            state = state,
            title = { Text("Dialog") },
            info = "Simple Info Dialog",
            icon = icon,
            style = style,
            onEvent = { event ->
                context.showToast("Event $event")
            }
        )
    }
    /* --8<-- [end: demo] */
    DemoDialogButton(
        state,
        Icons.Default.Info,
        "Simple Info Dialog",
        "Shows a basic info dialog"
    )
}

@Composable
private fun RowScope.DemoDialogInfo2(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    val context = LocalContext.current
    val state = rememberDialogState(
        visible = false,
        buttonPositiveEnabled = false,
        dismissAllowed = false
    )
    if (state.visible) {
        var currentIcon by remember { mutableStateOf(icon) }
        var time by rememberSaveable { mutableStateOf(10) }
        val iconDone = @Composable {
            Icon(Icons.Default.Check, null)
        }
        LaunchedEffect(Unit) {
            launch {
                while (time > 0) {
                    delay(1000)
                    time--
                }
                state.enableButton(DialogButtonType.Positive, true)
                state.dismissable(true)
                currentIcon = iconDone
            }
        }
        DialogInfo(
            state = state,
            title = { Text("Dialog") },
            info = if (time == 0) "Dialog can be dismissed" else "Dialog can be dismissed in $time seconds...",
            icon = currentIcon,
            style = style,
            onEvent = {
                context.showToast("Event $it")
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Info,
        "Advanced Info Dialog",
        "Shows a simple dialog with updating text that can't be closed before 10s are over"
    )
}