package com.michaelflisar.composedialogs.dialogs.list

import com.michaelflisar.composedialogs.core.DialogState

internal object DialogListUtil {

    fun <T> onCheckboxChecked(
        itemId: Int,
        selectionMode: DialogList.SelectionMode.MultiSelect<T>,
        checked: Boolean
    ) {
        if (checked) {
            if (!selectionMode.selected.value.contains(itemId)) {
                selectionMode.selected.value =
                    selectionMode.selected.value.plus(itemId)
            }
        } else {
            if (selectionMode.selected.value.contains(itemId)) {
                selectionMode.selected.value =
                    selectionMode.selected.value.minus(itemId)
            }
        }
    }

    fun <T> createOnClick(
        item: T,
        itemId: Int,
        selectionMode: DialogList.SelectionMode<T>,
        state: DialogState
    ): (() -> Unit)? {
        val onClick: (() -> Unit)? = when (selectionMode) {
            is DialogList.SelectionMode.MultiClick -> { ->
                selectionMode.onItemClicked(item)
            }

            is DialogList.SelectionMode.MultiSelect -> {
                if (selectionMode.selectOnCheckboxClickOnly) {
                    null
                } else {
                    {
                        if (selectionMode.selected.value.contains(itemId)) {
                            selectionMode.selected.value =
                                selectionMode.selected.value.minus(itemId)
                        } else {
                            selectionMode.selected.value =
                                selectionMode.selected.value.plus(itemId)
                        }
                    }
                }
            }

            is DialogList.SelectionMode.SingleClickAndClose -> { ->
                selectionMode.onItemClicked(item)
                state.dismiss()
            }

            is DialogList.SelectionMode.SingleSelect -> {
                if (selectionMode.selectOnRadioButtonClickOnly) {
                    null
                } else {
                    {
                        if (selectionMode.selected.value == itemId) {
                            // radio button cant be deselected either...
                        } else {
                            selectionMode.selected.value = itemId
                        }
                    }
                }
            }
        }
        return onClick
    }

    fun <T> ensureOnlyVisibleItemsAreSelected(
        selectionMode: DialogList.SelectionMode<T>,
        items: List<T>,
        itemIdProvider: (item: T) -> Int
    ) {
        when (selectionMode) {
            is DialogList.SelectionMode.MultiClick,
            is DialogList.SelectionMode.SingleClickAndClose, -> {
                // nothing to do...
            }
            is DialogList.SelectionMode.SingleSelect -> {
                val selected = selectionMode.selected.value ?: return
                val ids = items.map(itemIdProvider)
                if (!ids.contains(selected)) {
                    selectionMode.selected.value = null
                }
            }
            is DialogList.SelectionMode.MultiSelect -> {
                val selection = selectionMode.selected.value
                val ids = items.map(itemIdProvider)
                val newSelection = mutableListOf<Int>()
                var updated = false
                selection.forEach {
                    if (ids.contains(it)) {
                        newSelection += it
                    } else {
                        updated = true
                    }
                }
                if (updated)
                    selectionMode.selected.value = newSelection
            }
        }
    }
}