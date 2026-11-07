package com.michaelflisar.composedialogs.dialogs.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import androidx.compose.runtime.saveable.Saver

// begin-snippet: DialogList::constructor1
/**
 * Displays a dialog containing a selectable or clickable list of items.
 *
 * This overload is intended for already available item lists.
 *
 * Each item is identified by [key]. The key is also used by [selectionMode]
 * to determine and update the selected item state.
 *
 * The [content] slot receives the item and an [DialogList.ItemContext].
 * Custom item implementations should use the context to read the current
 * selection state and call [DialogList.ItemContext.performItemAction] whenever
 * the item is activated by the user.
 *
 * @param state Controls the visibility and lifecycle of the dialog.
 * @param items The items displayed in the dialog.
 * @param key Returns a stable unique identifier for an item. The identifier is
 * also used internally for selection handling.
 * @param content Composable used to render an item. Receives both the item and
 * its associated [DialogList.ItemContext].
 * @param selectionMode Defines how item interactions and selection are handled.
 * @param divider Optional divider displayed between list items. By default, no dividers are shown.
 * @param description Optional text displayed above the list content.
 * @param filter Optional filter configuration used to filter visible items.
 * @param title Optional dialog title.
 * @param icon Optional dialog icon displayed next to the title.
 * @param style Defines the visual appearance of the dialog.
 * @param buttons Defines the dialog buttons.
 * @param options Additional dialog configuration options.
 * @param onEvent Callback invoked for dialog events.
 *
 * Example:
 *
 * ```
 * DialogList(
 *     state = state,
 *     items = users,
 *     key = { it.id },
 *     selectionMode = DialogList.SelectionMode.MultiSelect(
 *         selected = selectedUserIds
 *     ),
 *     content = DialogListDefaults.itemContent(
 *         text = { Text(it.name) },
 *         supportingText = { Text(it.email) }
 *     ),
 *     title = {
 *         Text("Select users")
 *     }
 * )
 * ```
 */
@Composable
fun <T> DialogList(
    // Base Dialog - State
    state: BaseDialogState,
    // Custom - Required
    items: List<T>,
    key: (item: T) -> Int,
    content: @Composable (item: T, context: DialogList.ItemContext) -> Unit,
    selectionMode: DialogList.SelectionMode<T>,
    // Custom - Optional
    divider: @Composable (() -> Unit)?  = null,
    description: String = "",
    filter: DialogList.Filter<T>? = null,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
// end-snippet
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogListContent(
            items = items,
            key = key,
            content = content,
            selectionMode = selectionMode,
            divider = divider,
            description = description,
            filter = filter,
            onDismiss = { state.dismiss() },
            onEvent = onEvent
        )
    }
}

// begin-snippet: DialogList::constructor2
/**
 * Displays a dialog containing a selectable or clickable list of items loaded
 * asynchronously.
 *
 * This overload is useful when the list content has to be loaded when the
 * dialog is shown. Until loading is complete, [loadingIndicator] is displayed.
 *
 * If [itemSaver] is provided, the loaded items are stored using
 * [rememberSaveable]. This can be useful when the dialog should preserve loaded
 * data across configuration changes or process recreation.
 *
 * Each item is identified by [key]. The key is also used by [selectionMode]
 * to determine and update the selected item state.
 *
 * The [content] slot receives the item and an [DialogList.ItemContext].
 * Custom item implementations should use the context to read the current
 * selection state and call [DialogList.ItemContext.performItemAction] whenever
 * the item is activated by the user.
 *
 * @param state Controls the visibility and lifecycle of the dialog.
 * @param items Suspended function used to load the items displayed in the dialog.
 * @param key Returns a stable unique identifier for an item. The identifier is
 * also used internally for selection handling.
 * @param content Composable used to render an item. Receives both the item and
 * its associated [DialogList.ItemContext].
 * @param selectionMode Defines how item interactions and selection are handled.
 * @param itemSaver Optional saver used to persist loaded items across state
 * restoration.
 * @param loadingIndicator Composable displayed while items are being loaded.
 * @param divider Optional divider displayed between list items. By default, no dividers are shown.
 * @param description Optional text displayed above the list content.
 * @param filter Optional filter configuration used to filter visible items.
 * @param title Optional dialog title.
 * @param icon Optional dialog icon displayed next to the title.
 * @param style Defines the visual appearance of the dialog.
 * @param buttons Defines the dialog buttons.
 * @param options Additional dialog configuration options.
 * @param onEvent Callback invoked for dialog events.
 *
 * Example:
 *
 * ```
 * DialogList(
 *     state = state,
 *     items = {
 *         repository.loadUsers()
 *     },
 *     key = { it.id },
 *     selectionMode = DialogList.SelectionMode.SingleSelect(
 *         selected = selectedUserId
 *     ),
 *     content = DialogListDefaults.itemContent(
 *         text = { Text(it.name) },
 *         supportingText = { Text(it.email) }
 *     ),
 *     title = {
 *         Text("Select user")
 *     }
 * )
 * ```
 */
@Composable
fun <T> DialogList(
    // Base Dialog - State
    state: BaseDialogState,
    // Custom - Required
    items: suspend () -> List<T>,
    key: (item: T) -> Int,
    content: @Composable (item: T, context: DialogList.ItemContext) -> Unit,
    selectionMode: DialogList.SelectionMode<T>,
    itemSaver: Saver<MutableState<List<T>>, out Any>? = null,
    // Custom - Optional
    loadingIndicator: @Composable () -> Unit = { DialogListDefaults.LoadingIndicator() },
    divider: @Composable (() -> Unit)?  = null,
    description: String = "",
    filter: DialogList.Filter<T>? = null,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
// end-snippet
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogListContent(
            items = items,
            key = key,
            content = content,
            selectionMode = selectionMode,
            itemSaver = itemSaver,
            loadingIndicator = loadingIndicator,
            divider = divider,
            description = description,
            filter = filter,
            onDismiss = { state.dismiss() },
            onEvent = onEvent
        )
    }
}
