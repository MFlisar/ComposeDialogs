package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ui.DialogUI

@Composable
internal fun DialogListTrailingContent(
    content: @Composable () -> Unit
) {
    DialogUI.provider.ProvideContentColor(
        DialogUI.provider.color(DialogUI.ColorRole.OnSurfaceVariant)
    ) {
        DialogUI.provider.ProvideTextStyle(
            DialogUI.provider.textStyle(DialogUI.TextRole.Body)
        ) {
            content()
        }
    }
}