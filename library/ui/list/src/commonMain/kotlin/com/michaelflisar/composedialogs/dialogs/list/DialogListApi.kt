package com.michaelflisar.composedialogs.dialogs.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.saveable.Saver

@Stable
object DialogList {

    sealed class SelectionMode<T> {
        class SingleSelect<T>(
            val selected: MutableState<Int?>,
            val selectOnRadioButtonClickOnly: Boolean = true,
            val closeOnSelect: Boolean = false
        ) : SelectionMode<T>()

        class MultiSelect<T>(
            val selected: MutableState<List<Int>>,
            val selectOnCheckboxClickOnly: Boolean = true,
            val showSelectionCounter: Boolean = false
        ) : SelectionMode<T>()

        class SingleClickAndClose<T>(val onItemClicked: (item: T) -> Unit) : SelectionMode<T>()
        class MultiClick<T>(val onItemClicked: (item: T) -> Unit) : SelectionMode<T>()
    }

    class ItemContext internal constructor(
        val selected: State<Boolean>,
        val selectionMode: SelectionMode<*>,
        val performItemAction: () -> Unit
    )

    internal sealed class ItemProvider<T> {
        class List<T>(val items: kotlin.collections.List<T>) : ItemProvider<T>()
        class Loader<T>(
            val loadingIndicator: @Composable () -> Unit,
            val loader: suspend () -> kotlin.collections.List<T>,
            val itemSaver: Saver<MutableState<kotlin.collections.List<T>>, out Any>?
        ) : ItemProvider<T>()
    }

    class Filter<T>(
        val filter: (filter: String, item: T) -> Boolean,
        val infoText: @Composable ((count: Int, total: Int) -> String)? = { count, total -> "$count/$total" },
        val keepSelectionForInvisibleItems: Boolean = true,
        val showClearIcon: Boolean = true
    )
}
