package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.DialogUI

internal object OutlinedDecoratedContainer {
    // TODO: find out why extra padding is necessary + fix it
    val MODIFIER_CORRECTION = Modifier.padding(vertical = 1.5.dp)
}

@Composable
internal fun OutlinedDecoratedContainer(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    DialogUI.provider.OutlinedContainer(
        title = title,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        leadingIcon = leadingIcon,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        content = content
    )
}