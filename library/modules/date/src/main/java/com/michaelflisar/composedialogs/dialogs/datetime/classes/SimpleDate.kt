package com.michaelflisar.composedialogs.dialogs.datetime.classes

import com.michaelflisar.composedialogs.dialogs.datetime.DialogDateState

internal data class SimpleDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val weekday: Int
) {

    fun isEqual(selected: DialogDateState?) : Boolean {
        if (selected == null)
            return false
        return year == selected.year.value && month == selected.month.value && day == selected.day.value
    }
}