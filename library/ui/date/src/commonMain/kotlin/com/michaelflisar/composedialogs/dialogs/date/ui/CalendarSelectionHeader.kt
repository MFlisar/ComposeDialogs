package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtonType
import com.michaelflisar.composedialogs.dialogs.date.DateTextStyle
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarSelectionHeader(
    state: MutableState<LocalDate>,
    setup: DatePickerSetup,
    pagerState: PagerState,
    currentPage: State<Int>,
    viewState: MutableState<DatePickerViewState>,
    uiProvider: DateUIProvider
) {
    val ui = uiProvider
    val scope = rememberCoroutineScope()
    val enabled = pagerState.currentPage != currentPage.value && viewState.value == DatePickerViewState.Calendar
    ui.Button(
        type = DatePickerButtonType.SelectedDate,
        modifier = Modifier,
        enabled = enabled,
        onClick = {
            //if (viewState.value != DatePickerViewState.Calendar) {
            //    viewState.value = DatePickerViewState.Calendar
            //}
            scope.launch { pagerState.animateScrollToPage(currentPage.value) }
        },
    ) {
        ui.Text(
            text = setup.formatterSelectedDate(state.value),
            modifier = Modifier,
            style = DateTextStyle.TitleMedium,
            color = null,
            textAlign = null,
            maxLines = Int.MAX_VALUE
        )
    }
}