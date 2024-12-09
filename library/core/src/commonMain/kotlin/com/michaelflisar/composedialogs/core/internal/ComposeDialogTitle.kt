package com.michaelflisar.composedialogs.core.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
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
internal fun ColumnScope.ComposeDialogTitle(
    title: @Composable() (() -> Unit)?,
    icon: @Composable() (() -> Unit)?,
    iconColor: Color,
    titleColor: Color,
    modifier: Modifier = Modifier
) {
    if (icon != null) {
        CompositionLocalProvider(LocalContentColor provides iconColor) {
            Box(
                modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                icon()
            }
        }
    }
    if (title != null) {
        CompositionLocalProvider(LocalContentColor provides titleColor) {
            val textStyle = MaterialTheme.typography.headlineSmall
            ProvideTextStyle(textStyle) {
                Box(
                    modifier
                        .padding(bottom = 16.dp)
                        .align(
                            if (icon == null) {
                                Alignment.Start
                            } else {
                                Alignment.CenterHorizontally
                            }
                        )
                ) {
                    title()
                }
            }
        }
    }
}