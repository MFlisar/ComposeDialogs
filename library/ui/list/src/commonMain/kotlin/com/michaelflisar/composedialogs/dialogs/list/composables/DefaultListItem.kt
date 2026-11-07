package com.michaelflisar.composedialogs.dialogs.list.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.list.DialogList

@Composable
internal fun <T> DefaultListItem(
    item: T,
    context: DialogList.ItemContext,
    text: @Composable (item: T) -> Unit,
    supportingText: (@Composable (item: T) -> Unit)? = null,
    trailingText: (@Composable (item: T) -> Unit)? = null,
    icon: (@Composable (item: T) -> Unit)? = null
) {
    val hasItemClick = when (val mode = context.selectionMode) {
        is DialogList.SelectionMode.SingleSelect<*> -> !mode.selectOnRadioButtonClickOnly
        is DialogList.SelectionMode.MultiSelect<*> -> !mode.selectOnCheckboxClickOnly
        is DialogList.SelectionMode.SingleClickAndClose<*> -> true
        is DialogList.SelectionMode.MultiClick<*> -> true
    }

    val paddingVertical = 8.dp
    val paddingStart = if (hasItemClick) 16.dp else 0.dp
    val paddingEnd = if (hasItemClick) 16.dp else 0.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .height(IntrinsicSize.Min)
            .clip(ComposeDialogsUI.provider.smallShape())
            .then(
                if (hasItemClick) {
                    Modifier.clickable {
                        context.performItemAction()
                    }
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(paddingStart))

        icon?.let {
            it(item)
            Spacer(modifier = Modifier.width(16.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = paddingVertical)
        ) {
            DialogListContent(
                text = {
                    text(item)
                },
                supportingText = supportingText?.let {
                    {
                        it(item)
                    }
                }
            )
        }

        trailingText?.let {
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.padding(vertical = paddingVertical)) {
                DialogListTrailingContent {
                    it(item)
                }
            }
        }

        when (val mode = context.selectionMode) {
            is DialogList.SelectionMode.SingleSelect<*> -> {
                if (mode.selectOnRadioButtonClickOnly) {
                    ComposeDialogsUI.provider.RadioButton(
                        selected = context.selected.value,
                        onClick = {
                            context.performItemAction()
                        }
                    )
                } else {
                    Box(modifier = Modifier.wrapContentSize()) {
                        ComposeDialogsUI.provider.RadioButton(
                            selected = context.selected.value,
                            onClick = null
                        )
                    }
                }
            }

            is DialogList.SelectionMode.MultiSelect<*> -> {
                if (mode.selectOnCheckboxClickOnly) {
                    ComposeDialogsUI.provider.Checkbox(
                        checked = context.selected.value,
                        onCheckedChange = {
                            context.performItemAction()
                        }
                    )
                } else {
                    Box(modifier = Modifier.wrapContentSize()) {
                        ComposeDialogsUI.provider.Checkbox(
                            checked = context.selected.value,
                            onCheckedChange = null
                        )
                    }
                }
            }

            is DialogList.SelectionMode.SingleClickAndClose<*>,
            is DialogList.SelectionMode.MultiClick<*> -> Unit
        }

        Spacer(modifier = Modifier.width(paddingEnd))
    }
}