package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.rememberDialogColor

@Composable
fun ColorDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Color Dialogs")
    DemoRow {
        DemoColor1(style, icon, showInfo)
    }
    DemoRow {
        DemoColor2(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoColor1(
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
    DemoButton(
        icon = Icons.Default.ColorLens,
        label = "Show Color Dialog",
        description = "Shows a color dialog (alpha supported)"
    ) {
        state.show()
    }
}

@Composable
private fun RowScope.DemoColor2(
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
    DemoButton(
        icon = Icons.Default.ColorLens,
        label = "Show Color Dialog",
        description = "Shows a color dialog (alpha NOT supported + RGB values are shown in percentages in all sliders)"
    ) {
        state.show()
    }
}