package com.michaelflisar.composedialogs.core.internal

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.DialogState

@Composable
internal fun ComposeDialogImageButton(
    buttonType: DialogButtonType,
    icon: ImageVector,
    state: DialogState,
    options: DialogOptions,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit,
) {
    ComposeDialogImageButton(
        buttonType = buttonType,
        icon = { Icon(imageVector = icon, contentDescription = null) },
        state = state,
        options = options,
        dismissOnButtonPressed = dismissOnButtonPressed,
        onEvent = onEvent
    )
}

@Composable
internal fun ComposeDialogImageButton(
    buttonType: DialogButtonType,
    icon: @Composable () -> Unit,
    state: DialogState,
    options: DialogOptions,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit,
) {
    val enabled = state.isButtonEnabled(buttonType)
    IconButton(
        enabled = enabled,
        onClick = {
            val dismiss =
                options.dismissOnButtonClick && state.interactionSource.dismissAllowed.value
            state.onButtonPressed(onEvent, buttonType, dismiss)
            if (dismiss) {
                dismissOnButtonPressed()
            }
        }
    ) {
        icon()
    }
}