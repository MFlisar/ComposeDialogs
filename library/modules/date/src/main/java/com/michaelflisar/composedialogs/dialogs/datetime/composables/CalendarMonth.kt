package com.michaelflisar.composedialogs.dialogs.datetime.composables

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.datetime.DialogDateSetup
import com.michaelflisar.composedialogs.dialogs.datetime.DialogDateState
import com.michaelflisar.composedialogs.dialogs.datetime.classes.SimpleDate
import com.michaelflisar.composedialogs.dialogs.datetime.utils.DateUtil

@Composable
internal fun CalendarMonth(
    state: DialogDateState,
    setup: DialogDateSetup,
    year: Int,
    month: Int
) {
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
            val items = mutableListOf<SimpleDate?>()
            val colIndexOfFirstDay = weekdays.indexOf(firstDayOfMonth.value.weekday)
            val colIndexOfLastDay = weekdays.indexOf(lastDayOfMonth.value.weekday)
            repeat(colIndexOfFirstDay) {
                items.add(null)
            }
            repeat(lastDayOfMonth.value.day) {
                val col = (colIndexOfFirstDay + it) % 7
                val date = SimpleDate(
                    firstDayOfMonth.value.year,
                    firstDayOfMonth.value.month,
                    it + 1,
                    weekdays[col]
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
                CalendarCellHeader(weekdays[it], modifierHeader)
            }
        }
        repeat(rows) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(7) { col ->
                    CalendarCell(state, items.value[row * 7 + col], today.value, modifierCell)
                }
            }
        }
    }
}

@Composable
private fun CalendarCellHeader(
    dayOfWeek: Int,
    modifier: Modifier
) {
    val text = DateUtil.getWeekDayInfo(dayOfWeek)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text, maxLines = 1)
    }
}

@Composable
private fun CalendarCell(
    state: DialogDateState,
    date: SimpleDate?,
    today: SimpleDate,
    modifier: Modifier
) {
    if (date == null) {
        Spacer(
            modifier = modifier
        )
    } else {
        if (date.isEqual(state)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(date.day.toString(), color = MaterialTheme.colorScheme.onPrimary)
            }
        } else {
            val isToday = today == date
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .border(
                        if (isToday) 2.dp else 1.dp,
                        if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    )
                    .clickable {
                        state.update(date)
                    }
            ) {
                Text(date.day.toString())
            }
        }
    }
}
