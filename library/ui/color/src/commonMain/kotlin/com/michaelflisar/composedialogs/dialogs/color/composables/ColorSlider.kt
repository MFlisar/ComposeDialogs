package com.michaelflisar.composedialogs.dialogs.color.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import kotlin.math.roundToInt

@Composable
internal fun ColorSlider(
    label: String,
    value: Float,
    labelStyle: DialogColor.LabelStyle,
    onValueChange: (value: Float) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DialogUI.provider.Text(
            text = label,
            role = DialogUI.TextRole.Body,
            modifier = Modifier,
            color = null,
            textAlign = null,
            fontWeight = null
        )
        DialogUI.provider.Slider(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange
        )
        when (labelStyle) {
            DialogColor.LabelStyle.Value -> DialogUI.provider.Text(
                text = (255f * value).toInt().toString(),
                role = DialogUI.TextRole.Body,
                modifier = Modifier,
                color = null,
                textAlign = null,
                fontWeight = null
            )
            DialogColor.LabelStyle.Percent -> {
                DialogUI.provider.Text(
                    modifier = Modifier.width(48.dp),
                    text = ((value * 1000f).roundToInt() / 10f).toString() + "%", // 1 comma
                    role = DialogUI.TextRole.Body,
                    color = null,
                    textAlign = TextAlign.End,
                    fontWeight = null
                )
            }
        }

    }
}