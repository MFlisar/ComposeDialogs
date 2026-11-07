package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.DialogUI

/**
 * Displays optional progress content and a progress indicator without creating a dialog.
 *
 * @param content optional content displayed above the progress indicator
 * @param progressStyle the style of the progress indicator
 */
@Composable
fun DialogProgressContent(
    content: (@Composable ColumnScope.() -> Unit)? = null,
    progressStyle: DialogProgress.Style = DialogProgress.Style.Indeterminate()
) {
    Column {
        if (content != null) {
            content()
            Spacer(modifier = Modifier.height(8.dp))
        }
        when (progressStyle) {
            is DialogProgress.Style.Indeterminate -> {
                val type = if (progressStyle.linear) {
                    DialogUI.ProgressIndicatorType.Linear
                } else {
                    DialogUI.ProgressIndicatorType.Circular
                }
                if (progressStyle.linear) {
                    DialogUI.provider.ProgressIndicator(
                        type = type,
                        progress = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    DialogUI.provider.ProgressIndicator(
                        type = type,
                        progress = null,
                        modifier = Modifier
                    )
                }
            }

            is DialogProgress.Style.Determinate -> {
                val progress by animateFloatAsState(
                    targetValue = progressStyle.progress.toFloat() / progressStyle.max.toFloat()
                )
                if (progressStyle.linear) {
                    DialogUI.provider.ProgressIndicator(
                        type = DialogUI.ProgressIndicatorType.Linear,
                        progress = progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    DialogUI.provider.ProgressIndicator(
                        type = DialogUI.ProgressIndicatorType.Circular,
                        progress = progress,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
