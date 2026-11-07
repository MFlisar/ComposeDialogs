package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI

@Composable
internal fun InputButton(
    modifier: Modifier = Modifier.Companion,
    title: String = "",
    value: String,
    minLines: Int = 1,
    maxLines: Int = 1,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    OutlinedDecoratedContainer(
        title = title,
        modifier = modifier,
        enabled = enabled,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        onClick = onClick
    ) {
        ComposeDialogsUI.provider.Text(
            text = value,
            role = ComposeDialogsUI.TextRole.Body,
            modifier = Modifier.fillMaxWidth().then(OutlinedDecoratedContainer.MODIFIER_CORRECTION),
            color = ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface),
            textAlign = null,
            fontWeight = null
        )
    }
}