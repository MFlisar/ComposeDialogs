package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberDialogInputValidator(
    validate: (value: String) -> DialogInputValidator.Result = { DialogInputValidator.Result.Valid },
    state: DialogInputValidator.Result = DialogInputValidator.Result.Valid
): DialogInputValidator {
    return DialogInputValidator(
        validate,
        remember {
            mutableStateOf(state)
        }
    )
}

class DialogInputValidator internal constructor(
    private val validate: (value: String) -> Result = { Result.Valid },
    private val state: MutableState<Result> = mutableStateOf(Result.Valid)
) {
    fun state(): State<Result> = state

    fun isValid() = state.value is Result.Valid
    fun getErrorMessage(): String {
        return when (val s = state.value) {
            is Result.Error -> s.message
            Result.Valid -> ""
        }
    }

    fun check(value: String) {
        state.value = validate(value)
    }

    fun isValid(value: String) = validate(value) is Result.Valid

    sealed class Result {
        data object Valid : Result()
        class Error(val message: String) : Result()
    }
}