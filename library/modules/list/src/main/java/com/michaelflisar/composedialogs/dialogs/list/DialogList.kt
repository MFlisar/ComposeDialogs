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
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.dialogs.list.defaults.DialogListContent
import com.michaelflisar.composedialogs.dialogs.list.defaults.DialogListTrailingContent

/**
 * Shows a dialog with a list and an optional filter option
 *
 * consider the overload with a lambda for the items parameter if items should be loaded lazily
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param items the list items
 * @param itemIdProvider the items to id lambda that is used to store selected item ids
 * @param itemContents the [DialogListItemContents] holding composables to customise the rendering of the list items - use [DialogListItemDefaultContent] or [DialogListItemContents] if you want to completely customise the items
 * @param selectionMode the [DialogListSelectionMode]
 * @param divider if true, a divider is shown between the list items
 * @param description a custom text that will be shown as description at the top of the dialog
 * @param filter the [FilterSetup] - if it is null, filtering is disabled
 */
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
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
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
        icon,
        style,
        buttons,
        options,
        onEvent
    )
}

/**
 * Shows a dialog with a list and an optional filter option
 *
 * consider the overload with a list if the items are just a simple list of items
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param itemsLoader the lambda that will return the items for this dialog
 * @param itemIdProvider the items to id lambda that is used to store selected item ids
 * @param itemContents the [DialogListItemContents] holding composables to customise the rendering of the list items - use [DialogListItemDefaultContent] or [DialogListItemContents] if you want to completely customise the items
 * @param selectionMode the [DialogListSelectionMode]
 * @param itemSaver the saver for the list items - if no itemSaver is provided, data won't be remembered as saveable and will be reloaded on recomposition (e.g. screen rotation)
 * @param loadingIndicator the composable that will be shown while items are loaded
 * @param divider if true, a divider is shown between the list items
 * @param description a custom text that will be shown as description at the top of the dialog
 * @param filter the [FilterSetup] - if it is null, filtering is disabled
 */
@Composable
fun <T> DialogList(
    state: DialogState,
    // Custom - Required
    itemsLoader: suspend () -> List<T>,
    itemIdProvider: (item: T) -> Int,
    itemContents: DialogListItemContents<T>,
    selectionMode: DialogListSelectionMode<T>,
    // Custom - Optional
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
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
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
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle,
    buttons: DialogButtons,
    options: Options,
    onEvent: (event: DialogEvent) -> Unit
) {
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
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

/**
 * selection mode for the list dialog
 */
sealed class DialogListSelectionMode<T> {

    /**
     * single selection mode for the list dialog
     *
     * @param selected holds the currently selected item id
     * @param selectOnRadioButtonClickOnly if true, only clicks on the radio button will select an item, otherwise a click on the item itself will select it as well
     */
    class SingleSelect<T>(
        val selected: MutableState<Int?>,
        val selectOnRadioButtonClickOnly: Boolean = true
    ) : DialogListSelectionMode<T>()

    /**
     * multi selection mode for the list dialog
     *
     * @param selected holds the currently selected item ids
     * @param selectOnCheckboxClickOnly if true, only clicks on the checkbox will select an item, otherwise a click on the item itself will select it as well
     * @param showSelectionCounter if true, the count of the selected items will be shown in the dialog
     */
    class MultiSelect<T>(
        val selected: MutableState<List<Int>>,
        val selectOnCheckboxClickOnly: Boolean = true,
        val showSelectionCounter: Boolean = false
    ) : DialogListSelectionMode<T>()

    /**
     * single click mode for the list dialog
     *
     * this mode will close the dialog as soon as a single item is selected
     *
     * @param onItemClicked the callback that will be used to emit the single item that was clicked before the dialog is dismissed
     */
    class SingleClickAndClose<T>(val onItemClicked: (item: T) -> Unit) :
        DialogListSelectionMode<T>()

    /**
     * multi click mode for the list dialog
     *
     * this mode will emit item clicked events whenever an item is clicked
     *
     * @param onItemClicked the callback that will be used to emit the click events
     */
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

/**
 * the content provider interface for a list item
 *
 * @property content the main content of a list item
 * @property iconContent the icon of a list item
 * @property trailingContent the trailing content of a list item
 */
interface DialogListItemContents<T> {
    val content: @Composable ColumnScope.(item: T) -> Unit
    val iconContent: @Composable ((item: T) -> Unit)?
    val trailingContent: @Composable (ColumnScope.(item: T) -> Unit)?
}

/**
 * the default content provider for a list item that will use the default composables to render the provided texts and icon
 *
 * @param text the item text
 * @param supportingText the optional supporting item text
 * @property trailingSupportingText the optional trailing supporting text
 * @property icon the optional icon composable
 */
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

/**
 * the default content provider for a list item that will use the default composables to render the provided texts and icon
 *
 * @param filter the filter lambda to determine if an item matches the filter (true) or not (false)
 * @param infoText the info text that shows how many items are filtered (if null this text is disabled)
 * @property keepSelectionForInvisibleItems if true, filtered out items stay selected, otherwise they will be deselected on filter changes whenever the are not part of the currently filtered items anymore
 * @property showClearIcon if true, the filter input field shows a clear icon to clear the filter
 */
class FilterSetup<T>(
    val filter: (filter: String, item: T) -> Boolean,
    val infoText: @Composable ((count: Int, total: Int) -> String)? = { count, total -> "$count/$total" },
    val keepSelectionForInvisibleItems: Boolean = true,
    val showClearIcon: Boolean = true
)