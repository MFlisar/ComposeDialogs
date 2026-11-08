package com.michaelflisar.composedialogs.dialogs.frequency

/** Selects how the frequency interval is entered. Dropdown bounds are inclusive. */
sealed interface FrequencyIntervalInputType {
    object Input : FrequencyIntervalInputType

    /** Offers values from [min] through [max]; an existing out-of-range value is preserved. */
    data class Dropdown(
        val min: Int,
        val max: Int
    ) : FrequencyIntervalInputType {
        init {
            require(min > 0) { "min must be greater than zero" }
            require(max >= min) { "max must be greater than or equal to min" }
        }
    }
}
