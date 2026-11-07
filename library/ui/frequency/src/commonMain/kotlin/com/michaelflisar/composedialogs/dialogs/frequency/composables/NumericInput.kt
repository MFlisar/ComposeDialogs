package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI

@Composable
internal fun <T : Number> NumericInput(
    title: String,
    value: MutableState<T?>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    NumericInput(title, value.value, modifier, readOnly) {
        value.value = it
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
internal  fun <T : Number> NumericInput(
    title: String,
    value: T?,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    onValueChange: (T?) -> Unit = {}
) {
    val text = remember { mutableStateOf(value?.toString() ?: "") }
    ComposeDialogsUI.provider.OutlinedTextField(
        modifier = modifier,
        value = text.value,
        enabled = true,
        readOnly = readOnly,
        isError = false,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = when (value) {
                is Double?,
                is Float? -> KeyboardType.Number
                is Int?,
                is Long?,
                is Short?,
                is Byte? -> KeyboardType.Decimal
                else -> throw RuntimeException("Type not handled!")
            }
        ),
        label = {
            ComposeDialogsUI.provider.Text(
                text = title,
                role = ComposeDialogsUI.TextRole.Label,
                modifier = Modifier
            )
        },
        placeholder = null,
        leadingIcon = null,
        trailingIcon = if (text.value.isNotEmpty()) {
            {
                ComposeDialogsUI.provider.Icon(
                    icon = ComposeDialogsUI.IconType.Clear,
                    contentDescription = "clear",
                    modifier = Modifier
                        .clip(CircleShape)
                        .focusProperties { canFocus = false }
                        .clickable {
                            text.value = ""
                            onValueChange(null)
                        },
                    tint = ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
                )
            }
        } else null,
        supportingText = null,
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
    )
}