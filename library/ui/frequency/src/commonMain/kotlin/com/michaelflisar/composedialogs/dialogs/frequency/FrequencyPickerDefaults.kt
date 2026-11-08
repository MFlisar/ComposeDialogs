package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.michaelflisar.composedialogs.frequency.resources.Res
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_day
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_day_of_month
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_day_of_week
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_day_plural
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_frequency
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_interval
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_invalid_interval
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_month
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_month_of_year
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_month_plural
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_time
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_week
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_week_plural
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_year
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_year_plural
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_friday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_monday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_saturday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_sunday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_thursday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_tuesday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_weekday_wednesday
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_april
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_august
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_december
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_february
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_january
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_july
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_june
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_march
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_may
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_november
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_october
import com.michaelflisar.composedialogs.frequency.resources.composedialogs_frequency_monthname_september
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

@Immutable
class FrequencyPickerTexts internal constructor(
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

@Stable
object FrequencyPickerDefaults {
    @Composable
    fun texts(
        interval: String = stringResource(Res.string.composedialogs_frequency_interval),
        invalidInterval: String = stringResource(Res.string.composedialogs_frequency_invalid_interval),
        type: String = stringResource(Res.string.composedialogs_frequency_frequency),
        monthOfYear: String = stringResource(Res.string.composedialogs_frequency_month_of_year),
        dayOfMonth: String = stringResource(Res.string.composedialogs_frequency_day_of_month),
        dayOfWeek: String = stringResource(Res.string.composedialogs_frequency_day_of_week),
        time: String = stringResource(Res.string.composedialogs_frequency_time),
        nameOfType: ((type: FrequencyPickerType, interval: Int) -> String)? = null,
        nameOfDayOfWeek: ((day: DayOfWeek) -> String)? = null,
        nameOfMonth: ((month: Month) -> String)? = null
    ): FrequencyPickerTexts {
        val day = stringResource(Res.string.composedialogs_frequency_day)
        val dayPlural = stringResource(Res.string.composedialogs_frequency_day_plural)
        val week = stringResource(Res.string.composedialogs_frequency_week)
        val weekPlural = stringResource(Res.string.composedialogs_frequency_week_plural)
        val month = stringResource(Res.string.composedialogs_frequency_month)
        val monthPlural = stringResource(Res.string.composedialogs_frequency_month_plural)
        val year = stringResource(Res.string.composedialogs_frequency_year)
        val yearPlural = stringResource(Res.string.composedialogs_frequency_year_plural)
        val monday = stringResource(Res.string.composedialogs_frequency_weekday_monday)
        val tuesday = stringResource(Res.string.composedialogs_frequency_weekday_tuesday)
        val wednesday = stringResource(Res.string.composedialogs_frequency_weekday_wednesday)
        val thursday = stringResource(Res.string.composedialogs_frequency_weekday_thursday)
        val friday = stringResource(Res.string.composedialogs_frequency_weekday_friday)
        val saturday = stringResource(Res.string.composedialogs_frequency_weekday_saturday)
        val sunday = stringResource(Res.string.composedialogs_frequency_weekday_sunday)
        val january = stringResource(Res.string.composedialogs_frequency_monthname_january)
        val february = stringResource(Res.string.composedialogs_frequency_monthname_february)
        val march = stringResource(Res.string.composedialogs_frequency_monthname_march)
        val april = stringResource(Res.string.composedialogs_frequency_monthname_april)
        val may = stringResource(Res.string.composedialogs_frequency_monthname_may)
        val june = stringResource(Res.string.composedialogs_frequency_monthname_june)
        val july = stringResource(Res.string.composedialogs_frequency_monthname_july)
        val august = stringResource(Res.string.composedialogs_frequency_monthname_august)
        val september = stringResource(Res.string.composedialogs_frequency_monthname_september)
        val october = stringResource(Res.string.composedialogs_frequency_monthname_october)
        val november = stringResource(Res.string.composedialogs_frequency_monthname_november)
        val december = stringResource(Res.string.composedialogs_frequency_monthname_december)

        return FrequencyPickerTexts(
            interval = interval,
            invalidInterval = invalidInterval,
            type = type,
            monthOfYear = monthOfYear,
            dayOfMonth = dayOfMonth,
            dayOfWeek = dayOfWeek,
            time = time,
            nameOfType = nameOfType ?: { frequencyType, count ->
                val singularAndPlural = when (frequencyType) {
                    FrequencyPickerType.Daily -> day to dayPlural
                    FrequencyPickerType.Weekly -> week to weekPlural
                    FrequencyPickerType.Monthly -> month to monthPlural
                    FrequencyPickerType.Yearly -> year to yearPlural
                }
                if (count == 1) singularAndPlural.first else singularAndPlural.second
            },
            nameOfDayOfWeek = nameOfDayOfWeek ?: { dayOfWeek ->
                when (dayOfWeek) {
                    DayOfWeek.MONDAY -> monday
                    DayOfWeek.TUESDAY -> tuesday
                    DayOfWeek.WEDNESDAY -> wednesday
                    DayOfWeek.THURSDAY -> thursday
                    DayOfWeek.FRIDAY -> friday
                    DayOfWeek.SATURDAY -> saturday
                    DayOfWeek.SUNDAY -> sunday
                }
            },
            nameOfMonth = nameOfMonth ?: { month ->
                when (month) {
                    Month.JANUARY -> january
                    Month.FEBRUARY -> february
                    Month.MARCH -> march
                    Month.APRIL -> april
                    Month.MAY -> may
                    Month.JUNE -> june
                    Month.JULY -> july
                    Month.AUGUST -> august
                    Month.SEPTEMBER -> september
                    Month.OCTOBER -> october
                    Month.NOVEMBER -> november
                    Month.DECEMBER -> december
                }
            }
        )
    }
}
