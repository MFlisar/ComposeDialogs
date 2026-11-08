package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.michaelflisar.composedialogs.date.resources.Res
import com.michaelflisar.composedialogs.date.resources.composedialogs_date_today
import org.jetbrains.compose.resources.stringResource

@Immutable
class DatePickerTexts internal constructor(
    val today: String
)

@Stable
object DatePickerDefaults {
    @Composable
    fun texts(
        today: String = stringResource(Res.string.composedialogs_date_today)
    ) = DatePickerTexts(today = today)
}
