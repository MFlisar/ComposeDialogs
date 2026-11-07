package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.frequency.DialogFrequency
import com.michaelflisar.composedialogs.dialogs.time.DialogTimeContent
import kotlinx.datetime.LocalTime

@Composable
internal fun Time(
    selectedTime: MutableState<LocalTime>,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier.Companion
) {
    val showDialogTime = remember { mutableStateOf(false) }
    InputButton(
        modifier = modifier,
        title = texts.time,
        value = selectedTime.value.toString(),
        onClick = {
            showDialogTime.value = true
        }
    )
    if (showDialogTime.value) {
        DialogUI.provider.Dialog(
            onDismissRequest = { showDialogTime.value = false }
        ) {
            Column {
                DialogUI.provider.Text(
                    text = texts.time,
                    role = DialogUI.TextRole.TitleSmall,
                    modifier = Modifier
                )
                DialogTimeContent(selectedTime)
            }
        }
    }
}