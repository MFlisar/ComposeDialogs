package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DatePickerConfig
import com.michaelflisar.composedialogs.dialogs.date.DatePickerCellType
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import com.michaelflisar.composedialogs.dialogs.date.DateColor
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarMonth(
    state: MutableState<LocalDate>,
    config: DatePickerConfig,
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

    val weekdays = DateUtil.getSortedWeekDays(config.firstDayOfWeek)

    val items = remember(year, month, config.firstDayOfWeek) {
        derivedStateOf {
            val colIndexOfFirstDay = weekdays.indexOf(firstDayOfMonth.value.dayOfWeek)
            val daysInGrid = colIndexOfFirstDay + lastDayOfMonth.value.day
            val cellCount = ((daysInGrid + 6) / 7) * 7
            List(cellCount) { index ->
                firstDayOfMonth.value.plus(index - colIndexOfFirstDay, DateTimeUnit.DAY)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.wrapContentHeight().verticalScroll(rememberScrollState())
    ) {
        val modifierHeader = Modifier
            .weight(1f)
            .padding(4.dp)
        val modifierCell = Modifier
            .weight(1f)
            .height(config.dateCellHeight)

        val rows = items.value.size / 7
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(7) {
                CalendarCellHeader(uiProvider, config, weekdays[it], modifierHeader)
            }
        }
        repeat(rows) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(7) { col ->
                    val date = items.value[row * 7 + col]
                    val selected = date == state.value
                    val isCurrentMonth = date.year == year && date.month == firstDayOfMonth.value.month
                    val type = if (isCurrentMonth) {
                        DatePickerCellType.CurrentMonth(
                            selected = selected,
                            today = date == today.value
                        )
                    } else {
                        DatePickerCellType.OtherMonth(
                            selected = selected
                        )
                    }
                    ui.CalendarCell(
                        date = date,
                        type = type,
                        modifier = modifierCell
                    ) {
                        if (type is DatePickerCellType.CurrentMonth) {
                            state.value = date
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarCellHeader(
    uiProvider: DateUIProvider,
    config: DatePickerConfig,
    dayOfWeek: DayOfWeek,
    modifier: Modifier
) {
    val text = config.formatterWeekDayLabel(dayOfWeek)
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
