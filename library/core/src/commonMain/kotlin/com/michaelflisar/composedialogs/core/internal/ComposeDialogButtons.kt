package com.michaelflisar.composedialogs.core.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.copied.AlertDialogFlowRow
import com.michaelflisar.composedialogs.core.copied.ButtonsCrossAxisSpacing
import com.michaelflisar.composedialogs.core.copied.ButtonsMainAxisSpacing
import com.michaelflisar.composedialogs.core.internal.sub.ComposeDialogButton

@Composable
internal fun ColumnScope.ComposeDialogButtons(
    modifier: Modifier = Modifier,
    buttons: DialogButtons,
    state: DialogState,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    if (!buttons.enabled) {
        return
    }
    Box(
        modifier = modifier.align(Alignment.End),
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
                            state = state,
                            dismissOnButtonPressed = dismissOnButtonPressed,
                            onEvent = onEvent
                        )
                        ComposeDialogButton(
                            button = buttons.positive,
                            buttonType = DialogButtonType.Positive,
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

