package com.michaelflisar.composedialogs.dialogs.menu.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.DialogUI

@Composable
internal fun MenuRow(
    title: String,
    description: String?,
    icon: @Composable (() -> Unit)?,
    endIcon: @Composable (() -> Unit)? = null,
    bold: Boolean = false,
    onMenuClicked: (() -> Unit?)?
) {
    val paddingVertical = 8.dp
    val paddingHorizontal = 16.dp

    val maxLinesText = 2
    val maxLinesSupportingText = 2

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .height(IntrinsicSize.Min)
            .clip(DialogUI.provider.smallShape())
            .then(
                if (onMenuClicked != null) {
                    Modifier.clickable { onMenuClicked() }
                } else Modifier.Companion
            )
            .padding(horizontal = paddingHorizontal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon
        icon?.invoke()

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = paddingVertical)
        ) {
            DialogUI.provider.Text(
                text = title,
                role = DialogUI.TextRole.BodyLarge,
                modifier = Modifier,
                color = DialogUI.provider.color(DialogUI.ColorRole.OnSurface),
                textAlign = null,
                fontWeight = if (bold) FontWeight.Bold else null
            )
            if (description?.isNotEmpty() == true) {
                DialogUI.provider.Text(
                    text = description,
                    role = if (bold) DialogUI.TextRole.BodySmall else DialogUI.TextRole.Body,
                    modifier = Modifier,
                    color = DialogUI.provider.color(DialogUI.ColorRole.OnSurfaceVariant),
                    textAlign = null,
                    fontWeight = if (bold) FontWeight.Bold else null
                )
            }
        }

        // Icon end
        endIcon?.invoke()
    }
}