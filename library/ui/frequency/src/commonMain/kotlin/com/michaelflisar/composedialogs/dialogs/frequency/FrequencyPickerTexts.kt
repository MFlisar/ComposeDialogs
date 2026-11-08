package com.michaelflisar.composedialogs.dialogs.frequency

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

class FrequencyPickerTexts(
    val interval: String,
    val invalidInterval: String,
    val type: String,
    val monthOfYear: String,
    val dayOfMonth: String,
    val dayOfWeek: String,
    val time: String,
    val nameOfType: (type: FrequencyPickerType, interval: Int) -> String,
    val nameOfDayOfWeek: (day: DayOfWeek) -> String,
    val nameOfMonth: (month: Month) -> String
)
