package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressDemos(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Progress Dialogs")
    DemoDialogRow {
        DemoDialogProgress1(style, icon)
        DemoDialogProgress2(style, icon)
    }
    DemoDialogRow {
        DemoDialogProgress3(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogProgress1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?
) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        DialogProgress(
            state = state,
            content = {
                Text("Working...")
            },
            progressStyle = DialogProgress.Style.Indeterminate(linear = true),
            icon = icon,
            title = "Progress Dialog",
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Stop")
            ),
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    context.showToast("Progress Dialog closed by button")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Downloading,
        "Progress Dialog",
        "Shows an endless LINEAR progress dialog"
    )
}

@Composable
private fun RowScope.DemoDialogProgress2(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?
) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        DialogProgress(
            state = state,
            content = {
                Text("Working...")
            },
            progressStyle = DialogProgress.Style.Indeterminate(linear = false),
            icon = icon,
            title = "Progress Dialog",
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Stop")
            ),
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    context.showToast("Progress Dialog closed by button")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Downloading,
        "Progress Dialog",
        "Shows an endless CIRCULAR progress dialog"
    )
}

@Composable
private fun RowScope.DemoDialogProgress3(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?
) {
    val context = LocalContext.current
    val state = rememberDialogState(
        dismissAllowed = false,
        buttonPositiveEnabled = false
    )
    if (state.showing) {
        var time by rememberSaveable { mutableStateOf(10) }
        LaunchedEffect(Unit) {
            launch {
                while (time > 0) {
                    delay(1000)
                    time--
                }
                state.enableButton(DialogButtonType.Positive, true)
                state.dismissable(true)
            }
        }
        val progressStyle by remember {
            derivedStateOf { DialogProgress.Style.Determinate(linear = true, 10 - time, 10) }
        }
        val label by remember {
            derivedStateOf {
                if (time > 0) "Working..." else "Done!"
            }
        }
        val iconToShow by remember {
            derivedStateOf {
                if (time > 0) icon else {
                    { Icon(Icons.Default.Check, null, tint = Color.Green) }
                }
            }
        }
        DialogProgress(
            state = state,
            content = {
                Text("Working...")
            },
            progressStyle = progressStyle,
            icon = iconToShow,
            title = "Progress Dialog",
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Close")
            ),
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    context.showToast("Progress Dialog closed by button")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Downloading,
        "Progress Dialog",
        "Shows an LINEAR progress dialog for 10 seconds (not cancelable)"
    )
}