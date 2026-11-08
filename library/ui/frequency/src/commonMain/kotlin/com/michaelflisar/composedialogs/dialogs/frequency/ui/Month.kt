package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider
import kotlinx.datetime.Month

@Composable
fun Month(
    uiProvider: FrequencyUIProvider,
    selectedMonthOfYear: MutableState<Month>,
    title: String,
    nameOfMonth: (Month) -> String,
    modifier: Modifier = Modifier.Companion
) {
    uiProvider.MonthDropdown(
        modifier = modifier,
        items = Month.entries,
        itemText = nameOfMonth,
        selected = selectedMonthOfYear.value,
        title = title,
        onItemSelected = { selectedMonthOfYear.value = it }
    )
}