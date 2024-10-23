package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarTodayButton(
    buttonToday: @Composable (enabled: Boolean, onClick: () -> Unit) -> Unit,
    date: MutableState<LocalDate>,
    todayPage: Int,
    today: LocalDate,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val todayButtonEnabled by remember(
        pagerState.currentPage,
        date.value.year,
        date.value.month,
        date.value.dayOfMonth
    ) {
        derivedStateOf {
            pagerState.currentPage != todayPage || date.value != today
        }
    }
    val scope = rememberCoroutineScope()
    buttonToday(todayButtonEnabled) {
        date.value = today
        if (viewState.value != DateViewState.Calendar) {
            viewState.value = DateViewState.Calendar
        }
        scope.launch { pagerState.animateScrollToPage(todayPage) }
    }
}