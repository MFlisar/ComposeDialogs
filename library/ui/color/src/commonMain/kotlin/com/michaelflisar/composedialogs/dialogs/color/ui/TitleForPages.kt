package com.michaelflisar.composedialogs.dialogs.color.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.michaelflisar.composedialogs.dialogs.color.ColorTextStyle
import com.michaelflisar.composedialogs.dialogs.color.ColorUIProvider

@Composable
internal fun TitleForPages(
    uiProvider: ColorUIProvider,
    modifier: Modifier,
    presetsText: String,
    customText: String,
    customSelected: Boolean,
    onPresetsClick: () -> Unit,
    onCustomClick: () -> Unit
) {
    val ui = uiProvider
    ui.TabButton(
        modifier = modifier
            .alpha(if (customSelected) .5f else 1f),
        onClick = onPresetsClick
    ) {
        ui.Text(presetsText, Modifier, ColorTextStyle.BodyMedium, null, null)
    }
    ui.TabButton(
        modifier = modifier
            .alpha(if (!customSelected) .5f else 1f),
        onClick = onCustomClick
    ) {
        ui.Text(customText, Modifier, ColorTextStyle.BodyMedium, null, null)
    }
}