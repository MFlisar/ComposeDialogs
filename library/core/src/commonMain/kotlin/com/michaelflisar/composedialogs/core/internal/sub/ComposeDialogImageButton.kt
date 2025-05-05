package com.michaelflisar.composedialogs.core.internal.sub

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent

@Composable
internal fun ComposeDialogImageButton(
    buttonType: DialogButtonType,
    icon: ImageVector,
    state: DialogState,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    val enabled = state.isButtonEnabled(buttonType)
    IconButton(
        enabled = enabled,
        onClick = {
            val dismiss = state.interactionSource.dismissOnButtonClick.value && state.interactionSource.dismissAllowed.value
            state.onButtonPressed(onEvent, buttonType, dismiss)
            if (dismiss) {
                dismissOnButtonPressed()
            }
        }
    ) {
        Icon(icon, "Back")
    }
}