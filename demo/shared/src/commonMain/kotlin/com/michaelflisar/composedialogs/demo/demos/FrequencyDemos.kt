package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.dialogs.frequency.DialogFrequency
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.frequency.rememberDialogFrequency
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

@Composable
fun FrequencyDemos(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Frequency Dialogs")
    DemoRow {
        DemoFrequency1(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoFrequency1(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    /* --8<-- [start: demo] */
    if (state.visible) {
        val frequency = rememberDialogFrequency(Frequency.Weekly(DayOfWeek.MONDAY, LocalTime(12, 0), 1))
        DialogFrequency(
            state = state,
            frequency = frequency,
            title = { Text("Frequency") },
            icon = icon,
            style = style,
            onEvent = { event ->
                showInfo("Event $event | frequency: ${frequency.value}")
            }
        )
    }
    /* --8<-- [end: demo] */
    DemoButton(
        Icons.Default.Repeat,
        "Frequency Dialog",
        "Shows a frequency dialog"
    ) {
        state.show()
    }
}