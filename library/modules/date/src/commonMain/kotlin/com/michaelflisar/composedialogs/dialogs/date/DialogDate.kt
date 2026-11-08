package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerRange
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerSetup
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

// begin-snippet: DialogDate::constructor
/**
 * Shows a dialog with an input field
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param date the selected date state
 * @param dateRange the supported [DialogDate.Range] - use [DialogDateDefaults.dateRange] to provide your own data
 * @param setup the [DialogDate.Setup] - use [DialogDateDefaults.setup] to provide your own data
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogDate(
    state: BaseDialogState,
    date: MutableState<LocalDate>,
    dateRange: DialogDate.Range = DialogDateDefaults.dateRange(),
    setup: DialogDate.Setup = DialogDateDefaults.setup(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    val landscape = isLandscape()
    Dialog(
        state,
        title?.takeIf { !landscape },
        icon?.takeIf { !landscape },
        style,
        buttons,
        options,
        onEvent = onEvent
    ) {
        DatePicker(
            uiProvider = Material3DateUIProvider,
            date = date,
            dateRange = DatePickerRange(dateRange.years),
            setup = DatePickerSetup(
                setup.buttonToday,
                setup.firstDayOfWeek,
                setup.formatterWeekDayLabel,
                setup.formatterSelectedDate,
                setup.formatterSelectedMonth,
                setup.formatterSelectedYear,
                setup.formatterMonthSelectorList,
                setup.formatterYearSelectorList,
                setup.dateCellHeight,
                setup.showNextPreviousMonthButtons,
                setup.showNextPreviousYearButtons
            ),
            landscape = landscape,
            title = title,
            icon = icon
        )
    }
}
// end-snippet

@Stable
object DialogDate {
    @Immutable
    class Setup internal constructor(
        val buttonToday: (@Composable () -> Unit)?,
        val firstDayOfWeek: DayOfWeek,
        val formatterWeekDayLabel: @Composable (date: DayOfWeek) -> String,
        val formatterSelectedDate: @Composable (date: LocalDate) -> String,
        val formatterSelectedMonth: @Composable (month: Month) -> String,
        val formatterSelectedYear: @Composable (year: Int) -> String,
        val formatterMonthSelectorList: @Composable (month: Month) -> String,
        val formatterYearSelectorList: @Composable (year: Int) -> String,
        val dateCellHeight: Dp,
        val showNextPreviousMonthButtons: Boolean,
        val showNextPreviousYearButtons: Boolean
    )

    @Immutable
    class Range internal constructor(
        val years: IntRange
    )

    val LocalDateSaver = Saver<MutableState<LocalDate>, Long>(
        save = { it.value.toEpochDays() },
        restore = { mutableStateOf(LocalDate.fromEpochDays(it)) }
    )
}

/**
 * @param date the initial date
 *
 * @return a state holding the current date value
 */
@Composable
fun rememberDialogDate(
    date: LocalDate = DateUtil.today()
): MutableState<LocalDate> {
    return rememberSaveable(
        saver = DialogDate.LocalDateSaver
    ) {
        mutableStateOf(date)
    }
}
