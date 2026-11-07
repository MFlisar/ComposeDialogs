package com.michaelflisar.composedialogs.dialogs.list

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.list.composables.DefaultListItem

object DialogListDefaults {

    @Composable
    fun LoadingIndicator() {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            DialogUI.provider.ProgressIndicator(
                type = DialogUI.ProgressIndicatorType.Circular,
                progress = null,
                modifier = Modifier
            )
        }
    }

    /**
     * Creates a default item content implementation matching the standard
     * DialogList appearance.
     *
     * The returned content displays the provided text, supporting text,
     * trailing text and icon using the library's default list item layout
     * and styling. Selection controls (checkboxes or radio buttons) are
     * automatically rendered based on the active
     * [DialogList.SelectionMode].
     *
     * This function is intended as a convenient way to create list items
     * without having to implement a custom item layout.
     *
     * For fully customized item designs, provide a custom `@Composable (item: T, context: DialogList.ItemContext) -> Unit`
     * implementation directly to [DialogList].
     *
     * @param text Primary content displayed for the item.
     * @param supportingText Optional secondary content displayed below [text].
     * @param trailingText Optional content displayed at the end of the item.
     * @param icon Optional leading content displayed before [text].
     *
     * @return A [DialogList] item content implementation using the default
     * DialogList item layout and styling.
     */
    fun <T> itemContent(
        text: @Composable (item: T) -> Unit,
        supportingText: (@Composable (item: T) -> Unit)? = null,
        trailingText: (@Composable (item: T) -> Unit)? = null,
        icon: (@Composable (item: T) -> Unit)? = null,
    ): @Composable (item: T, context: DialogList.ItemContext) -> Unit = { item, context ->
        DefaultListItem(
            item = item,
            context = context,
            text = text,
            supportingText = supportingText,
            trailingText = trailingText,
            icon = icon
        )
    }

}