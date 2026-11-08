package com.michaelflisar.composedialogs.dialogs.date.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogContentScrollableLazyColumn
import kotlinx.datetime.LocalDate
import com.michaelflisar.composedialogs.dialogs.date.*
import androidx.compose.material3.minimumInteractiveComponentSize as material3MinimumInteractiveComponentSize

internal object Material3DateUIProvider : DateUIProvider {
    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: DateTextStyle,
        color: Color?,
        textAlign: TextAlign?,
        maxLines: Int,
    ) {
        val textStyle = when (style) {
            DateTextStyle.BodySmall -> MaterialTheme.typography.bodySmall
            DateTextStyle.BodyMedium -> MaterialTheme.typography.bodyMedium
            DateTextStyle.TitleMedium -> MaterialTheme.typography.titleMedium
            DateTextStyle.HeadlineSmall -> MaterialTheme.typography.headlineSmall
        }
        androidx.compose.material3.Text(
            text = text,
            modifier = modifier,
            style = textStyle,
            color = color ?: Color.Unspecified,
            textAlign = textAlign,
            maxLines = maxLines
        )
    }

    @Composable
    override fun Icon(
        icon: DatePickerIcon,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color?,
    ) {
        val imageVector = when (icon) {
            DatePickerIcon.Previous -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
            DatePickerIcon.Next -> Icons.AutoMirrored.Filled.KeyboardArrowRight
            DatePickerIcon.DropDown -> Icons.Default.ArrowDropDown
            DatePickerIcon.Check -> Icons.Default.Check
        }
        androidx.compose.material3.Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint ?: LocalContentColor.current
        )
    }

    @Composable
    override fun IconButton(
        modifier: Modifier,
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit,
    ) {
        androidx.compose.material3.IconButton(
            modifier = modifier,
            onClick = onClick,
            enabled = enabled,
            content = content
        )
    }

    @Composable
    override fun Button(
        type: DatePickerButtonType,
        modifier: Modifier,
        onClick: () -> Unit,
        enabled: Boolean,
        content: @Composable () -> Unit,
    ) {
        when (type) {
            DatePickerButtonType.SelectedDate -> androidx.compose.material3.Button(
                modifier = modifier,
                onClick = onClick,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                content = { content() }
            )

            DatePickerButtonType.Today -> OutlinedButton(
                modifier = modifier,
                onClick = onClick,
                enabled = enabled,
                content = { content() }
            )
        }
    }

    @Composable
    override fun Divider(modifier: Modifier, vertical: Boolean) {
        if (vertical) androidx.compose.material3.VerticalDivider(modifier = modifier) else androidx.compose.material3.HorizontalDivider(
            modifier = modifier
        )
    }

    @Composable
    override fun color(color: DateColor): Color = when (color) {
        DateColor.Primary -> MaterialTheme.colorScheme.primary
        DateColor.OnPrimary -> MaterialTheme.colorScheme.onPrimary
        DateColor.Outline -> MaterialTheme.colorScheme.outline
    }

    @Composable
    override fun smallShape(): Shape = MaterialTheme.shapes.small

    override fun Modifier.minimumInteractiveComponentSize(): Modifier {
        return material3MinimumInteractiveComponentSize()
    }

    @Composable
    override fun ScrollableList(state: LazyListState, content: LazyListScope.() -> Unit) {
        DialogContentScrollableLazyColumn(state = state, content = content)
    }

    @Composable
    override fun CalendarCell(
        date: LocalDate,
        selected: Boolean,
        today: Boolean,
        modifier: Modifier,
        onClick: () -> Unit,
    ) {
        if (selected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                androidx.compose.material3.Text(
                    date.day.toString(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .border(
                        if (today) 2.dp else 1.dp,
                        if (today) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    )
                    .clickable(onClick = onClick)
            ) {
                androidx.compose.material3.Text(date.day.toString())
            }
        }
    }

    @Composable
    override fun DialogTitle(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.headlineSmall,
            content = content
        )
    }
}
