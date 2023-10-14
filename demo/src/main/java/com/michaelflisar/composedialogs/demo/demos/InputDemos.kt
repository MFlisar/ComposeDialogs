package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.info.DialogInput
import com.michaelflisar.composedialogs.dialogs.info.DialogInputValidator
import com.michaelflisar.composedialogs.dialogs.info.rememberDialogInput

@Composable
fun InputDemos(style: DialogStyle, icon: DialogIcon?) {
    DemoDialogRegion("Input Dialogs")
    DemoDialogRow {
        DemoDialogInput1(style, icon)
    }
    DemoDialogRow {
        DemoDialogInput2(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogInput1(style: DialogStyle, icon: DialogIcon?) {

    val context = LocalContext.current

    val text = "Hello"
    val state = rememberDialogState(
        showing = false,
        buttonPositiveEnabled = text.isNotEmpty(),
        dismissAllowed = text.isNotEmpty()
    )
    if (state.showing) {

        // special state for input dialog
        val input = rememberDialogInput(text)

        // input dialog
        DialogInput(
            state = state,
            title = "Dialog",
            input = input,
            inputLabel = "Input",
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
            validator = DialogInputValidator(
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
    DemoDialogButton(
        state,
        Icons.Default.TextFields,
        "Simple Input Dialog",
        "Shows an input dialog that does not accept an empty input"
    )
}

@Composable
private fun RowScope.DemoDialogInput2(style: DialogStyle, icon: DialogIcon?) {

    val context = LocalContext.current

    val text = "123"
    val state = rememberDialogState()
    if (state.showing) {

        // special state for input dialog
        val input = rememberDialogInput(text)

        // input dialog
        DialogInput(
            state = state,
            title = "Dialog",
            input = input,
            inputLabel = "Number",
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
        "Simple Number Input Dialog",
        "Shows an input dialog that does only accept numbers"
    )
}