package com.michaelflisar.composedialogs.demo.demos

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.classes.AppItem
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.list.DialogList
import com.michaelflisar.composedialogs.dialogs.list.composables.DialogListContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ListDemos(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    val context = LocalContext.current
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

    // Item Content Renderer - here you can provide composables for the content, icon and trailing area...
    // => I have defined some default composables which I will use here
    val itemContentsCustom = object : DialogList.ItemContents<AppItem> {

        override val content: @Composable() (ColumnScope.(item: AppItem) -> Unit)
            get() = {
                DialogListContent(it.label, "ID: ${it.id}")
            }

        override val iconContent: @Composable() ((item: AppItem) -> Unit)?
            get() = {
                Image(
                    painter = rememberDrawablePainter(it.icon(context)),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }

        override val trailingContent: @Composable() (ColumnScope.(item: AppItem) -> Unit)?
            get() = null
    }
    val customItemIdProvider = { item: AppItem -> item.id }


    val selectedSingle = rememberSaveable { mutableStateOf<Int?>(null) }
    val selectedMulti = rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    DemoDialogRow {
        DemoList(
            style,
            icon,
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.SingleSelect(selectedSingle)
        )
        DemoList(
            style,
            icon,
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
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.SingleClickAndClose {
                context.showToast("Selected in Single Click Mode: $it")
            })
        DemoList(
            style,
            icon,
            itemContents = itemContentsSimple,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiClick {
                context.showToast("Selected in Multi Click Mode: $it")
            })
    }
    DemoDialogRegion("List Dialogs (Other Options)")
    DemoDialogRow {
        DemoList(
            style,
            icon,
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
            itemContents = itemContentsSimpleWithIcon,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiClick {
                context.showToast("Selected in Multi Click Mode: $it")
            },
            divider = true,
            infos = "Divider + Icons"
        )
    }
    DemoDialogRow {
        DemoList(
            style,
            icon,
            itemContents = itemContentsSimpleWithIconAndTrailingInfo,
            itemIdProvider = simpleItemIdProvider,
            items = simpleItems,
            selectionMode = DialogList.SelectionMode.MultiClick {
                context.showToast("Selected in Multi Click Mode: $it")
            },
            infos = "Trailing Text"
        )
        DemoList(
            style,
            icon,
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

    DemoDialogRow {
        DemoList(
            style,
            icon,
            itemContents = itemContentsCustom,
            itemIdProvider = customItemIdProvider,
            itemsLoader = { loadApps(context) },
            // optional => if no saver is provided, data will be reloaded and not retained between e.g. screen rotations
            // AppItem is parcelable so autoSaver can handle it!
            itemSaver = autoSaver(),
            selectionMode = DialogList.SelectionMode.MultiClick {
                context.showToast("Selected in Multi Click Mode: ${it.id}")
            },
            infos = "Asynchronous loaded items..."
        )
    }
}

@Composable
private fun <T> RowScope.DemoList(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
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
    val context = LocalContext.current
    val state = rememberDialogState()
    if (state.showing) {
        val eventHandler = { event: DialogEvent ->
            if (event is DialogEvent.Button && event.button == DialogButtonType.Positive) {
                // we should probably handle the selected values in this case
                when (selectionMode) {
                    is DialogList.SelectionMode.MultiClick,
                    is DialogList.SelectionMode.SingleClickAndClose -> {
                        // Selection (Click) events are handled inside the selection mode callbacks directly!
                        context.showToast("Event $event")
                    }

                    is DialogList.SelectionMode.MultiSelect -> {
                        context.showToast(
                            "Selected IDs: ${selectionMode.selected.value.joinToString(",")}"
                        )
                    }

                    is DialogList.SelectionMode.SingleSelect -> {
                        context.showToast("Selection ID: ${selectionMode.selected.value}")
                    }
                }
            } else {
                context.showToast("Event $event")
            }
        }
        // list dialog
        if (items != null) {
            DialogList(
                state = state,
                title = "Dialog",
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
                title = "Dialog",
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

private suspend fun loadApps(context: Context): List<AppItem> {
    val context = context.applicationContext
    return withContext(Dispatchers.IO) {
        val items = ArrayList<AppItem>()
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos = pm.queryIntentActivities(intent, 0)
        var id = 1
        for (info in resolveInfos) {
            val text = info.loadLabel(pm)?.toString() ?: ""
            //val icon = info.loadIcon(context.packageManager)
            items.add(AppItem(id, info, text))
            id++
        }
        items.sortWith { o1, o2 -> o1.label.compareTo(o2.label, true) }
        items
    }
}
