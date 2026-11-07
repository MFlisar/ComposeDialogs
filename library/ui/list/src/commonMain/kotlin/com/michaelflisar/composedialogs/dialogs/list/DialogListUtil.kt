package com.michaelflisar.composedialogs.dialogs.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent

internal object DialogListUtil {

    fun <T> isSelected(itemKey: Int, selectionMode: DialogList.SelectionMode<T>): Boolean = when (selectionMode) {
        is DialogList.SelectionMode.SingleSelect -> selectionMode.selected.value == itemKey
        is DialogList.SelectionMode.MultiSelect -> selectionMode.selected.value.contains(itemKey)
        is DialogList.SelectionMode.SingleClickAndClose,
        is DialogList.SelectionMode.MultiClick -> false
    }

    fun <T> setSelected(
        itemKey: Int,
        selectionMode: DialogList.SelectionMode<T>,
        selected: Boolean
    ) {
        when (selectionMode) {
            is DialogList.SelectionMode.SingleSelect -> {
                if (selected) selectionMode.selected.value = itemKey
                else if (selectionMode.selected.value == itemKey) selectionMode.selected.value = null
            }
            is DialogList.SelectionMode.MultiSelect -> {
                if (selected && !selectionMode.selected.value.contains(itemKey)) {
                    selectionMode.selected.value = selectionMode.selected.value.plus(itemKey)
                } else if (!selected && selectionMode.selected.value.contains(itemKey)) {
                    selectionMode.selected.value = selectionMode.selected.value.minus(itemKey)
                }
            }
            is DialogList.SelectionMode.SingleClickAndClose,
            is DialogList.SelectionMode.MultiClick -> Unit
        }
    }

    fun <T> toggleSelection(itemKey: Int, selectionMode: DialogList.SelectionMode<T>) {
        setSelected(itemKey, selectionMode, !isSelected(itemKey, selectionMode))
    }

    @Composable
    fun <T> createItemContext(
        item: T,
        itemKey: Int,
        selectionMode: DialogList.SelectionMode<T>,
        onDismiss: () -> Unit,
        onEvent: (event: DialogEvent) -> Unit
    ): DialogList.ItemContext {
        val selected = remember(itemKey, selectionMode) {
            derivedStateOf {
                isSelected(itemKey, selectionMode)
            }
        }

        fun performItemAction() {
            when (selectionMode) {
                is DialogList.SelectionMode.MultiClick -> {
                    selectionMode.onItemClicked(item)
                }

                is DialogList.SelectionMode.MultiSelect -> {
                    toggleSelection(itemKey, selectionMode)
                }

                is DialogList.SelectionMode.SingleClickAndClose -> {
                    selectionMode.onItemClicked(item)
                    onDismiss()
                }

                is DialogList.SelectionMode.SingleSelect -> {
                    setSelected(itemKey, selectionMode, true)

                    if (selectionMode.closeOnSelect) {
                        onEvent(DialogEvent.Button(DialogButtonType.Positive, true))
                        onDismiss()
                    }
                }
            }
        }

        return DialogList.ItemContext(
            selected = selected,
            selectionMode = selectionMode,
            performItemAction = ::performItemAction
        )
    }

    fun <T> ensureOnlyVisibleItemsAreSelected(
        selectionMode: DialogList.SelectionMode<T>,
        items: List<T>,
        key: (item: T) -> Int
    ) {
        when (selectionMode) {
            is DialogList.SelectionMode.MultiClick,
            is DialogList.SelectionMode.SingleClickAndClose -> Unit

            is DialogList.SelectionMode.SingleSelect -> {
                val selected = selectionMode.selected.value ?: return
                val ids = items.map(key)
                if (!ids.contains(selected)) selectionMode.selected.value = null
            }

            is DialogList.SelectionMode.MultiSelect -> {
                val ids = items.map(key)
                val newSelection = selectionMode.selected.value.filter { ids.contains(it) }
                if (newSelection.size != selectionMode.selected.value.size) {
                    selectionMode.selected.value = newSelection
                }
            }
        }
    }
}
