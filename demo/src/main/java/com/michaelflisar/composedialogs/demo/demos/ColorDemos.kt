package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.DialogColorLabelStyle
import com.michaelflisar.composedialogs.dialogs.color.rememberDialogColor

@Composable
fun ColorDemos(style: DialogStyle, icon: DialogIcon?) {
    DemoDialogRegion("Color Dialogs")
    DemoDialogRow {
        DemoDialogColor1(style, icon)
    }
    DemoDialogRow {
        DemoDialogColor2(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogColor1(style: DialogStyle, icon: DialogIcon?) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        val color = rememberDialogColor(Color.Blue.copy(alpha = .5f))
        DialogColor(
            state = state,
            color = color,
            alphaSupported = true,
            icon = icon,
            title = "Color Dialog",
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    context.showToast("Selected color: #${Integer.toHexString(color.value.toArgb())}")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.ColorLens,
        "Show Color Dialog",
        "Shows a color dialog (alpha supported)"
    )
}

@Composable
private fun RowScope.DemoDialogColor2(style: DialogStyle, icon: DialogIcon?) {
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        val color = rememberDialogColor(Color.Red)
        DialogColor(
            state = state,
            color = color,
            alphaSupported = false,
            labelStyle = DialogColorLabelStyle.Percent,
            icon = icon,
            title = "Color Dialog",
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    context.showToast("Selected color: #${Integer.toHexString(color.value.toArgb())}")
                } else {
                    context.showToast("Event $it")
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