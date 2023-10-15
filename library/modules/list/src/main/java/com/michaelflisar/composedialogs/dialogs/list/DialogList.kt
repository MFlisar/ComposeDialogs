package com.michaelflisar.composedialogs.dialogs.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogIcon
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.DialogTitleStyle
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.dialogs.list.defaults.DialogListContent
import com.michaelflisar.composedialogs.dialogs.list.defaults.DialogListTrailingContent

@Composable
fun <T> DialogList(
    state: DialogState,
    // Custom - Required
    items: List<T>,
    itemIdProvider: (item: T) -> Int,
    itemContents: DialogListItemContents<T>,
    selectionMode: DialogListSelectionMode<T>,
    // Custom - Optional
    divider: Boolean = false,
    description: String = "",
    filter: FilterSetup<T>? = null,
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    DialogList(
        state,
        DialogListItemProvider.List(items),
        itemIdProvider,
        itemContents,
        selectionMode,
        divider,
        description,
        filter,
        title,
        titleStyle,
        icon,
        style,
        buttons,
        options,
        onEvent
    )
}

@Composable
fun <T> DialogList(
    state: DialogState,
    // Custom - Required
    itemsLoader: suspend () -> List<T>,
    itemIdProvider: (item: T) -> Int,
    itemContents: DialogListItemContents<T>,
    selectionMode: DialogListSelectionMode<T>,
    // Custom - Optional
    // if no itemSaver is provided, data won't be remember as a saveable and will be reloaded on recomposition (e.g. screen rotation)
    itemSaver: Saver<MutableState<List<T>>, out Any>? = null,
    loadingIndicator: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    },
    divider: Boolean,
    description: String,
    filter: FilterSetup<T>? = null,
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    DialogList(
        state,
        DialogListItemProvider.Loader(loadingIndicator, itemsLoader, itemSaver),
        itemIdProvider,
        itemContents,
        selectionMode,
        divider,
        description,
        filter,
        title,
        titleStyle,
        icon,
        style,
        buttons,
        options,
        onEvent
    )
}

@Composable
private fun <T> DialogList(
    state: DialogState,
    // Custom - Required
    itemsProvider: DialogListItemProvider<T>,
    itemIdProvider: (item: T) -> Int,
    itemContents: DialogListItemContents<T>,
    selectionMode: DialogListSelectionMode<T>,
    // Custom - Optional
    divider: Boolean,
    description: String,
    filter: FilterSetup<T>? = null,
    // Base Dialog - Optional
    title: String,
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon?,
    style: DialogStyle,
    buttons: DialogButtons,
    options: Options,
    onEvent: (event: DialogEvent) -> Unit
) {
    Dialog(state, title, titleStyle, icon, style, buttons, options, onEvent = onEvent) {
        val items = when (itemsProvider) {
            is DialogListItemProvider.List -> remember { mutableStateOf(itemsProvider.items) }
            is DialogListItemProvider.Loader -> {
                if (itemsProvider.itemSaver != null) {
                    rememberSaveable(saver = itemsProvider.itemSaver) {
                        mutableStateOf(emptyList())
                    }
                } else remember { mutableStateOf(emptyList()) }
            }
        }

        val filterText = rememberSaveable {
            mutableStateOf("")
        }

        val filteredItemsLoaded =
            if (itemsProvider is DialogListItemProvider.Loader && itemsProvider.itemSaver != null) {
                rememberSaveable { mutableStateOf(false) }
            } else remember { mutableStateOf(true) }

        val filteredItems = remember(items.value, filterText, filter) {
            derivedStateOf {
                if (filter == null)
                    items.value
                else
                    items.value.filter { filter.filter.invoke(filterText.value, it) }
            }
        }

        LaunchedEffect(filteredItems.value, filteredItemsLoaded.value) {
            if (filteredItemsLoaded.value) {
                if (filter?.keepSelectionForInvisibleItems == false) {
                    DialogListUtil.ensureOnlyVisibleItemsAreSelected(
                        selectionMode,
                        filteredItems.value,
                        itemIdProvider
                    )
                }
            }
        }

        if (itemsProvider is DialogListItemProvider.Loader && !filteredItemsLoaded.value) {
            LaunchedEffect(Unit) {
                items.value = itemsProvider.loader()
                filteredItemsLoaded.value = true
            }
        }

        if (description.isNotEmpty()) {
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (filter?.infoText != null) {
            val info = filter.infoText.invoke(filteredItems.value.size, items.value.size)
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), value = filterText.value, onValueChange = {
                    filterText.value = it
                },
                supportingText = if (info.isNotEmpty()) {
                    {
                        Text(
                            modifier = Modifier.align(Alignment.End),
                            text = info,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else null,
                trailingIcon = if (filter.showClearIcon && filterText.value.isNotEmpty()) {
                    {
                        IconButton(
                            onClick = {
                                filterText.value = ""
                            }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                } else null
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectionMode is DialogListSelectionMode.MultiSelect && selectionMode.showSelectionCounter) {
            Text(
                modifier = Modifier.align(Alignment.End),
                text = "${selectionMode.selected.value.size}/${items.value.size}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            if (items.value.isEmpty() && itemsProvider is DialogListItemProvider.Loader) {
                item(key = "${this.javaClass.name}-PLACEHOLDER") {
                    itemsProvider.loadingIndicator()
                }
            }
            items(
                filteredItems.value,
                key = { itemIdProvider(it) }
            ) {
                Item(
                    state,
                    it,
                    itemIdProvider(it),
                    selectionMode,
                    itemContents
                )
                if (divider && it != filteredItems.value.last()) {
                    Divider(modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }

    }
}

@Composable
private fun <T> Item(
    state: DialogState,
    item: T,
    itemId: Int,
    selectionMode: DialogListSelectionMode<T>,
    itemContents: DialogListItemContents<T>
) {
    DialogListItem(
        item = item,
        itemId = itemId,
        state = state,
        selectionMode = selectionMode,
        content = { itemContents.content(this, item) },
        icon = if (itemContents.iconContent != null) {
            { itemContents.iconContent!!.invoke(item) }
        } else null,
        trailingContent = if (itemContents.trailingContent != null) {
            { itemContents.trailingContent!!.invoke(this, item) }
        } else null
    )
}

sealed class DialogListSelectionMode<T> {
    class SingleSelect<T>(
        val selected: MutableState<Int?>,
        val selectOnRadioButtonClickOnly: Boolean = true
    ) : DialogListSelectionMode<T>()

    class MultiSelect<T>(
        val selected: MutableState<List<Int>>,
        val selectOnCheckboxClickOnly: Boolean = true,
        val showSelectionCounter: Boolean = false
    ) : DialogListSelectionMode<T>()

    class SingleClickAndClose<T>(val onItemClicked: (item: T) -> Unit) :
        DialogListSelectionMode<T>()

    class MultiClick<T>(val onItemClicked: (item: T) -> Unit) :
        DialogListSelectionMode<T>()
}

internal sealed class DialogListItemProvider<T> {
    class List<T>(
        val items: kotlin.collections.List<T>
    ) : DialogListItemProvider<T>()

    class Loader<T>(
        val loadingIndicator: @Composable () -> Unit,
        val loader: suspend () -> kotlin.collections.List<T>,
        val itemSaver: Saver<MutableState<kotlin.collections.List<T>>, out Any>?
    ) : DialogListItemProvider<T>()
}

interface DialogListItemContents<T> {
    val content: @Composable ColumnScope.(item: T) -> Unit
    val iconContent: @Composable ((item: T) -> Unit)?
    val trailingContent: @Composable (ColumnScope.(item: T) -> Unit)?
}

class DialogListItemDefaultContent<T>(
    private val text: (item: T) -> String,
    private val supportingText: ((item: T) -> String)? = null,
    private val trailingSupportingText: ((item: T) -> String)? = null,
    private val icon: @Composable ((item: T) -> Unit)? = null
) : DialogListItemContents<T> {

    override val content: @Composable ColumnScope.(item: T) -> Unit =
        { DialogListContent(text(it), supportingText?.invoke(it)) }

    override val iconContent: @Composable ((item: T) -> Unit)? = if (icon != null) {
        { icon.invoke(it) }
    } else null

    override val trailingContent: @Composable (ColumnScope.(item: T) -> Unit)? =
        if (trailingSupportingText != null) {
            { DialogListTrailingContent(trailingSupportingText.invoke(it)) }
        } else null
}

class FilterSetup<T>(
    val filter: (filter: String, item: T) -> Boolean,
    val infoText: @Composable ((count: Int, total: Int) -> String)? = { count, total -> "$count/$total" },
    val keepSelectionForInvisibleItems: Boolean = true,
    val showClearIcon: Boolean = true
)