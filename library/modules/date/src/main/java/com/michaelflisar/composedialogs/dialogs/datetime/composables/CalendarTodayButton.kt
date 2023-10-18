package com.michaelflisar.composedialogs.dialogs.datetime.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarTodayButton(
    buttonToday: String,
    date: MutableState<LocalDate>,
    todayPage: Int,
    today: LocalDate,
    pagerState: PagerState
) {
    val todayButtonEnabled by remember(pagerState.currentPage, date.value.year, date.value.month, date.value.dayOfMonth) {
        derivedStateOf {
            pagerState.currentPage != todayPage || date.value != today
        }
    }
    val scope = rememberCoroutineScope()
    OutlinedButton(
        onClick = {
            date.value = today
            scope.launch { pagerState.animateScrollToPage(todayPage) }
        },
        enabled = todayButtonEnabled
    ) {
        Text(text = buttonToday)
    }
}