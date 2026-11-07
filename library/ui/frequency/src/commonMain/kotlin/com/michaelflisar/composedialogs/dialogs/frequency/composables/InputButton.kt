package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.ui.DialogUI

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
        DialogUI.provider.Text(
            text = value,
            role = DialogUI.TextRole.Body,
            modifier = Modifier.fillMaxWidth().then(OutlinedDecoratedContainer.MODIFIER_CORRECTION),
            color = DialogUI.provider.color(DialogUI.ColorRole.OnSurface),
            textAlign = null,
            fontWeight = null
        )
    }
}