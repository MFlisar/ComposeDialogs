package com.michaelflisar.composedialogs.dialogs.frequency.internal

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogInputButton
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogLabeledField
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogNumberInput
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogTextDropdown
import com.michaelflisar.composedialogs.dialogs.time.DialogTime
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import com.michaelflisar.composedialogs.dialogs.time.rememberTimePickerTime
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import kotlinx.datetime.LocalTime
import com.michaelflisar.composedialogs.dialogs.frequency.*

internal object Material3FrequencyUIProvider : FrequencyUIProvider {

    @Composable
    override fun LabeledField(
        label: String,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit
    ) {
        InternalDialogLabeledField(
            label = label,
            modifier = modifier,
            errorMessage = errorMessage,
            labelTextAlign = TextAlign.Center,
            content = content
        )
    }

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: FrequencyTextStyle,
        color: Color?,
        textAlign: TextAlign?,
        minLines: Int,
        maxLines: Int,
    ) {
        val textStyle = when (style) {
            FrequencyTextStyle.Current -> LocalTextStyle.current
            FrequencyTextStyle.CurrentSmall -> LocalTextStyle.current.merge(
                fontSize = LocalTextStyle.current.fontSize * .8f
            )

            FrequencyTextStyle.TitleSmall -> MaterialTheme.typography.titleSmall
        }
        androidx.compose.material3.Text(
            text = text,
            modifier = modifier,
            style = textStyle,
            color = color ?: Color.Unspecified,
            textAlign = textAlign,
            minLines = minLines,
            maxLines = maxLines
        )
    }

    @Composable
    override fun NumberInput(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        title: String,
        keyboardType: KeyboardType,
        readOnly: Boolean,
    ) {
        InternalDialogNumberInput(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            title = title,
            keyboardType = keyboardType,
            readOnly = readOnly
        )
    }

    @Composable
    override fun IntervalDropdown(
        modifier: Modifier,
        title: String,
        min: Int,
        max: Int,
        selected: Int,
        onItemSelected: (Int) -> Unit
    ) {
        val items = remember(min, max) { (min..max).toList() }
        InternalDialogTextDropdown(
            modifier = modifier,
            title = title,
            items = items,
            selected = selected,
            itemText = { it.toString() },
            onItemSelected = onItemSelected,
            selectedText = selected.toString(),
            selectedTextAlign = TextAlign.Center,
            selectedTextStyle = MaterialTheme.typography.bodyLarge,
            itemTextStyle = MaterialTheme.typography.bodyLarge
        )
    }

    @Composable
    override fun InputButton(
        modifier: Modifier,
        title: String,
        value: String,
        onClick: () -> Unit,
    ) {
        InternalDialogInputButton(
            modifier = modifier,
            title = title,
            value = value,
            onClick = onClick
        )
    }

    @Composable
    override fun TimeInput(
        selectedTime: LocalTime,
        onTimeSelected: (LocalTime) -> Unit,
        title: String,
        setup: TimePickerSetup,
        modifier: Modifier,
    ) {
        val selectedTimeState = rememberTimePickerTime(selectedTime)
        LaunchedEffect(selectedTime) {
            selectedTimeState.value = selectedTime
        }
        LaunchedEffect(selectedTimeState.value) {
            onTimeSelected(selectedTimeState.value)
        }
        val dialogState = rememberDialogState()
        InputButton(
            modifier = modifier,
            title = title,
            value = selectedTimeState.value.toString(),
            onClick = { dialogState.show() }
        )
        if (dialogState.visible) {
            DialogTime(
                state = dialogState,
                time = selectedTimeState,
                setup = setup,
                title = { Text(title) }
            )
        }
    }

    @Composable
    override fun DayOfWeekDropdown(
        modifier: Modifier,
        title: String,
        items: List<kotlinx.datetime.DayOfWeek>,
        selected: kotlinx.datetime.DayOfWeek,
        itemText: (kotlinx.datetime.DayOfWeek) -> String,
        onItemSelected: (kotlinx.datetime.DayOfWeek) -> Unit,
    ) {
        DropdownImpl(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    override fun MonthDropdown(
        modifier: Modifier,
        title: String,
        items: List<kotlinx.datetime.Month>,
        selected: kotlinx.datetime.Month,
        itemText: (kotlinx.datetime.Month) -> String,
        onItemSelected: (kotlinx.datetime.Month) -> Unit,
    ) {
        DropdownImpl(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    override fun DayOfMonthDropdown(
        modifier: Modifier,
        title: String,
        items: List<Int>,
        selected: Int,
        itemText: (Int) -> String,
        onItemSelected: (Int) -> Unit,
    ) {
        DropdownImpl(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    override fun FrequencyTypeDropdown(
        modifier: Modifier,
        title: String,
        items: List<Frequency.Type>,
        selected: Frequency.Type,
        itemText: (Frequency.Type) -> String,
        onItemSelected: (Frequency.Type) -> Unit,
    ) {
        DropdownImpl(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    private fun <T : Any> DropdownImpl(
        modifier: Modifier,
        title: String,
        items: List<T>,
        selected: T,
        itemText: (T) -> String,
        onItemSelected: (T) -> Unit,
    ) {
        val currentTextStyle = LocalTextStyle.current
        InternalDialogTextDropdown(
            modifier = modifier,
            title = title,
            items = items,
            selected = selected,
            itemText = itemText,
            onItemSelected = onItemSelected,
            selectedTextStyle = currentTextStyle.merge(
                fontSize = currentTextStyle.fontSize * .8f
            ),
            itemTextStyle = currentTextStyle
        )
    }

}
