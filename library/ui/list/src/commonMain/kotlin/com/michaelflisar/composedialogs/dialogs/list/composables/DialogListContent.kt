package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI

@Composable
internal fun DialogListContent(
    text: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)? = null
) {
    Column {
        ComposeDialogsUI.provider.ProvideContentColor(
            ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
        ) {
            ComposeDialogsUI.provider.ProvideTextStyle(
                ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.BodyLarge)
            ) {
                text()
            }
        }

        supportingText?.let {
            ComposeDialogsUI.provider.ProvideContentColor(
                ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurfaceVariant)
            ) {
                ComposeDialogsUI.provider.ProvideTextStyle(
                    ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.Body)
                ) {
                    it()
                }
            }
        }
    }
}