package com.michaelflisar.composedialogs.demo.demos

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.classes.AppItem
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRow
import com.michaelflisar.composedialogs.dialogs.list.DialogList
import com.michaelflisar.composedialogs.dialogs.list.composables.DialogListContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

@Composable
actual fun DemoListAppSelector(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val context = LocalContext.current

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

    DemoDialogRow {
        DemoList(
            style,
            icon,
            showInfo,
            itemContents = itemContentsCustom,
            itemIdProvider = customItemIdProvider,
            itemsLoader = { loadApps(context) },
            // optional => if no saver is provided, data will be reloaded and not retained between e.g. screen rotations
            // AppItem is parcelable so autoSaver can handle it!
            itemSaver = autoSaver(),
            selectionMode = DialogList.SelectionMode.MultiClick {
                showInfo("Selected in Multi Click Mode: ${it.id}")
            },
            infos = "Asynchronous loaded items..."
        )
    }
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
            val text = info.loadLabel(pm).toString()
            //val icon = info.loadIcon(context.packageManager)
            items.add(AppItem(id, info, text))
            id++
        }
        items.sortWith { o1, o2 -> o1.label.compareTo(o2.label, true) }
        items
    }
}