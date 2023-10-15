package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * function for creating a custom input validator
 *
 * @param validate the validation function returning a [DialogInputValidator.Result] (can be [DialogInputValidator.Result.Valid] or [DialogInputValidator.Result.Error])
 * @param state the initial [DialogInputValidator.Result]
 *
 * @return a [DialogInputValidator]
 */
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

/**
 * a custom input validator
 *
 * @param validate the validation function returning a [DialogInputValidator.Result] (can be [DialogInputValidator.Result.Valid] or [DialogInputValidator.Result.Error])
 * @param state the initial [DialogInputValidator.Result]
 */
class DialogInputValidator internal constructor(
    private val validate: (value: String) -> Result = { Result.Valid },
    private val state: MutableState<Result> = mutableStateOf(Result.Valid)
) {
    /**
     * @return a state holding the current result of this validator
     */
    fun state(): State<Result> = state

    /**
     * checks if the current state is valid
     *
     * @return true if the current state is valid
     */
    fun isValid() = state.value is Result.Valid


    /**
     * gets the current error message
     *
     * @return the message from an error state or an empty string for a valid state
     */
    fun getErrorMessage(): String {
        return when (val s = state.value) {
            is Result.Error -> s.message
            Result.Valid -> ""
        }
    }

    /**
     * runs the validation function for the provided value and saves the result inside the state
     */
    fun check(value: String) {
        state.value = validate(value)
    }

    /**
     * runs the validation function and returns its result (without changing this validators state)
     */
    fun isValid(value: String) = validate(value) is Result.Valid

    /**
     * Validator results
     */
    sealed class Result {
        /**
         * valid result
         */
        data object Valid : Result()

        /**
         * error result
         *
         * @param message the error message
         */
        class Error(val message: String) : Result()
    }
}