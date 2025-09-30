package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

internal object DropdownDefaults {

    @Composable
    fun Content(
        modifier: Modifier,
        text: String,
        leadingContent: @Composable () -> Unit = {}
    ) {
        val style1 = MaterialTheme.typography.titleSmall
        val style2 = LocalTextStyle.current.merge(fontSize = LocalTextStyle.current.fontSize * .8f)

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingContent()
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    style = style2
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
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                modifier = Modifier.weight(1f)
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val colors = OutlinedTextFieldDefaults.colors()
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
            colors = colors,
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
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val item = items.find { it.index == selected }
        content(
            Modifier.weight(1f),
            item
        )

        Icon(
            modifier = Modifier.rotate(rotation),
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = labelColor
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
    val scrollState = rememberScrollState()
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        scrollState = scrollState
    ) {
        filteredItems.forEach {
            DropdownMenuItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
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