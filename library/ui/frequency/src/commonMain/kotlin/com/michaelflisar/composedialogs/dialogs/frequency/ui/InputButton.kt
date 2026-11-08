package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider

@Composable
fun InputButton(
    uiProvider: FrequencyUIProvider,
    modifier: Modifier = Modifier.Companion,
    title: String = "",
    value: String,
    onClick: () -> Unit
) {
    uiProvider.InputButton(
        modifier = modifier,
        title = title,
        value = value,
        onClick = onClick
    )
}