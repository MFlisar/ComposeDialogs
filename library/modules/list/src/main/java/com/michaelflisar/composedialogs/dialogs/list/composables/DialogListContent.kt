package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DialogListContent(
    text: String,
    supportingText: String?,
    maxLinesText: Int = 2,
    maxLinesSupportingText: Int = 2
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = maxLinesText,
        color = MaterialTheme.colorScheme.onSurface
    )
    if (supportingText?.isNotEmpty() == true) {
        Text(
            text = supportingText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = maxLinesSupportingText,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}