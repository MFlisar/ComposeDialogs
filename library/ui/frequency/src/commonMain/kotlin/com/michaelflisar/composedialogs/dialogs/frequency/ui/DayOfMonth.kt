package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider
import kotlinx.datetime.Month

@Composable
fun DayOfMonth(
    uiProvider: FrequencyUIProvider,
    selectedMonth: Month,
    selectedDayOfMonth: Int,
    title: String,
    modifier: Modifier = Modifier.Companion,
    onDaySelected: (Int) -> Unit
) {
    val items = remember(selectedMonth) {
        when (selectedMonth) {
            Month.FEBRUARY -> (1..29).toList()
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> (1..30).toList()
            else -> (1..31).toList()
        }
    }
    LaunchedEffect(selectedMonth, selectedDayOfMonth) {
        if (selectedDayOfMonth > items.last()) {
            onDaySelected(items.last())
        }
    }
    uiProvider.DayOfMonthDropdown(
        modifier = modifier,
        items = items,
        itemText = Int::toString,
        selected = selectedDayOfMonth,
        title = title,
        onItemSelected = onDaySelected
    )
}