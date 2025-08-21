package com.michaelflisar.composedialogs.core.internal

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogButton
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions

@Composable
internal fun ComposeDialogButton(
    button: DialogButton,
    buttonType: DialogButtonType,
    state: DialogState,
    options: DialogOptions,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    if (button.text.isNotEmpty()) {
        val enabled = state.isButtonEnabled(buttonType)
        TextButton(
            enabled = enabled,
            colors = colors,
            onClick = {
                val dismiss =
                    options.dismissOnButtonClick && state.interactionSource.dismissAllowed.value
                state.onButtonPressed(onEvent, buttonType, dismiss)
                if (dismiss) {
                    dismissOnButtonPressed()
                }
            }
        ) {
            Text(button.text)
        }
    }
}