package com.michaelflisar.composedialogs.core.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogButton
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.copied.AlertDialogFlowRow
import com.michaelflisar.composedialogs.core.copied.ButtonsCrossAxisSpacing
import com.michaelflisar.composedialogs.core.copied.ButtonsMainAxisSpacing
import com.michaelflisar.composedialogs.core.DialogState

@Composable
internal fun ColumnScope.ComposeDialogButtons(
    modifier: Modifier = Modifier,
    buttons: DialogButtons,
    options: Options,
    state: DialogState,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            val textStyle = MaterialTheme.typography.labelLarge
            ProvideTextStyle(
                value = textStyle,
                content = {
                    AlertDialogFlowRow(
                        mainAxisSpacing = ButtonsMainAxisSpacing,
                        crossAxisSpacing = ButtonsCrossAxisSpacing
                    ) {
                        ComposeDialogButton(
                            button = buttons.negative,
                            buttonType = DialogButtonType.Negative,
                            options = options,
                            state = state,
                            dismissOnButtonPressed = dismissOnButtonPressed,
                            onEvent = onEvent
                        )
                        ComposeDialogButton(
                            button = buttons.positive,
                            buttonType = DialogButtonType.Positive,
                            options = options,
                            state = state,
                            dismissOnButtonPressed = dismissOnButtonPressed,
                            onEvent = onEvent
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun ComposeDialogButton(
    button: DialogButton,
    buttonType: DialogButtonType,
    options: Options,
    state: DialogState,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    if (button.text.isNotEmpty()) {
        val enabled = state.isButtonEnabled(buttonType)
        TextButton(
            enabled = enabled,
            onClick = {
                val dismiss = options.dismissOnButtonClick && state.interactionSource.dismissAllowed.value
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