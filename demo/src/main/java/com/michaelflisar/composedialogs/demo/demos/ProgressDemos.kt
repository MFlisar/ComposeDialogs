package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgress
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgressStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressDemos(style: DialogStyle, icon: DialogIcon?) {
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
private fun RowScope.DemoDialogProgress1(style: DialogStyle, icon: DialogIcon?) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        DialogProgress(
            state = state,
            label = "Working...",
            progressStyle = DialogProgressStyle.Indeterminate(linear = true),
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
private fun RowScope.DemoDialogProgress2(style: DialogStyle, icon: DialogIcon?) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        DialogProgress(
            state = state,
            label = "Working...",
            progressStyle = DialogProgressStyle.Indeterminate(linear = false),
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
private fun RowScope.DemoDialogProgress3(style: DialogStyle, icon: DialogIcon?) {
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
            derivedStateOf { DialogProgressStyle.Determinate(linear = true, 10 - time, 10) }
        }
        DialogProgress(
            state = state,
            label = "Working...",
            progressStyle = progressStyle,
            icon = icon,
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