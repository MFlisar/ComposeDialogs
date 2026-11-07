package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import com.michaelflisar.composedialogs.core.ui.DialogUI

internal object DropdownDefaults {

    @Composable
    fun Content(
        modifier: Modifier,
        text: String,
        leadingContent: @Composable () -> Unit = {}
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent()
            Column(modifier = Modifier.weight(1f)) {
                DialogUI.provider.Text(
                    text = text,
                    role = DialogUI.TextRole.BodySmall,
                    modifier = Modifier
                )
            }
        }

    }

    @Composable
    fun DropdownContent(
        text: String,
        selected: Boolean,
        leadingContent: @Composable () -> Unit = {}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent()
            DialogUI.provider.Text(
                text = text,
                role = DialogUI.TextRole.Body,
                modifier = Modifier.weight(1f),
                color = if (selected) {
                    DialogUI.provider.color(DialogUI.ColorRole.Primary)
                } else Color.Unspecified,
                textAlign = null,
                fontWeight = null
            )
        }
    }
}

internal object Dropdown {

    internal class Item<T>(
        val item: T & Any,
        val index: Int,
        val text: String,
        val textDropdown: String,
    )
}

@Composable
internal fun <T> Dropdown(
    modifier: Modifier = Modifier,
    title: String = "",
    items: List<T & Any>,
    selected: MutableState<T>,
    mapper: (item: T & Any, dropdown: Boolean) -> String,
    enabled: Boolean = true,
    content: @Composable (modifier: Modifier, item: T?, text: String) -> Unit = { modifier, item, text ->
        DropdownDefaults.Content(modifier, text)
    },
    dropdownContent: @Composable (item: T & Any, text: String, selected: Boolean) -> Unit = { item, text, selected ->
        DropdownDefaults.DropdownContent(text, selected)
    },
    onSelectionChanged: ((T & Any) -> Unit)? = null,
) {
    val selectedIndex = items.indexOf(selected.value)
    val dropdownItems by remember(items) {
        derivedStateOf {
            items.mapIndexed { index, item ->
                Dropdown.Item(
                    mapper(item, false),
                    index,
                    mapper(item, false),
                    mapper(item, true)
                )
            }
        }
    }
    val content: @Composable (modifier: Modifier, item: Dropdown.Item<String>?) -> Unit =
        { modifier, item ->
            content(
                modifier,
                item?.index?.takeIf { it != -1 }?.let { items[it] },
                item?.text ?: ""
            )
        }
    val dropdownContent: @Composable (item: Dropdown.Item<String>, selected: Boolean) -> Unit =
        { item, selected ->
            dropdownContent(items[item.index], item.textDropdown, selected)
        }
    DropdownImpl(
        modifier,
        title,
        dropdownItems,
        selectedIndex,
        enabled,
        content,
        dropdownContent
    ) { item ->
        val s = items[item.index]
        selected.value = s
        onSelectionChanged?.invoke(s)
    }
}

@Composable
private fun <T> DropdownImpl(
    modifier: Modifier = Modifier,
    title: String,
    items: List<Dropdown.Item<T>>,
    selected: Int,
    enabled: Boolean,
    content: @Composable (modifier: Modifier, item: Dropdown.Item<T>?) -> Unit,
    dropdownContent: @Composable (item: Dropdown.Item<T>, selected: Boolean) -> Unit,
    onSelectionChange: (item: Dropdown.Item<T>) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded.value) -180f else 0f)

    Box(
        modifier = modifier
    ) {
        OutlinedDecoratedContainer(
            title = title,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = null,
            leadingIcon = null,
            trailingIcon = null,
            onClick = {
                expanded.value = !expanded.value
            }
        ) {
            DropdownContent(
                // TODO: find out why extra padding is necessary + fix it
                Modifier.fillMaxWidth().then(OutlinedDecoratedContainer.MODIFIER_CORRECTION),
                rotation,
                items,
                selected,
                content
            )
        }
        DropdownDropdown(
            expanded,
            items,
            dropdownContent,
            selected,
            onSelectionChange
        )
    }


}

@Composable
private fun <T> DropdownContent(
    modifier: Modifier,
    rotation: Float,
    items: List<Dropdown.Item<T>>,
    selected: Int,
    content: @Composable (modifier: Modifier, item: Dropdown.Item<T>?) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val item = items.find { it.index == selected }
        content(
            Modifier.weight(1f),
            item
        )

        DialogUI.provider.Icon(
            modifier = Modifier.rotate(rotation),
            icon = DialogUI.IconType.ArrowDropDown,
            contentDescription = null,
            tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurfaceVariant)
        )
    }
}

@Composable
private fun <T> DropdownDropdown(
    expanded: MutableState<Boolean>,
    filteredItems: List<Dropdown.Item<T>>,
    dropdownContent: @Composable ((Dropdown.Item<T>, Boolean) -> Unit),
    selected: Int,
    onSelectionChange: (Dropdown.Item<T>) -> Unit
) {
    DialogUI.provider.DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        filteredItems.forEach {
            DialogUI.provider.DropdownMenuItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                selected = it.index == selected,
                text = {
                    dropdownContent(it, it.index == selected)
                },
                onClick = {
                    onSelectionChange(it)
                    expanded.value = false
                }
            )
        }
    }
}