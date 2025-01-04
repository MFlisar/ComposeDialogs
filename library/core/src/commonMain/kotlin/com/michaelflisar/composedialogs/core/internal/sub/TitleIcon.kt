package com.michaelflisar.composedialogs.core.internal.sub

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun TitleIcon(
    icon: @Composable (() -> Unit)?,
    iconColor: Color,
    modifier: Modifier,
) {
    if (icon != null) {
        CompositionLocalProvider(LocalContentColor provides iconColor) {
            Box(modifier) {
                icon()
            }
        }
    }
}