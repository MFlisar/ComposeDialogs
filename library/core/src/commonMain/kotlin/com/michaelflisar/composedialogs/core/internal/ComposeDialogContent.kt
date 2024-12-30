package com.michaelflisar.composedialogs.core.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun ColumnScope.ComposeDialogContent(
    content: @Composable () -> Unit,
    contentColor: Color,
    fill: Boolean = false,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        val textStyle = MaterialTheme.typography.bodyMedium
        ProvideTextStyle(textStyle) {
            Box(
                modifier
                    .weight(weight = 1f, fill = fill)
                    .padding(bottom = 24.dp)
                    .align(Alignment.Start)
                    .wrapContentHeight()
            ) {
                content()
            }
        }
    }
}