package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.dialogs.list.DialogList
import com.michaelflisar.composedialogs.dialogs.list.DialogListUtil

@Composable
internal fun <T> DialogListItem(
    state: BaseDialogState,
    item: T,
    itemId: Int,
    selectionMode: DialogList.SelectionMode<T>,
    itemContents: DialogList.ItemContents<T>,
    onEvent: (event: DialogEvent) -> Unit
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
        } else null,
        onEvent = onEvent
    )
}

@Composable
private fun <T> DialogListItem(
    item: T,
    itemId: Int,
    state: BaseDialogState,
    selectionMode: DialogList.SelectionMode<T>,
    content: @Composable ColumnScope.() -> Unit,
    icon: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (ColumnScope.() -> Unit)? = null,
    onEvent: (event: DialogEvent) -> Unit
) {
    val onClick = DialogListUtil.createOnClick(item, itemId, selectionMode, state, onEvent)

    val paddingVertical = 8.dp
    val paddingStart = if (onClick != null) 16.dp else 0.dp
    val paddingEnd = if (onClick != null) 16.dp else 0.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .height(IntrinsicSize.Min)
            .clip(MaterialTheme.shapes.small)
            .then(
                if (onClick != null) Modifier.clickable {
                    onClick()
                } else Modifier
            )
        //.padding(padding)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(paddingStart))

        // Icon
        if (icon != null) {
            icon.invoke()
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = paddingVertical)
        ) {
            content()
        }

        // Trailing Supporting Text
        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .padding(vertical = paddingVertical)
            ) {
                trailingContent()
            }
        }

        // Checkbox/RadioButton
        when (selectionMode) {
            is DialogList.SelectionMode.SingleSelect -> {
                if (selectionMode.selectOnRadioButtonClickOnly) {
                    RadioButton(
                        selected = selectionMode.selected.value == itemId,
                        onClick = {
                            selectionMode.selected.value = itemId
                        }
                    )
                } else {
                    Box(modifier = Modifier.wrapContentSize()) {
                        RadioButton(
                            selected = selectionMode.selected.value == itemId,
                            onClick = null
                        )
                    }
                }
            }

            is DialogList.SelectionMode.MultiSelect -> {
                if (selectionMode.selectOnCheckboxClickOnly) {
                    Checkbox(
                        checked = selectionMode.selected.value.contains(itemId),
                        onCheckedChange = {
                            DialogListUtil.onCheckboxChecked(itemId, selectionMode, it)
                        }
                    )
                } else {
                    Box(modifier = Modifier.wrapContentSize()) {
                        Checkbox(
                            checked = selectionMode.selected.value.contains(itemId),
                            onCheckedChange = null
                        )
                    }
                }
            }

            is DialogList.SelectionMode.SingleClickAndClose,
            is DialogList.SelectionMode.MultiClick -> {
                // no ui here...
            }
        }

        Spacer(modifier = Modifier.width(paddingEnd))
    }

}