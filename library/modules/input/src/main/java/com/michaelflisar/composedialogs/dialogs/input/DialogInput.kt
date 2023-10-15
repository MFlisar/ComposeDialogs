package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogIcon
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.DialogTitleStyle
import com.michaelflisar.composedialogs.core.Options

@Composable
fun DialogInput(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    input: MutableState<String>,
    inputLabel: String,
    // Custom - Optional
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = LocalTextStyle.current,
    validator: DialogInputValidator = rememberDialogInputValidator(),
    requestFocus: Boolean = false,
    initialState: DialogInput.InitialState = DialogInput.InitialState.Default,
    onTextStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, titleStyle, icon, style, buttons, options, onEvent = onEvent) {
        val modifier = when (style) {
            is DialogStyle.BottomSheet -> Modifier.fillMaxWidth()
            is DialogStyle.Dialog -> Modifier
        }
        DialogInput.InputText(
            modifier,
            input,
            inputLabel,
            inputPlaceholder,
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
            requestFocus,
            initialState,
            onTextStateChanged
        )
    }
}

@Composable
fun rememberDialogInput(
    input: String
): MutableState<String> {
    return rememberSaveable { mutableStateOf(input) }
}

object DialogInput {

    sealed class InitialState {
        data object Default : InitialState()
        data object CursorEnd : InitialState()
        data object SelectAll : InitialState()
        class Selection(val start: Int, val end: Int) : InitialState()
    }

    @Composable
    fun InputText(
        modifier: Modifier = Modifier,
        state: MutableState<String>,
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
        textStyle: TextStyle = LocalTextStyle.current,
        validator: DialogInputValidator = DialogInputValidator(),
        requestFocus: Boolean = false,
        initialState: InitialState = InitialState.Default,
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
                        text = state.value,
                        selection = if (state.value.isEmpty())
                            TextRange.Zero
                        else {
                            when (initialState) {
                                InitialState.CursorEnd -> TextRange(
                                    state.value.length,
                                    state.value.length
                                )

                                InitialState.Default -> TextRange.Zero
                                InitialState.SelectAll -> TextRange(0, state.value.length)
                                is InitialState.Selection -> TextRange(
                                    initialState.start.coerceIn(
                                        0,
                                        state.value.length
                                    ), initialState.end.coerceIn(0, state.value.length)
                                )
                            }
                        }
                    )
                )
            }
            val textFieldValue = textFieldValueState.copy(text = state.value)
            SideEffect {
                if (textFieldValue.selection != textFieldValueState.selection ||
                    textFieldValue.composition != textFieldValueState.composition
                ) {
                    textFieldValueState = textFieldValue
                }
            }
            var lastTextValue by remember(state.value) { mutableStateOf(state.value) }

            OutlinedTextField(
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
                        state.value = it.text
                        val valid = validator.isValid()
                        onStateChanged(valid, it.text)
                    }
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
                placeholder = if (placeholder.isNotEmpty()) {
                    { Text(text = placeholder) }
                } else null,
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
                supportingText = if (enabled && validator.getErrorMessage().isNotEmpty()) {
                    {
                        val error = validator.getErrorMessage()
                        //AnimatedVisibility(visible = error.isNotEmpty()) {
                            Text(text = error)
                        //}
                    }
                } else null
            )
        }
    }
}