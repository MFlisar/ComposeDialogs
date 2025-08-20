package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InfoDemos(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Info Dialogs")
    DemoRow {
        DemoInfo1(style, icon, showInfo)
    }
    DemoRow {
        DemoInfo2(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoInfo1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
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
                showInfo("Event $event")
            }
        )
    }
    /* --8<-- [end: demo] */
    DemoButton(
        Icons.Default.Info,
        "Simple Info Dialog",
        "Shows a basic info dialog"
    ) {
        state.show()
    }
}

@Composable
private fun RowScope.DemoInfo2(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
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
                showInfo("Event $it")
            }
        )
    }
    DemoButton(
        Icons.Default.Info,
        "Advanced Info Dialog",
        "Shows a simple dialog with updating text that can't be closed before 10s are over"
    ) {
        state.show()
    }
}