package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerSetup
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import com.michaelflisar.composedialogs.dialogs.date.DateColor
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarMonth(
    state: MutableState<LocalDate>,
    setup: DatePickerSetup,
    year: Int,
    month: Int,
    uiProvider: DateUIProvider
) {
    val ui = uiProvider
    val firstDayOfMonth = remember(year, month) {
        derivedStateOf {
            DateUtil.firstDateOfMonth(year, month)
        }
    }

    val lastDayOfMonth = remember(year, month) {
        derivedStateOf {
            DateUtil.lastDateOfMonth(year, month)
        }
    }

    val today = remember {
        derivedStateOf { DateUtil.today() }
    }

    val weekdays = DateUtil.getSortedWeekDays(setup.firstDayOfWeek)

    val items = remember(year, month, setup.firstDayOfWeek) {
        derivedStateOf {
            val items = mutableListOf<LocalDate?>()
            val colIndexOfFirstDay = weekdays.indexOf(firstDayOfMonth.value.dayOfWeek)
            val colIndexOfLastDay = weekdays.indexOf(lastDayOfMonth.value.dayOfWeek)
            repeat(colIndexOfFirstDay) {
                items.add(null)
            }
            repeat(lastDayOfMonth.value.day) {
                val date = LocalDate(
                    firstDayOfMonth.value.year,
                    firstDayOfMonth.value.month,
                    it + 1
                )
                items.add(date)
            }
            repeat(7 - colIndexOfLastDay - 1) {
                items.add(null)
            }
            items
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.wrapContentHeight()
    ) {
        val modifierHeader = Modifier
            .weight(1f)
            .padding(4.dp)
        val modifierCell = Modifier
            .weight(1f)
            .height(setup.dateCellHeight)

        val rows = items.value.size / 7
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(7) {
                CalendarCellHeader(uiProvider, setup, weekdays[it], modifierHeader)
            }
        }
        repeat(rows) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(7) { col ->
                    val date = items.value[row * 7 + col]
                    if (date != null) {
                        ui.CalendarCell(
                            date = date,
                            selected = date == state.value,
                            today = date == today.value,
                            modifier = modifierCell
                        ) {
                            state.value = date
                        }
                    } else {
                        Spacer(modifier = modifierCell)
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarCellHeader(
    uiProvider: DateUIProvider,
    setup: DatePickerSetup,
    dayOfWeek: DayOfWeek,
    modifier: Modifier
) {
    val text = setup.formatterWeekDayLabel(dayOfWeek)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        uiProvider.Text(
            text = text,
            modifier = Modifier,
            style = com.michaelflisar.composedialogs.dialogs.date.DateTextStyle.BodyMedium,
            color = null,
            textAlign = null,
            maxLines = 1
        )
    }
}
