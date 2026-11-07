package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.Month

@Composable
internal fun CalendarSelectListYear(
    pageData: State<CalendarPageData>,
    setup: DialogDate.Setup,
    dateRange: DialogDate.Range,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.year,
        range = dateRange.years,
        itemFormatter = {
            setup.formatterYearSelectorList(it)
        }
    ) {
        val offsetPages = (it - dateRange.years.first) * 12 + pageData.value.month - 1
        scope.launch { pagerState.scrollToPage(offsetPages) }
        viewState.value = DateViewState.Calendar
    }
}

@Composable
internal fun CalendarSelectListMonth(
    pageData: State<CalendarPageData>,
    setup: DialogDate.Setup,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.month,
        range = (1..12),
        itemFormatter = {
            setup.formatterMonthSelectorList(Month(it))
        }
    ) {
        val offsetPages = it - pageData.value.month
        scope.launch { pagerState.scrollToPage(pagerState.currentPage + offsetPages) }
        viewState.value = DateViewState.Calendar
    }
}

@Composable
private fun CalendarSelectList(
    value: Int,
    range: IntRange,
    itemFormatter: @Composable (value: Int) -> String,
    onClick: (value: Int) -> Unit
) {
    val state = rememberLazyListState(
        value - range.first
    )
    DialogUI.provider.HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    LazyColumn(state = state) {
        range.forEach {
            item {
                Row(
                    modifier = Modifier
                        .clip(DialogUI.provider.smallShape())
                        .clickable {
                            onClick(it)
                        }
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialogUI.provider.Icon(
                        icon = DialogUI.IconType.Check,
                        contentDescription = null,
                        modifier = Modifier.alpha(if (it == value) 1f else 0f),
                        tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    DialogUI.provider.Text(
                        text = itemFormatter(it),
                        role = DialogUI.TextRole.Body,
                        modifier = Modifier,
                        color = DialogUI.provider.color(DialogUI.ColorRole.OnSurface),
                        textAlign = TextAlign.Center,
                        fontWeight = null
                    )
                }
            }
        }
    }
}