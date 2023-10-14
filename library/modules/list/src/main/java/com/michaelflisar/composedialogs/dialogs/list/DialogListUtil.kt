package com.michaelflisar.composedialogs.dialogs.list

import com.michaelflisar.composedialogs.core.DialogState

internal object DialogListUtil {

    fun <T> onCheckboxChecked(
        itemId: Int,
        selectionMode: DialogListSelectionMode.MultiSelect<T>,
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
        selectionMode: DialogListSelectionMode<T>,
        state: DialogState
    ): (() -> Unit)? {
        val onClick: (() -> Unit)? = when (selectionMode) {
            is DialogListSelectionMode.MultiClick -> { ->
                selectionMode.onItemClicked(item)
            }

            is DialogListSelectionMode.MultiSelect -> {
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

            is DialogListSelectionMode.SingleClickAndClose -> { ->
                selectionMode.onItemClicked(item)
                state.dismiss()
            }

            is DialogListSelectionMode.SingleSelect -> {
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
        selectionMode: DialogListSelectionMode<T>,
        items: List<T>,
        itemIdProvider: (item: T) -> Int
    ) {
        when (selectionMode) {
            is DialogListSelectionMode.MultiClick,
            is DialogListSelectionMode.SingleClickAndClose, -> {
                // nothing to do...
            }
            is DialogListSelectionMode.SingleSelect -> {
                val selected = selectionMode.selected.value ?: return
                val ids = items.map(itemIdProvider)
                if (!ids.contains(selected)) {
                    selectionMode.selected.value = null
                }
            }
            is DialogListSelectionMode.MultiSelect -> {
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