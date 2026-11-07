package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T : Number> rememberDialogInputNumber(number: T): MutableState<T> =
    rememberSaveable { mutableStateOf(number) }

object DialogInputNumber {

    @Suppress("UNCHECKED_CAST")
    fun <T : Number> convert(value: T, input: String): T? {
        return when (value) {
            is Int -> input.toIntOrNull()
            is Double -> input.toDoubleOrNull()
            is Float -> input.toFloatOrNull()
            is Long -> input.toLongOrNull()
            else -> throw RuntimeException("No string to ${value::class.simpleName} converter defined!")
        } as T?
    }

    inline fun <reified T : Number> convert(input: String): T? {
        return when (T::class) {
            Int::class -> input.toIntOrNull()
            Double::class -> input.toDoubleOrNull()
            Float::class -> input.toFloatOrNull()
            Long::class -> input.toLongOrNull()
            else -> throw RuntimeException("No string to ${T::class.simpleName} converter defined!")
        } as T?
    }

    @Composable
    fun <T : Number> rememberDefaultValidator(value: T) = rememberDialogInputValidator(
        validate = {
            val converted = convert(value, it)
            if (converted == null) defaultInvalidNumberError(value)
            else DialogInputValidator.Result.Valid
        }
    )

    fun <T : Number> defaultInvalidNumberError(value: T) =
        DialogInputValidator.Result.Error("Invalid ${value::class.simpleName}!")

    inline fun <reified T : Number> defaultInvalidNumberError() =
        DialogInputValidator.Result.Error("Invalid ${T::class.simpleName}!")
}

object DialogInputNumberDefaults {
    @Composable
    fun textStyle() = DialogInputDefaults.textStyle()
}
