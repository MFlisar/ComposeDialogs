package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DatePickerConfig
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import com.michaelflisar.composedialogs.dialogs.date.DatePickerIcon
import com.michaelflisar.composedialogs.dialogs.date.DateTextStyle
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarSelectListYear(
    pageData: State<CalendarPageData>,
    config: DatePickerConfig,
    pagerState: PagerState,
    viewState: MutableState<DatePickerViewState>,
    uiProvider: DateUIProvider
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.year,
        range = config.years,
        uiProvider = uiProvider,
        itemFormatter = {
            config.formatterYearSelectorList(it)
        }
    ) {
        val offsetPages = (it - config.years.first) * 12 + pageData.value.month - 1
        scope.launch { pagerState.scrollToPage(offsetPages) }
        viewState.value = DatePickerViewState.Calendar
    }
}

@Composable
internal fun CalendarSelectListMonth(
    pageData: State<CalendarPageData>,
    config: DatePickerConfig,
    pagerState: PagerState,
    viewState: MutableState<DatePickerViewState>,
    uiProvider: DateUIProvider
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.month,
        range = (1..12),
        uiProvider = uiProvider,
        itemFormatter = {
            config.formatterMonthSelectorList(Month(it))
        }
    ) {
        val offsetPages = it - pageData.value.month
        scope.launch { pagerState.scrollToPage(pagerState.currentPage + offsetPages) }
        viewState.value = DatePickerViewState.Calendar
    }
}

@Composable
private fun CalendarSelectList(
    value: Int,
    range: IntRange,
    uiProvider: DateUIProvider,
    itemFormatter: @Composable (value: Int) -> String,
    onClick: (value: Int) -> Unit
) {
    val ui = uiProvider
    val state = rememberLazyListState(
        value - range.first
    )
    ui.Divider(modifier = Modifier.padding(vertical = 8.dp), vertical = false)
    ui.ScrollableList(state = state) {
        range.forEach {
            item {
                Row(
                    modifier = Modifier
                        .clip(ui.smallShape())
                        .clickable {
                            onClick(it)
                        }
                        .then(with(ui) { Modifier.minimumInteractiveComponentSize() })
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ui.Icon(
                        icon = DatePickerIcon.Check,
                        contentDescription = null,
                        modifier = Modifier.alpha(if (it == value) 1f else 0f),
                        tint = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ui.Text(
                        text = itemFormatter(it),
                        modifier = Modifier,
                        style = DateTextStyle.BodyMedium,
                        color = null,
                        textAlign = TextAlign.Center,
                        maxLines = Int.MAX_VALUE
                    )
                }
            }
        }
    }
}