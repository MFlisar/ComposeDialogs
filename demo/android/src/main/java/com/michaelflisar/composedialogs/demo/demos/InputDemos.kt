package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.input.DialogInput
import com.michaelflisar.composedialogs.dialogs.input.DialogInputNumber
import com.michaelflisar.composedialogs.dialogs.input.DialogInputValidator
import com.michaelflisar.composedialogs.dialogs.number.DialogNumberPicker
import com.michaelflisar.composedialogs.dialogs.number.NumberPickerSetup
import com.michaelflisar.composedialogs.dialogs.number.RepeatingButton
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInput
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInputNumber
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInputValidator
import com.michaelflisar.composedialogs.dialogs.number.rememberDialogNumber

@Composable
fun InputDemos(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Text Input Dialogs")
    DemoDialogRow {
        DemoDialogInput1(style, icon)
    }
    DemoDialogRow {
        DemoDialogInput2(style, icon)
    }
    DemoDialogRegion("Number Input Dialogs")
    DemoDialogRow {
        DemoDialogInput3(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogInput1(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {

    val context = LocalContext.current

    val text = "Hello"
    val state = rememberDialogState(
        visible = false,
        buttonPositiveEnabled = text.isNotEmpty(),
        dismissAllowed = text.isNotEmpty()
    )
    /* --8<-- [start: demo] */
    if (state.visible) {

        // special state for input dialog
        val input = rememberDialogInput(text)

        // input dialog
        DialogInput(
            state = state,
            title = { Text("Input Dialog") },
            value = input,
            label = "Text",
            icon = icon,
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${input.value}")
                } else {
                    context.showToast("Event $it")
                }
            },
            validator = rememberDialogInputValidator(
                validate = {
                    if (it.isNotEmpty())
                        DialogInputValidator.Result.Valid
                    else
                        DialogInputValidator.Result.Error("Empty input is not allowed!")
                }
            ),
            onTextStateChanged = { valid, _ ->
                state.enableButton(DialogButtonType.Positive, valid)
            }
        )
    }
    /* --8<-- [end: demo] */
    DemoDialogButton(
        state,
        Icons.Default.TextFields,
        "Input Dialog",
        "Shows an input dialog that does not accept an empty input"
    )
}

@Composable
private fun RowScope.DemoDialogInput2(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {

    val context = LocalContext.current

    val text = "123"
    val state = rememberDialogState()
    if (state.visible) {

        // special state for input dialog
        val input = rememberDialogInput(text)

        // input dialog
        DialogInput(
            state = state,
            title = { Text("Input Dialog") },
            value = input,
            label = "Numerical Value",
            icon = icon,
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${input.value}")
                } else {
                    context.showToast("Event $it")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.TextFields,
        "Text Input Dialog which only allows numeric characters as input values",
        "Shows an input dialog that does only accept numeric characters"
    )
}

@Composable
private fun RowScope.DemoDialogInput3(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {

    val context = LocalContext.current

    /* --8<-- [start: demo-number] */
    // would work with Int, Long, Double and Float (all options of Number!)
    val number = 123
    val state = rememberDialogState()
    if (state.visible) {

        // special state for input dialog
        val value = rememberDialogInputNumber(number)

        // number dialog
        DialogInputNumber(
            state = state,
            title = { Text("Input Integer Dialog") },
            value = value,
            label = "Integer",
            icon = icon,
            style = style,
            onEvent = {
                if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                    // we should probably handle the input value in this case
                    context.showToast("Submitted Input: ${value.value}")
                } else {
                    context.showToast("Event $it")
                }
            }
        )
    }
    /* --8<-- [end: demo-number] */
    DemoDialogButton(
        state,
        Icons.Default.Numbers,
        "Number INPUT Dialog",
        "Shows a number dialog that does only accept a valid Integer"
    )
}

@Composable
private fun RowScope.DemoDialogInput4(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    enableButtonLongPress: Boolean
) {

    val context = LocalContext.current

    // would work with Int, Long, Double and Float (all options of Number!)
    val number = 5
    val state = rememberDialogState()
    if (state.visible) {

        // special state for input dialog
        val value = rememberDialogNumber(number)

        // number dialog
        DialogNumberPicker(
            state = state,
            title = { Text("Input Integer Dialog") },
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
                min = 0, max = 100, stepSize = 5, repeatingButton = RepeatingButton.Enabled()
            )
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Numbers,
        "Number PICKER Dialog",
        "Shows a number picker for Integer values in [0, 100] with a step size of 5 (repeat button clicks on long press = $enableButtonLongPress)"
    )
}