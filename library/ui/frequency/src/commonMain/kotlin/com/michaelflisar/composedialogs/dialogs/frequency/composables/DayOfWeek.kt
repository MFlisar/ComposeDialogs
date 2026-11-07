package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.DialogFrequency
import kotlinx.datetime.DayOfWeek

@Composable
internal fun DayOfWeek(
    selectedDayOfWeek: MutableState<DayOfWeek>,
    firstDayOffset: DayOfWeek,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier.Companion
) {
    val items by remember {
        derivedStateOf {
            val days = DayOfWeek.entries.toMutableList()
            while (days.first() != firstDayOffset) {
                val first = days.drop(1)
                days += first
            }
            days
        }
    }
    Dropdown<DayOfWeek>(
        modifier = modifier,
        items = items,
        mapper = { item, dropdown -> texts.nameOfDayOfWeek(item) },
        selected = selectedDayOfWeek,
        title = texts.dayOfWeek
    )
}