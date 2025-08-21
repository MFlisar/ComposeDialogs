package com.michaelflisar.composedialogs.core.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.StyleOptions
import com.michaelflisar.composedialogs.core.internal.TitleIcon
import com.michaelflisar.composedialogs.core.internal.TitleTitle

@Composable
fun ColumnScope.ComposeDialogTitle(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)?,
    icon: @Composable (() -> Unit)?,
    iconColor: Color,
    titleColor: Color,
    options: StyleOptions,
) {
    if (icon != null) {
        if (options.iconMode == StyleOptions.IconMode.CenterTop) {
            Column(
                modifier = modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TitleIcon(
                    icon = icon,
                    iconColor = iconColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TitleTitle(
                    title = title,
                    titleColor = titleColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        } else {
            Row(
                modifier = modifier.padding(bottom = 16.dp)
            ) {
                TitleIcon(
                    icon = icon,
                    iconColor = iconColor,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    TitleTitle(
                        title = title,
                        titleColor = titleColor,
                        modifier = Modifier
                    )
                }
            }
        }
    } else {
        TitleTitle(
            title = title,
            titleColor = titleColor,
            modifier = modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)

        )
    }
}

