package com.michaelflisar.composedialogs.dialogs.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.michaelflisar.composedialogs.core.*

@Composable
fun DialogInput(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    input: MutableState<String>,
    inputLabel: String,
    // Custom - Optional
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = LocalTextStyle.current,
    validator: DialogInputValidator = DialogInputValidator(),
    onTextStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: DialogTitle = DialogDefaults.title(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        val modifier = when (style) {
            is DialogStyle.BottomSheet -> Modifier.fillMaxWidth()
            is DialogStyle.Dialog -> Modifier
        }
        DialogInput.InputText(
            modifier,
            input,
            inputLabel,
            singleLine,
            maxLines,
            minLines,
            keyboardOptions,
            enabled,
            clearable,
            prefix,
            suffix,
            textStyle,
            validator,
            onTextStateChanged
        )
    }
}

class DialogInputValidator(
    val error: MutableState<String?> = mutableStateOf(null),
    val validate: (value: String) -> String? = { null }
) {

    fun isValid() = error.value == null

    fun check(value: String) {
        error.value = validate(value)
    }

    fun test(value: String) = validate(value) == null

    fun getIntWithOffset(input: MutableState<TextFieldValue>, offset: Int): Int? {
        val current = input.value.text.toIntOrNull()
        return if (current != null) {
            val next = current + offset
            next
        } else null
    }

    fun testIntWithOffset(input: MutableState<TextFieldValue>, offset: Int): Boolean {
        val current = input.value.text.toIntOrNull()
        return if (current != null) {
            val next = current + offset
            val valid = test(next.toString())
            valid
        } else false
    }
}

object DialogInput {

    @Composable
    fun InputText(
        modifier: Modifier = Modifier,
        state: MutableState<String>,
        label: String = "",
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        minLines: Int = 1,
        keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        enabled: Boolean = true,
        clearable: Boolean = true,
        prefix: String = "",
        suffix: String = "",
        textStyle: TextStyle = LocalTextStyle.current,
        validator: DialogInputValidator = DialogInputValidator(),
        onStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> }
    ) {
        val focusRequester = FocusRequester()
        Column(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = state.value,
                modifier = modifier
                    .focusRequester(focusRequester),
                enabled = enabled,
                onValueChange = {
                    validator.check(it)
                    state.value = it
                    val valid = validator.isValid()
                    onStateChanged(valid, it)
                },
                isError = !validator.isValid(),
                prefix = if (prefix.isNotEmpty()) {
                    @Composable { Text(prefix, style = MaterialTheme.typography.bodyMedium) }
                } else null,
                suffix = if (suffix.isNotEmpty()) {
                    @Composable { Text(suffix, style = MaterialTheme.typography.bodyMedium) }
                } else null,
                keyboardOptions = keyboardOptions,
                label = if (label.isNotEmpty()) {
                    @Composable { Text(text = label) }
                } else null,
                placeholder = { Text(text = label) },
                minLines = minLines,
                maxLines = maxLines,
                singleLine = maxLines == 1,
                textStyle = textStyle,
                trailingIcon = if (clearable) {
                    @Composable {
                        when {
                            state.value.isNotEmpty() -> IconButton(onClick = {
                                state.value = ""
                                validator.check("")
                                val valid = validator.isValid()
                                onStateChanged(valid, "")
                                focusRequester.requestFocus()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    }
                } else null,
                supportingText = {
                    val error = validator.error.value
                    AnimatedVisibility(visible = error != null && enabled) {
                        Text(text = error ?: "")
                    }
                }
            )
        }
    }
}