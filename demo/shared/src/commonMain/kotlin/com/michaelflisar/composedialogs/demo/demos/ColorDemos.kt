package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.composables.DemoDialogButton
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRow
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.rememberDialogColor

@Composable
fun ColorDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoDialogRegion("Color Dialogs")
    DemoDialogRow {
        DemoDialogColor1(style, icon, showInfo)
    }
    DemoDialogRow {
        DemoDialogColor2(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoDialogColor1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    /* --8<-- [start: demo] */
    if (state.visible) {
        val color = rememberDialogColor(Color.Blue.copy(alpha = .5f))
        DialogColor(
            state = state,
            color = color,
            alphaSupported = true,
            icon = icon,
            title = { Text("Color Dialog") },
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    val hex = color.value.toArgb().toUInt().toString(16).padStart(8, '0')
                    showInfo("Selected color: #$hex")
                } else {
                    showInfo("Event $it")
                }
            }
        )
    }
    /* --8<-- [end: demo] */
    DemoDialogButton(
        state,
        Icons.Default.ColorLens,
        "Show Color Dialog",
        "Shows a color dialog (alpha supported)"
    )
}

@Composable
private fun RowScope.DemoDialogColor2(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    if (state.visible) {
        val color = rememberDialogColor(Color.Red)
        DialogColor(
            state = state,
            color = color,
            alphaSupported = false,
            labelStyle = DialogColor.LabelStyle.Percent,
            icon = icon,
            title = { Text("Color Dialog") },
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    val hex = color.value.toArgb().toUInt().toString(16).padStart(8, '0')
                    showInfo("Selected color: #$hex")
                } else {
                    showInfo("Event $it")
                }
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.ColorLens,
        "Show Color Dialog",
        "Shows a color dialog (alpha NOT supported + RGB values are shown in percentages in all sliders)"
    )
}