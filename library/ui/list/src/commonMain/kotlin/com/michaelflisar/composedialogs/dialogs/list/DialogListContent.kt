package com.michaelflisar.composedialogs.dialogs.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.list.composables.DialogListItem

/**
 * Displays a list and manages filtering, loading, and selection without a dialog container.
 */
@Composable
fun <T> DialogListContent(
    items: List<T>,
    key: (item: T) -> Int,
    content: @Composable (item: T, context: DialogList.ItemContext) -> Unit,
    selectionMode: DialogList.SelectionMode<T>,
    divider: @Composable (() -> Unit)? = null,
    description: String = "",
    filter: DialogList.Filter<T>? = null,
    onDismiss: () -> Unit = {},
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    DialogListContentImpl(
        itemsProvider = remember(items) { DialogList.ItemProvider.List(items) },
        key = key,
        content = content,
        selectionMode = selectionMode,
        divider = divider,
        description = description,
        filter = filter,
        onDismiss = onDismiss,
        onEvent = onEvent
    )
}

/**
 * Loads and displays a list without a dialog container.
 */
@Composable
fun <T> DialogListContent(
    items: suspend () -> List<T>,
    key: (item: T) -> Int,
    content: @Composable (item: T, context: DialogList.ItemContext) -> Unit,
    selectionMode: DialogList.SelectionMode<T>,
    itemSaver: Saver<MutableState<List<T>>, out Any>? = null,
    loadingIndicator: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            DialogUI.provider.ProgressIndicator(
                type = DialogUI.ProgressIndicatorType.Circular,
                progress = null,
                modifier = Modifier
            )
        }
    },
    divider: @Composable (() -> Unit)? = null,
    description: String = "",
    filter: DialogList.Filter<T>? = null,
    onDismiss: () -> Unit = {},
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    DialogListContentImpl(
        itemsProvider = remember(items, loadingIndicator, itemSaver) {
            DialogList.ItemProvider.Loader(loadingIndicator, items, itemSaver)
        },
        key = key,
        content = content,
        selectionMode = selectionMode,
        divider = divider,
        description = description,
        filter = filter,
        onDismiss = onDismiss,
        onEvent = onEvent
    )
}

@Composable
internal fun <T> DialogListContentImpl(
    itemsProvider: DialogList.ItemProvider<T>,
    key: (item: T) -> Int,
    content: @Composable (item: T, context: DialogList.ItemContext) -> Unit,
    selectionMode: DialogList.SelectionMode<T>,
    divider: @Composable (() -> Unit)?,
    description: String,
    filter: DialogList.Filter<T>?,
    onDismiss: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    val itemsState = when (itemsProvider) {
        is DialogList.ItemProvider.List -> remember(itemsProvider) { mutableStateOf(itemsProvider.items) }
        is DialogList.ItemProvider.Loader -> {
            if (itemsProvider.itemSaver != null) {
                rememberSaveable(saver = itemsProvider.itemSaver) { mutableStateOf(emptyList()) }
            } else {
                remember { mutableStateOf(emptyList()) }
            }
        }
    }

    val filterText = rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    val itemsLoaded = rememberSaveable { mutableStateOf(itemsProvider is DialogList.ItemProvider.List) }
    val filteredItems = remember(itemsState.value, filterText.value, filter) {
        derivedStateOf {
            if (filter == null) itemsState.value
            else itemsState.value.filter { filter.filter(filterText.value, it) }
        }
    }

    LaunchedEffect(filteredItems.value, itemsLoaded.value) {
        if (itemsLoaded.value && filter?.keepSelectionForInvisibleItems == false) {
            DialogListUtil.ensureOnlyVisibleItemsAreSelected(selectionMode, filteredItems.value, key)
        }
    }

    if (itemsProvider is DialogList.ItemProvider.Loader && !itemsLoaded.value) {
        LaunchedEffect(Unit) {
            itemsState.value = itemsProvider.loader()
            itemsLoaded.value = true
        }
    }

    Column {
        if (description.isNotEmpty()) {
            DialogUI.provider.Text(
                text = description,
                role = DialogUI.TextRole.Body,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (filter?.infoText != null) {
            val info = filter.infoText.invoke(filteredItems.value.size, itemsState.value.size)
            DialogUI.provider.OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                value = filterText.value,
                onValueChange = { filterText.value = it },
                enabled = true,
                readOnly = false,
                isError = false,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default,
                label = null,
                placeholder = null,
                leadingIcon = null,
                supportingText = if (info.isNotEmpty()) {
                    {
                        DialogUI.provider.Text(
                            modifier = Modifier.align(Alignment.End),
                            text = info,
                            role = DialogUI.TextRole.BodySmall
                        )
                    }
                } else null,
                trailingIcon = if (filter.showClearIcon && filterText.value.isNotEmpty()) {
                    {
                        DialogUI.provider.IconButton(
                            onClick = { filterText.value = "" },
                            enabled = true,
                            modifier = Modifier
                        ) {
                            DialogUI.provider.Icon(
                                icon = DialogUI.IconType.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier,
                                tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
                            )
                        }
                    }
                } else null
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectionMode is DialogList.SelectionMode.MultiSelect && selectionMode.showSelectionCounter) {
            DialogUI.provider.Text(
                modifier = Modifier.align(Alignment.End),
                text = "${selectionMode.selected.value.size}/${itemsState.value.size}",
                role = DialogUI.TextRole.BodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn(state = listState, modifier = Modifier.padding(vertical = 8.dp)) {
            if (itemsState.value.isEmpty() && itemsProvider is DialogList.ItemProvider.Loader) {
                item(key = "DialogList-PLACEHOLDER") { itemsProvider.loadingIndicator() }
            }

            items(filteredItems.value, key = { key(it) }) { item ->
                DialogListItem(
                    item = item,
                    itemKey = key(item),
                    selectionMode = selectionMode,
                    content = content,
                    onDismiss = onDismiss,
                    onEvent = onEvent
                )
                if (divider != null && item != filteredItems.value.last()) {
                    divider()
                }
            }
        }
    }
}
