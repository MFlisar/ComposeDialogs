package com.michaelflisar.composedialogs.dialogs.time.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal object TimeUtil {

    @OptIn(ExperimentalTime::class)
    fun now(): LocalDateTime {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault())
    }
}