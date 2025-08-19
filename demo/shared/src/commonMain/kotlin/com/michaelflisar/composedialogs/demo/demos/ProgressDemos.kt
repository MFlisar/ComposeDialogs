package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButton
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.composables.DemoDialogButton
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRow
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressDemos(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoDialogRegion("Progress Dialogs")
    DemoDialogRow {
        DemoDialogProgress1(style, icon, showInfo)
        DemoDialogProgress2(style, icon, showInfo)
    }
    DemoDialogRow {
        DemoDialogProgress3(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoDialogProgress1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    if (state.visible) {
        DialogProgress(
            state = state,
            content = {
                Text("Working...")
            },
            progressStyle = DialogProgress.Style.Indeterminate(linear = true),
            icon = icon,
            title = { Text("Progress Dialog") },
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Stop")
            ),
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    showInfo("Progress Dialog closed by button")
                } else {
                    showInfo("Event $it")
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
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    /* --8<-- [start: demo] */
    if (state.visible) {
        DialogProgress(
            state = state,
            content = {
                Text("Working...")
            },
            progressStyle = DialogProgress.Style.Indeterminate(linear = false),
            icon = icon,
            title = { Text("Progress Dialog") },
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Stop")
            ),
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    showInfo("Progress Dialog closed by button")
                } else {
                    showInfo("Event $it")
                }
            }
        )
    }
    /* --8<-- [end: demo] */
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
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState(
        dismissAllowed = false,
        buttonPositiveEnabled = false
    )
    if (state.visible) {
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
            title = { Text("Progress Dialog") },
            buttons = DialogDefaults.buttons(
                positive = DialogButton("Close")
            ),
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    showInfo("Progress Dialog closed by button")
                } else {
                    showInfo("Event $it")
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