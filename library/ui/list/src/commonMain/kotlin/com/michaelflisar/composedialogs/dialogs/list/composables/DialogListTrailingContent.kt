package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI

@Composable
internal fun DialogListTrailingContent(
    content: @Composable () -> Unit
) {
    ComposeDialogsUI.provider.ProvideContentColor(
        ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurfaceVariant)
    ) {
        ComposeDialogsUI.provider.ProvideTextStyle(
            ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.Body)
        ) {
            content()
        }
    }
}