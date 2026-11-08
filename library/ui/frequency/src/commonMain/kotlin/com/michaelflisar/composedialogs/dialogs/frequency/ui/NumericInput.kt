package com.michaelflisar.composedialogs.dialogs.frequency.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider

@Composable
internal fun <T : Number> NumericInput(
    uiProvider: FrequencyUIProvider,
    title: String,
    value: MutableState<T?>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    NumericInput(uiProvider, title, value.value, modifier, readOnly) {
        value.value = it
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
internal  fun <T : Number> NumericInput(
    uiProvider: FrequencyUIProvider,
    title: String,
    value: T?,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onValueChange: (T?) -> Unit = {}
) {
    val text = remember { mutableStateOf(value?.toString() ?: "") }
    uiProvider.NumberInput(
        modifier = modifier,
        value = text.value,
        onValueChange = {
            text.value = it
            when (value) {
                is Int? -> onValueChange(it.toIntOrNull() as T?)
                is Double? -> onValueChange(it.toDoubleOrNull() as T?)
                is Float? -> onValueChange(it.toFloatOrNull() as T?)
                is Long? -> onValueChange(it.toLongOrNull() as T?)
                is Short? -> onValueChange(it.toShortOrNull() as T?)
                is Byte? -> onValueChange(it.toByteOrNull() as T?)
                else -> throw RuntimeException("Type not handled!")
            }
        },
        title = title,
        keyboardType = when (value) {
            is Double?,
            is Float? -> KeyboardType.Number
            is Int?,
            is Long?,
            is Short?,
            is Byte? -> KeyboardType.Decimal
            else -> throw RuntimeException("Type not handled!")
        },
        readOnly = readOnly
    )
}