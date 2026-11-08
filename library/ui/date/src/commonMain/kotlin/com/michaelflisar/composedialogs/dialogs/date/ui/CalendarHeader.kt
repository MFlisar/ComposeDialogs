package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerRange
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerSetup
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import com.michaelflisar.composedialogs.dialogs.date.DatePickerIcon
import com.michaelflisar.composedialogs.dialogs.date.DateTextStyle
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarHeader(
    pagerState: PagerState,
    range: DatePickerRange,
    setup: DatePickerSetup,
    pageData: State<CalendarPageData>,
    viewState: MutableState<DatePickerViewState>,
    uiProvider: DateUIProvider
) {
    val ui = uiProvider
    val scope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderItem(
            modifier = Modifier.weight(1f),
            text = setup.formatterSelectedMonth(Month(pageData.value.month)),
            showButtons = setup.showNextPreviousMonthButtons,
            onDownClick = if (pagerState.currentPage > 0) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }
            } else null,
            onUpClick = if (pagerState.currentPage < pagerState.pageCount - 1) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }
            } else null,
            viewState = viewState,
            targetViewState = DatePickerViewState.SelectMonth,
            uiProvider = uiProvider
        )
        //Spacer(modifier = Modifier.width(8.dp))
        HeaderItem(
            modifier = Modifier.weight(1f),
            text = setup.formatterSelectedYear(pageData.value.year),
            showButtons = setup.showNextPreviousYearButtons,
            onDownClick = if (pageData.value.year > range.years.first) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 12) } }
            } else null,
            onUpClick = if (pageData.value.year < range.years.last) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 12) } }
            } else null,
            viewState = viewState,
            targetViewState = DatePickerViewState.SelectYear,
            uiProvider = uiProvider
        )
    }
}

@Composable
internal fun HeaderItem(
    modifier: Modifier = Modifier,
    text: String,
    showButtons: Boolean,
    onDownClick: (() -> Unit)?,
    onUpClick: (() -> Unit)?,
    viewState: MutableState<DatePickerViewState>,
    targetViewState: DatePickerViewState,
    uiProvider: DateUIProvider
) {
    val ui = uiProvider
    Row(
        modifier = modifier.heightIn(min = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showButtons) {
            AnimatedVisibility(visible = viewState.value == DatePickerViewState.Calendar) {
                ui.IconButton(
                    modifier = Modifier.size(36.dp),
                    onClick = { onDownClick?.invoke() },
                    enabled = onDownClick != null
                ) {
                    ui.Icon(DatePickerIcon.Previous, null, Modifier, null)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clip(ui.smallShape())
                .clickable {
                    if (viewState.value != targetViewState)
                        viewState.value = targetViewState
                    else
                        viewState.value = DatePickerViewState.Calendar
                }
                .padding(4.dp)
        ) {
            ui.Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = DateTextStyle.BodySmall,
                color = null,
                textAlign = TextAlign.Center,
                maxLines = Int.MAX_VALUE
            )
            val rotation = animateFloatAsState(if (viewState.value == targetViewState) 180f else 0f)
            ui.Icon(
                modifier = Modifier.rotate(rotation.value),
                icon = DatePickerIcon.DropDown,
                contentDescription = null,
                tint = null
            )
        }
        if (showButtons) {
            AnimatedVisibility(visible = viewState.value == DatePickerViewState.Calendar) {
                ui.IconButton(
                    modifier = Modifier.size(36.dp),
                    onClick = { onUpClick?.invoke() },
                    enabled = onUpClick != null
                ) {
                    ui.Icon(DatePickerIcon.Next, null, Modifier, null)
                }
            }
        }
    }
}