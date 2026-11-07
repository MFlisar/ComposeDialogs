package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.dialogs.list.DialogList
import com.michaelflisar.composedialogs.dialogs.list.DialogListUtil

@Composable
internal fun <T> DialogListItem(
    item: T,
    itemKey: Int,
    selectionMode: DialogList.SelectionMode<T>,
    content: @Composable (item: T, context: DialogList.ItemContext) -> Unit,
    onDismiss: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit
) {
    val context = DialogListUtil.createItemContext(
        item = item,
        itemKey = itemKey,
        selectionMode = selectionMode,
        onDismiss = onDismiss,
        onEvent = onEvent
    )
    content(item, context)
}
