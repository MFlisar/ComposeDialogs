package com.michaelflisar.composedialogs.dialogs.input.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.input.DialogInput
import com.michaelflisar.composedialogs.dialogs.input.DialogInputValidator

@Composable
fun DialogInputTextField(
    modifier: Modifier = Modifier,
    value: MutableState<String>,
    label: String = "",
    placeholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.Body),
    validator: DialogInputValidator = DialogInputValidator(),
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> }
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        if (requestFocus)
            focusRequester.requestFocus()
    }
    Column(
        modifier = modifier
    ) {
        // code for cursor position copied from
        // https://gist.github.com/Zhuinden/ab065534bbf73d7e6de83b5a39366c24
        // and extended afterwards
        var textFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = value.value,
                    selection = if (value.value.isEmpty())
                        TextRange.Zero
                    else {
                        when (selectionState) {
                            DialogInput.SelectionState.CursorEnd -> TextRange(
                                value.value.length,
                                value.value.length
                            )

                            DialogInput.SelectionState.Default -> TextRange.Zero
                            DialogInput.SelectionState.SelectAll -> TextRange(0, value.value.length)
                            is DialogInput.SelectionState.Selection -> TextRange(
                                selectionState.start.coerceIn(
                                    0,
                                    value.value.length
                                ), selectionState.end.coerceIn(0, value.value.length)
                            )
                        }
                    }
                )
            )
        }
        val textFieldValue = textFieldValueState.copy(text = value.value)
        SideEffect {
            if (textFieldValue.selection != textFieldValueState.selection ||
                textFieldValue.composition != textFieldValueState.composition
            ) {
                textFieldValueState = textFieldValue
            }
        }
        var lastTextValue by remember(value.value) { mutableStateOf(value.value) }

        ComposeDialogsUI.provider.OutlinedTextField(
            value = textFieldValue,
            modifier = modifier
                .focusRequester(focusRequester),
            enabled = enabled,
            onValueChange = {
                textFieldValueState = it
                val changed = lastTextValue != it.text
                lastTextValue = it.text
                if (changed) {
                    validator.check(it.text)
                    value.value = it.text
                    val valid = validator.isValid()
                    onStateChanged(valid, it.text)
                }
            },
            isError = !validator.isValid(),
            singleLine = maxLines == 1,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            textStyle = textStyle,
            prefix = if (prefix.isNotEmpty()) {
                @Composable {
                    ComposeDialogsUI.provider.Text(
                        prefix,
                        ComposeDialogsUI.TextRole.Body,
                        Modifier
                    )
                }
            } else null,
            suffix = if (suffix.isNotEmpty()) {
                @Composable {
                    ComposeDialogsUI.provider.Text(
                        suffix,
                        ComposeDialogsUI.TextRole.Body,
                        Modifier
                    )
                }
            } else null,
            label = if (label.isNotEmpty()) {
                @Composable {
                    ComposeDialogsUI.provider.Text(
                        label,
                        ComposeDialogsUI.TextRole.Label,
                        Modifier
                    )
                }
            } else null,
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    ComposeDialogsUI.provider.Text(
                        placeholder,
                        ComposeDialogsUI.TextRole.Body,
                        Modifier
                    )
                }
            } else null,
            trailingIcon = if (clearable) {
                @Composable {
                    when {
                        value.value.isNotEmpty() -> ComposeDialogsUI.provider.IconButton(
                            onClick = {
                                value.value = ""
                                validator.check("")
                                val valid = validator.isValid()
                                onStateChanged(valid, "")
                                focusRequester.requestFocus()
                            },
                            enabled = true,
                            modifier = Modifier
                        ) {
                            ComposeDialogsUI.provider.Icon(
                                icon = ComposeDialogsUI.IconType.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier,
                                tint = ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
                            )
                        }
                    }
                }
            } else null,
            supportingText = if (enabled && validator.getErrorMessage().isNotEmpty()) {
                {
                    ComposeDialogsUI.provider.Text(
                        text = validator.getErrorMessage(),
                        role = ComposeDialogsUI.TextRole.BodySmall,
                        modifier = Modifier,
                        color = ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.Error),
                        textAlign = null,
                        fontWeight = null
                    )
                }
            } else null
        )
    }
}