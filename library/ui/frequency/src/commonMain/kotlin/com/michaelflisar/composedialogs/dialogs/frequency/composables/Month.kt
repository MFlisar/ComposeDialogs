package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.DialogFrequency
import kotlinx.datetime.Month

@Composable
internal fun Month(
    selectedMonthOfYear: MutableState<Month>,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier.Companion
) {
    Dropdown(
        modifier = modifier,
        items = Month.entries,
        mapper = { item, dropdown -> texts.nameOfMonth(item) },
        selected = selectedMonthOfYear,
        title = texts.monthOfYear
    )
}