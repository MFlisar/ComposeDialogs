package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

enum class FrequencyTextStyle {
    Current,
    CurrentSmall,
    TitleSmall
}

/** UI rendering contract for the frequency picker's labels and controls. */
interface FrequencyUIProvider {

    /** Groups a field label with its control, such as the interval label and number input. */
    @Composable
    fun LabeledField(
        label: String,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit
    )

    /** Renders selected values and options for picker dropdowns. */
    @Composable
    fun Text(
        text: String,
        modifier: Modifier,
        style: FrequencyTextStyle,
        color: Color?,
        textAlign: TextAlign?,
        minLines: Int,
        maxLines: Int
    )

    /** Renders the editable interval field. */
    @Composable
    fun NumberInput(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        title: String,
        keyboardType: KeyboardType,
        readOnly: Boolean
    )

    /** Renders the interval dropdown with inclusive bounds. */
    @Composable
    fun IntervalDropdown(
        modifier: Modifier,
        title: String,
        min: Int,
        max: Int,
        selected: Int,
        onItemSelected: (Int) -> Unit
    )

    /** Renders the read-only time slot that opens the time picker when tapped. */
    @Composable
    fun InputButton(
        modifier: Modifier,
        title: String,
        value: String,
        onClick: () -> Unit
    )

    /** Renders the time slot and its time-picker dialog using the supplied setup. */
    @Composable
    fun TimeInput(
        selectedTime: MutableState<LocalTime>,
        title: String,
        setup: TimePickerSetup,
        modifier: Modifier
    )

    /** Renders the weekday selector used for weekly frequencies. */
    @Composable
    fun DayOfWeekDropdown(
        modifier: Modifier,
        title: String,
        items: List<DayOfWeek>,
        selected: DayOfWeek,
        itemText: (DayOfWeek) -> String,
        onItemSelected: (DayOfWeek) -> Unit
    )

    /** Renders the month selector used for yearly frequencies. */
    @Composable
    fun MonthDropdown(
        modifier: Modifier,
        title: String,
        items: List<Month>,
        selected: Month,
        itemText: (Month) -> String,
        onItemSelected: (Month) -> Unit
    )

    /** Renders the day selector used for monthly and yearly frequencies. */
    @Composable
    fun DayOfMonthDropdown(
        modifier: Modifier,
        title: String,
        items: List<Int>,
        selected: Int,
        itemText: (Int) -> String,
        onItemSelected: (Int) -> Unit
    )

    /** Renders the selector for daily, weekly, monthly, or yearly frequency. */
    @Composable
    fun FrequencyTypeDropdown(
        modifier: Modifier,
        title: String,
        items: List<FrequencyPickerType>,
        selected: FrequencyPickerType,
        itemText: (FrequencyPickerType) -> String,
        onItemSelected: (FrequencyPickerType) -> Unit
    )

}
