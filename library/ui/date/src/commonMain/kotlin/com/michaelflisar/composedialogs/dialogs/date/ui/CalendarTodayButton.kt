package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtonType
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarTodayButton(
    buttonToday: @Composable () -> Unit,
    date: MutableState<LocalDate>,
    todayPage: Int,
    today: LocalDate,
    pagerState: PagerState,
    viewState: MutableState<DatePickerViewState>,
    uiProvider: DateUIProvider
) {
    val todayButtonEnabled by remember(
        pagerState.currentPage,
        date.value.year,
        date.value.month,
        date.value.day
    ) {
        derivedStateOf {
            pagerState.currentPage != todayPage || date.value != today
        }
    }
    val scope = rememberCoroutineScope()
    uiProvider.Button(
        type = DatePickerButtonType.Today,
        modifier = Modifier,
        enabled = todayButtonEnabled,
        onClick = {
            date.value = today
            if (viewState.value != DatePickerViewState.Calendar) {
                viewState.value = DatePickerViewState.Calendar
            }
            scope.launch { pagerState.animateScrollToPage(todayPage) }
        }
    ) {
        buttonToday()
    }
}