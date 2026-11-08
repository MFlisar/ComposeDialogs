package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider
import kotlinx.datetime.DayOfWeek

@Composable
fun DayOfWeek(
    uiProvider: FrequencyUIProvider,
    selectedDayOfWeek: MutableState<DayOfWeek>,
    firstDayOffset: DayOfWeek,
    title: String,
    nameOfDay: (DayOfWeek) -> String,
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
    uiProvider.DayOfWeekDropdown(
        modifier = modifier,
        items = items,
        itemText = nameOfDay,
        selected = selectedDayOfWeek.value,
        title = title,
        onItemSelected = { selectedDayOfWeek.value = it }
    )
}