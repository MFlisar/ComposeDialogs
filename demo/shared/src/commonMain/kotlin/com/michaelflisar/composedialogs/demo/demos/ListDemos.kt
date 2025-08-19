package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.composables.DemoDialogButton
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRow
import com.michaelflisar.composedialogs.dialogs.list.DialogList
import com.michaelflisar.composedialogs.dialogs.list.composables.DialogListContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ListDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoDialogRegion("List Dialogs (Simple Layouts)")

    val simpleItems = (1..500).toList()

    // Item Content Renderer - Simple Version where you can provide simple strings for text/supporting text/trailing text and an icon composable
    val itemContentsSimple = DialogList.ItemDefaultContent<Int>(
        text = { "Item $it" }
    )
    val itemContentsSimpleWithIcon = DialogList.ItemDefaultContent<Int>(
        text = { "Item $it" },
        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) }
    )
    val itemContentsSimpleWithIconAndTrailingInfo = DialogList.ItemDefaultContent<Int>(
        text = { "Item $it" },
        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
        trailingSupportingText = { "Supporting Text $it..." }
    )

    val simpleItemIdProvider = { item: Int -> item }

    val selectedSingle = rememberSaveable { mutableStateOf<Int?>(null) }
    val selectedMulti = rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    DemoDialogRow {
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.SingleSelect(selectedSingle)
        )
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiSelect(selectedMulti)
        )
    }
    DemoDialogRow {
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.SingleClickAndClose {
                showInfo("Selected in Single Click Mode: $it")
            }
        )
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiClick {
                showInfo("Selected in Multi Click Mode: $it")
            }
        )
    }
    DemoDialogRegion("List Dialogs (Other Options)")
    DemoDialogRow {
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimpleWithIcon,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiSelect(
                selectedMulti,
                selectOnCheckboxClickOnly = false
            ),
            divider = true,
            infos = "Divider + Icons + Click On Row Support"
        )
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimpleWithIcon,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiClick {
                showInfo("Selected in Multi Click Mode: $it")
            },
            divider = true,
            infos = "Divider + Icons"
        )
    }
    DemoDialogRow {
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimpleWithIconAndTrailingInfo,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiClick {
                showInfo("Selected in Multi Click Mode: $it")
            },
            infos = "Trailing Text"
        )
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimpleWithIconAndTrailingInfo,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiSelect(
                selectedMulti,
                showSelectionCounter = true,
                selectOnCheckboxClickOnly = false
            ),
            description = "Select some items - this dialog will also show you how many items are selected!",
            infos = "Selection Counter + Description + Row Click"
        )
    }

    DemoDialogRow {
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsSimpleWithIcon,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiSelect(
                selectedMulti,
                showSelectionCounter = true,
                selectOnCheckboxClickOnly = false
            ),
            filter = DialogList.Filter(
                filter = { filter, item ->
                    filter.isEmpty() || item.toString().contains(filter)
                },
                keepSelectionForInvisibleItems = false
            ),
            description = "Select some items - this dialog will also show you how many items are selected and allows you to filter the list!",
            infos = "Selection Counter + Row Click + Filtering"
        )
    }

    DemoListAppSelector(
        style,
        icon,
        showInfo,
    )
}

@Composable
expect fun DemoListAppSelector(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
)

@Composable
fun <T> RowScope.DemoList(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit,
    itemContents: DialogList.ItemContents<T>,
    itemIdProvider: (item: T) -> Int,
    items: List<T>? = null,
    itemsLoader: (suspend () -> List<T>)? = null,
    itemSaver: Saver<MutableState<List<T>>, out Any>? = null,
    selectionMode: DialogList.SelectionMode<T>,
    filter: DialogList.Filter<T>? = null,
    divider: Boolean = false,
    infos: String = "",
    description: String = ""
) {
    val state = rememberDialogState()
    if (state.visible) {
        val eventHandler = { event: DialogEvent ->
            if (event is DialogEvent.Button && event.button == DialogButtonType.Positive) {
                // we should probably handle the selected values in this case
                when (selectionMode) {
                    is DialogList.SelectionMode.MultiClick,
                    is DialogList.SelectionMode.SingleClickAndClose -> {
                        // Selection (Click) events are handled inside the selection mode callbacks directly!
                        showInfo("Event $event")
                    }

                    is DialogList.SelectionMode.MultiSelect -> {
                        showInfo(
                            "Selected IDs: ${selectionMode.selected.value.joinToString(",")}"
                        )
                    }

                    is DialogList.SelectionMode.SingleSelect -> {
                        showInfo("Selection ID: ${selectionMode.selected.value}")
                    }
                }
            } else {
                showInfo("Event $event")
            }
        }
        // list dialog
        if (items != null) {
            DialogList(
                state = state,
                title = { Text("List Dialog") },
                items = items,
                itemIdProvider = itemIdProvider,
                itemContents = itemContents,
                selectionMode = selectionMode,
                filter = filter,
                divider = divider,
                description = description,
                icon = icon,
                style = style,
                onEvent = eventHandler
            )
        } else {
            DialogList(
                state = state,
                title = { Text("List Dialog") },
                loadingIndicator = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Loading Apps...")
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator()
                    }
                },
                itemsLoader = itemsLoader!!,
                itemSaver = itemSaver,
                itemIdProvider = itemIdProvider,
                itemContents = itemContents,
                selectionMode = selectionMode,
                filter = filter,
                divider = divider,
                description = description,
                icon = icon,
                style = style,
                onEvent = eventHandler
            )
        }
    }
    DemoDialogButton(
        state,
        Icons.Default.TextFields,
        "List Dialog",
        "\"${selectionMode::class.simpleName}\" list dialog" + (if (infos.isNotEmpty()) " - $infos" else "")
    )
}
