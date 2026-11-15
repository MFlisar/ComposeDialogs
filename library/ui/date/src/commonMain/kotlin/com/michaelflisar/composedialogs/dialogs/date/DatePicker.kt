package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarMonth
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectListMonth
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectListYear
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState

/**
 * Renders only the date calendar using the caller-supplied [DateUIProvider].
 *
 * The caller creates a [DatePickerState] and independently places this calendar
 * and any controls that use that state. [config] controls formatting and behavior
 * as well as the supported year range.
 *
 * @param modifier modifier applied to the calendar while showing a month
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DatePicker(
    uiProvider: DateUIProvider,
    state: DatePickerState,
    config: DatePickerConfig,
    modifier: Modifier = Modifier,
) {
    val pagerState = state.pagerState
    val viewState = state.viewState
    val pageData = remember(pagerState.currentPage, config.years) {
        derivedStateOf {
            CalendarPageData(pagerState.currentPage, config.years)
        }
    }

    BackHandler(enabled = viewState.value != DatePickerViewState.Calendar) {
        viewState.value = DatePickerViewState.Calendar
    }

    val calendarItems: @Composable () -> Unit = {
        CalendarHeader(pagerState, config, pageData, viewState, uiProvider)
        when (viewState.value) {
            DatePickerViewState.Calendar -> {
                HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
                    val page = CalendarPageData(it, config.years)
                        CalendarMonth(state.date, config, page.year, page.month, uiProvider)

                }
            }

            DatePickerViewState.SelectYear -> {
                CalendarSelectListYear(
                    pageData,
                    config,
                    pagerState,
                    viewState,
                    uiProvider
                )
            }

            DatePickerViewState.SelectMonth -> {
                CalendarSelectListMonth(pageData, config, pagerState, viewState, uiProvider)
            }
        }
    }
    Column(modifier = Modifier.animateContentSize()) {
        calendarItems()
    }
}
