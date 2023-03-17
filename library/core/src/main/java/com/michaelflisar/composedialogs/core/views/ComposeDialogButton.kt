package com.michaelflisar.composedialogs.core.views

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.*

@Composable
fun ComposeDialogButton(
    button: DialogButton,
    buttonType: DialogButtonType,
    options: Options,
    state: DialogState,
    requestDismiss: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    if (button.text.isNotEmpty()) {
        val enabled = state.isButtonEnabled(buttonType)
        TextButton(
            enabled = enabled,
            onClick = {
                val dismiss = options.dismissOnButtonClick && state.interactionSource.dismissAllowed.value
                state.onButtonPressed(onEvent, buttonType, dismiss)
                if (dismiss)
                    requestDismiss()
            }) {
            Text(button.text)
        }
    }
}