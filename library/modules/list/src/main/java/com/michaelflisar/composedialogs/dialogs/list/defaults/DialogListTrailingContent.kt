package com.michaelflisar.composedialogs.dialogs.list.defaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DialogListTrailingContent(
    trailingSupportingText: String
) {
    Text(
        text = trailingSupportingText,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}