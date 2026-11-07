package com.michaelflisar.composedialogs.dialogs.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI.TextRole

/**
 * Displays the information text and its optional label without creating a dialog.
 *
 * @param info the information text
 * @param infoLabel the optional label for the information text
 */
@Composable
fun DialogInfoContent(
    info: String,
    infoLabel: String = ""
) {
    Column {
        if (infoLabel.isNotEmpty()) {
            ComposeDialogsUI.provider.Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = infoLabel,
                role = TextRole.Label
            )
        }
        ComposeDialogsUI.provider.Text(
            text = info,
            role = TextRole.Body,
            modifier = Modifier
        )
    }
}
