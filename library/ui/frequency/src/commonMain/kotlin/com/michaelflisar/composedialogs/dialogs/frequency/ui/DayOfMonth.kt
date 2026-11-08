package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider
import kotlinx.datetime.Month

@Composable
fun DayOfMonth(
    uiProvider: FrequencyUIProvider,
    selectedMonth: Month,
    selectedDayOfMonth: MutableState<Int>,
    title: String,
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
    uiProvider.DayOfMonthDropdown(
        modifier = modifier,
        items = items,
        itemText = Int::toString,
        selected = selectedDayOfMonth.value,
        title = title,
        onItemSelected = { selectedDayOfMonth.value = it }
    )
}