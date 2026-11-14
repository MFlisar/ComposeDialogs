package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import kotlinx.datetime.LocalDate

enum class DatePickerIcon {
    Previous,
    Next,
    DropDown,
    Check
}

enum class DatePickerButtonType {
    SelectedDate,
    Today
}

enum class DateTextStyle {
    BodySmall,
    BodyMedium,
    TitleMedium,
    HeadlineSmall
}

enum class DateColor {
    Primary,
    OnPrimary,
    Outline
}

/** UI rendering contract for the date picker's calendar and selection controls. */
interface DateUIProvider {

    /** Renders calendar headings, date labels, and year/month selection entries. */
    @Composable
    fun Text(
        text: String,
        modifier: Modifier,
        style: DateTextStyle,
        color: Color?,
        textAlign: TextAlign?,
        maxLines: Int
    )

    /** Renders calendar navigation, dropdown, and selected-entry icons. */
    @Composable
    fun Icon(
        icon: DatePickerIcon,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color?
    )

    /** Renders the previous/next navigation buttons in the calendar header. */
    @Composable
    fun IconButton(
        modifier: Modifier,
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit
    )

    /** Renders the selected-date or Today button, styled by its semantic [type]. */
    @Composable
    fun Button(
        type: DatePickerButtonType,
        modifier: Modifier,
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit
    )

    /** Separates the calendar header from the year/month selection list. */
    @Composable
    fun Divider(modifier: Modifier, vertical: Boolean)

    /** Returns the theme color for a semantic date-picker color role. */
    @Composable
    fun color(color: DateColor): Color

    /** Returns the compact shape used by calendar header and selection rows. */
    @Composable
    fun smallShape(): Shape

    /** Applies a minimum touch-target size to year/month selection rows. */
    @Composable
    fun Modifier.minimumInteractiveComponentSize(): Modifier

    /** Renders the scrolling list used to select a year or month. */
    @Composable
    fun ScrollableList(
        state: LazyListState,
        content: LazyListScope.() -> Unit
    )

    /** Renders one calendar day, including selected/today styling and click behavior. */
    @Composable
    fun CalendarCell(
        date: LocalDate,
        selected: Boolean,
        today: Boolean,
        modifier: Modifier,
        onClick: () -> Unit
    )
}
