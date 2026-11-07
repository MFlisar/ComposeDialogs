package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ui.DialogUI

@Composable
internal fun DialogListContent(
    text: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)? = null
) {
    Column {
        DialogUI.provider.ProvideContentColor(
            DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
        ) {
            DialogUI.provider.ProvideTextStyle(
                DialogUI.provider.textStyle(DialogUI.TextRole.BodyLarge)
            ) {
                text()
            }
        }

        supportingText?.let {
            DialogUI.provider.ProvideContentColor(
                DialogUI.provider.color(DialogUI.ColorRole.OnSurfaceVariant)
            ) {
                DialogUI.provider.ProvideTextStyle(
                    DialogUI.provider.textStyle(DialogUI.TextRole.Body)
                ) {
                    it()
                }
            }
        }
    }
}