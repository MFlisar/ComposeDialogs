package com.michaelflisar.composedialogs.dialogs.color.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.color.ColorTextStyle
import com.michaelflisar.composedialogs.dialogs.color.ColorUIProvider
import kotlin.math.roundToInt

@Composable
internal fun ColorSlider(
    uiProvider: ColorUIProvider,
    label: String,
    value: Float,
    showPercent: Boolean,
    onValueChange: (value: Float) -> Unit,
) {
    val ui = uiProvider
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ui.Text(label, Modifier, ColorTextStyle.BodyMedium, null, null)
        ui.Slider(Modifier.weight(1f), value, onValueChange)
        if (showPercent) {
            ui.Text(
                modifier = Modifier.width(48.dp),
                text = ((value * 1000f).roundToInt() / 10f).toString() + "%",
                style = ColorTextStyle.BodyMedium,
                color = null,
                fontWeight = null
            )
        } else {
            ui.Text(
                text = (255f * value).toInt().toString(),
                modifier = Modifier,
                style = ColorTextStyle.BodyMedium,
                color = null,
                fontWeight = null
            )
        }

    }
}