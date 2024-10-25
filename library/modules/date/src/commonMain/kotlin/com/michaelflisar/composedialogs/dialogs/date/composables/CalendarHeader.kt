package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.Month

@Composable
internal fun CalendarHeader(
    pagerState: PagerState,
    range: DialogDate.Range,
    setup: DialogDate.Setup,
    pageData: State<CalendarPageData>,
    viewState: MutableState<DateViewState>
) {
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
            targetViewState = DateViewState.SelectMonth
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
            targetViewState = DateViewState.SelectYear
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
    viewState: MutableState<DateViewState>,
    targetViewState: DateViewState
) {
    Row(
        modifier = modifier.heightIn(min = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showButtons) {
            AnimatedVisibility(visible = viewState.value == DateViewState.Calendar) {
                IconButton(
                    modifier = Modifier.size(36.dp),
                    onClick = { onDownClick?.invoke() },
                    enabled = onDownClick != null
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    if (viewState.value != targetViewState)
                        viewState.value = targetViewState
                    else
                        viewState.value = DateViewState.Calendar
                }
                .padding(4.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            val rotation = animateFloatAsState(if (viewState.value == targetViewState) 180f else 0f)
            Icon(
                modifier = Modifier.rotate(rotation.value),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
        if (showButtons) {
            AnimatedVisibility(visible = viewState.value == DateViewState.Calendar) {
                IconButton(
                    modifier = Modifier.size(36.dp),
                    onClick = { onUpClick?.invoke() },
                    enabled = onUpClick != null
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                }
            }
        }
    }
}