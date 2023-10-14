package com.michaelflisar.composedialogs.dialogs.datetime.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.michaelflisar.composedialogs.dialogs.datetime.DialogDateState
import com.michaelflisar.composedialogs.dialogs.datetime.classes.SimpleDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarTodayButton(
    buttonToday: String,
    date: DialogDateState,
    todayPage: Int,
    today: SimpleDate,
    pagerState: PagerState
) {
    val todayButtonEnabled by remember(pagerState.currentPage, date.year, date.month, date.day) {
        derivedStateOf {
            pagerState.currentPage != todayPage || !date.isEqual(today)
        }
    }
    val scope = rememberCoroutineScope()
    OutlinedButton(
        onClick = {
            date.update(today)
            scope.launch { pagerState.animateScrollToPage(todayPage) }
        },
        enabled = todayButtonEnabled
    ) {
        Text(text = buttonToday)
    }
}