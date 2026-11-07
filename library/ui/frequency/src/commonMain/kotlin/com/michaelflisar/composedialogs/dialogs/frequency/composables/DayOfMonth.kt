package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.DialogFrequency
import kotlinx.datetime.Month

@Composable
internal fun DayOfMonth(
    selectedMonth: Month,
    selectedDayOfMonth: MutableState<Int>,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier.Companion
) {
    val items = remember(selectedMonth) {
        when (selectedMonth) {
            Month.FEBRUARY -> (1..29).toList()
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> (1..30).toList()
            else -> (1..31).toList()
        }
    }
    LaunchedEffect(items.size) {
        if (selectedDayOfMonth.value > items.last()) {
            selectedDayOfMonth.value = items.last()
        }
    }
    Dropdown<Int>(
        modifier = modifier,
        items = items,
        mapper = { item, dropdown -> item.toString() },
        selected = selectedDayOfMonth,
        title = texts.dayOfMonth
    )
}