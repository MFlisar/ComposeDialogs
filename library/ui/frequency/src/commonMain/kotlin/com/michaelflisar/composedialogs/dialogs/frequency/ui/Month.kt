package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider
import kotlinx.datetime.Month

@Composable
fun Month(
    uiProvider: FrequencyUIProvider,
    selectedMonthOfYear: Month,
    title: String,
    nameOfMonth: (Month) -> String,
    modifier: Modifier = Modifier.Companion,
    onMonthSelected: (Month) -> Unit
) {
    uiProvider.MonthDropdown(
        modifier = modifier,
        items = Month.entries,
        itemText = nameOfMonth,
        selected = selectedMonthOfYear,
        title = title,
        onItemSelected = onMonthSelected
    )
}