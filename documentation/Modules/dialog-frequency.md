---
icon: material/puzzle
---

This shows a frequency selector dialog. It supports daily, weekly, monthly and yearly frequencies.

Check out the composable and it's documentation in the code snipplet below.

#### Example

<!-- snippet: Demos::demo-frequency -->
```kt
if (state.visible) {
    val frequency = rememberDialogFrequency(Frequency.Weekly(DayOfWeek.MONDAY, LocalTime(12, 0), 1))
    DialogFrequency(
        state = state,
        frequency = frequency,
        title = { Text("Frequency") },
        icon = icon,
        style = style,
        onEvent = { event ->
            showInfo("Event $event | frequency: ${frequency.value}")
        }
    )
}
```
<!-- endSnippet -->

#### Composable

<!-- snippet: DialogFrequency::constructor -->
```kt
/**
 * Shows a frequency dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param frequency the frequency state of the dialog
 * @param texts the texts ([DialogFrequency.Texts]) that are used inside this dialog - use [DialogFrequencyDefaults.texts] to provide your own data
 * @param supportedTypes the supported frequency types - default is all types
 * @param firstDayOffset the first day of the week - default is [DayOfWeek.MONDAY]
 */
@Composable
fun DialogFrequency(
    state: DialogState,
    // Custom - Required
    frequency: MutableState<Frequency>,
    // Custom - Optional
    texts: DialogFrequency.Texts = DialogFrequencyDefaults.texts(),
    supportedTypes: List<Frequency.Type> = Frequency.Type.entries,
    firstDayOffset: DayOfWeek = DayOfWeek.MONDAY,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
```
<!-- endSnippet -->

#### Frequency class

This class offers some helpful functions to calculate the next date based on a given start date.

<!-- snippet: Frequency::calcNextOccurrence -->
```kt
/**
 * Calculates the next occurrence of the event based on the frequency settings.
 *
 * @param from The starting point to calculate the next occurrence from.
 * @param timeZone The time zone to consider for the calculation (defaults to the system's current time zone).
 * @param offset An optional offset to adjust the calculation (default is 0).
 * @return The next occurrence as a LocalDateTime.
 */
abstract fun calcNextOccurrence(
    from: LocalDate,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    offset: Int = 0,
): LocalDateTime
```
<!-- endSnippet -->

<!-- snippet: Frequency::calcNextOccurrences -->
```kt
/**
 * Calculates the next [count] occurrences of the event based on the frequency settings.
 *
 * @param from The starting point to calculate the next occurrences from.
 * @param offset An optional offset to adjust the calculation (default is 0).
 * @param count The number of occurrences to calculate.
 * @param timeZone The time zone to consider for the calculation (defaults to the system's current time zone).
 * @return A list of the next occurrences as LocalDateTime objects.
 */
fun calcNextOccurrences(
    from: LocalDate,
    count: Int,
    offset: Int = 0,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): List<LocalDateTime>
```
<!-- endSnippet -->

Here's a small example how to use it:

```kotlin
val time = LocalTime(12, 0)
val frequencyDaily1 = Frequency.Daily(time, 1)
val frequencyWeekly1 = Frequency.Weekly(DayOfWeek.MONDAY, time, 1)
val frequencyMonthly1 = Frequency.Monthly(31, time, 1)
val frequencyYearly1 = Frequency.Yearly(Month.JANUARY, 31, time, 1)

// Friday, January 31, 2025
val date = LocalDate(2025, Month.JANUARY, 31)

val nextDaily1 = frequencyDaily1.calcNextOccurrence(date) // February 1, 2025
val nextWeekly1 = frequencyWeekly1.calcNextOccurrence(date) // February 3 (next monday after date)
val nextMonthly1 = frequencyMonthly1.calcNextOccurrence(date) // March 31, 2025 (31st of next month)
val nextYearly1 = frequencyYearly1.calcNextOccurrence(date) // January 31, 2026 (31st of January next year)

// TIPP: do not use calcNextOccurrence recursively to get multiple occurrences, use calcNextOccurrence(..., offset = n) instead
// this avoid issues for example with monthly frequencies where the next month does not have the same day (e.g. 31st) and similar!

// you can also get multiple occurrences at once:
val next3Daily1 = frequencyDaily1.calcNextOccurrences(date, 3) // February 1, 2, 3 of 2025
val next3Weekly1 = frequencyWeekly1.calcNextOccurrences(date, 3) // February 3, 10, 17 of 2025
val next3Monthly1 = frequencyMonthly1.calcNextOccurrences(date, 3) // March 31, April 30, May 31 of 2025
val next3Yearly1 = frequencyYearly1.calcNextOccurrences(date, 3) // January 31 of 2026, 2027, 2028

// TIPP2: calcNextOccurrences does also support an offset parameter
```

#### Screenshots

|                                                             |                                                        |                                                             |                                                             |
|-------------------------------------------------------------|--------------------------------------------------------|-------------------------------------------------------------|-------------------------------------------------------------|
| ![Screenshot](../screenshots/frequency/demo_frequency1.png) | ![Screenshot](../screenshots/frequency/demo_frequency2.png) | ![Screenshot](../screenshots/frequency/demo_frequency3.png) | ![Screenshot](../screenshots/frequency/demo_frequency4.png) |
