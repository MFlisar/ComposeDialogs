package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.input.DialogNumberPicker
import com.michaelflisar.composedialogs.dialogs.input.NumberPickerSetup
import com.michaelflisar.composedialogs.dialogs.input.RepeatingButton
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogNumber

@Composable
fun NumberDemos(style: DialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Number Picker Dialogs")
    DemoDialogRow {
        DemoDialogInput1(style, icon)
    }
    DemoDialogRow {
        DemoDialogInput2(style, icon)
    }
    DemoDialogRow {
        DemoDialogInput3(style, icon)
    }
    DemoDialogRow {
        DemoDialogInput4(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogInput1(
    style: DialogStyle,
    icon: (@Composable () -> Unit)?
) {

    val context = LocalContext.current

    // would work with Int, Long, Double and Float (all options of Number!)
    val number = 5
    val state = rememberDialogState()
    if (state.showing) {

        // special state for input dialog
        val value = rememberDialogNumber(number)

        // number dialog
        DialogNumberPicker(
            state = state,
            title = { Text("Integer Picker Dialog") },
            value = value,
            icon = icon,
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${value.value}")
                } else {
                    context.showToast("Event $it")
                }
            },
            setup = NumberPickerSetup(
                min = 0, max = 100, stepSize = 5
            )
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Numbers,
        "Number PICKER Dialog",
        "Shows a number picker for Integer values in [0, 100] with a step size of 5"
    )
}

@Composable
private fun RowScope.DemoDialogInput2(
    style: DialogStyle,
    icon: (@Composable () -> Unit)?
) {

    val context = LocalContext.current

    // would work with Int, Long, Double and Float (all options of Number!)
    val number = 5
    val state = rememberDialogState()
    if (state.showing) {

        // special state for input dialog
        val value = rememberDialogNumber(number)

        // number dialog
        DialogNumberPicker(
            state = state,
            title = { Text("Integer Picker Dialog") },
            value = value,
            icon = icon,
            style = style,
            formatter = { "${it}g" },
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${value.value}")
                } else {
                    context.showToast("Event $it")
                }
            },
            setup = NumberPickerSetup(
                min = 0, max = 100, stepSize = 5, repeatingButton = RepeatingButton.Enabled()
            )
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Numbers,
        "Number PICKER Dialog",
        "Shows a number picker for Integer values in [0, 100] with a step size of 5 + repeat button clicks on long press + custom value formatter"
    )
}

@Composable
private fun RowScope.DemoDialogInput3(style: DialogStyle, icon: (@Composable () -> Unit)?) {

    val context = LocalContext.current

    // would work with Int, Long, Double and Float (all options of Number!)
    val number = 5f
    val state = rememberDialogState()
    if (state.showing) {

        // special state for input dialog
        val value = rememberDialogNumber(number)

        // number dialog
        DialogNumberPicker(
            state = state,
            title = { Text("Float Picker Dialog") },
            value = value,
            icon = icon,
            style = style,
            iconDown = {
                Icon(Icons.Default.Remove, null)
            },
            iconUp = {
                Icon(Icons.Default.Add, null)
            },
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${value.value}")
                } else {
                    context.showToast("Event $it")
                }
            },
            setup = NumberPickerSetup(
                min = 0f, max = 10f, stepSize = .5f
            )
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Numbers,
        "Number PICKER Dialog",
        "Shows a number picker for Float values in [0, 10f] with a step size of .5f + custom icons"
    )
}


@Composable
private fun RowScope.DemoDialogInput4(style: DialogStyle, icon: (@Composable () -> Unit)?) {

    val context = LocalContext.current

    // would work with Int, Long, Double and Float (all options of Number!)
    val number = 50
    val state = rememberDialogState()
    if (state.showing) {

        // special state for input dialog
        val value = rememberDialogNumber(number)

        // number dialog
        DialogNumberPicker(
            state = state,
            title = { Text("Int Picker Dialog") },
            value = value,
            icon = icon,
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${value.value}")
                } else {
                    context.showToast("Event $it")
                }
            },
            setup = NumberPickerSetup(
                min = 0, max = 1000, stepSize = 10, stepSize2 = 100
            )
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Numbers,
        "Number PICKER Dialog",
        "Shows a number picker for Int values in [0, 1000] with a step size of 10 + a second step size of 100 for faster buttons."
    )
}