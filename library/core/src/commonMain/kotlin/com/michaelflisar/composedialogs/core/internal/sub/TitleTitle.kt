package com.michaelflisar.composedialogs.core.internal.sub

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun TitleTitle(
    title: @Composable (() -> Unit)?,
    titleColor: Color,
    modifier: Modifier,
) {
    if (title != null) {
        CompositionLocalProvider(LocalContentColor provides titleColor) {
            val textStyle = MaterialTheme.typography.headlineSmall
            ProvideTextStyle(textStyle) {
                Box(modifier) {
                    title()
                }
            }
        }
    }
}