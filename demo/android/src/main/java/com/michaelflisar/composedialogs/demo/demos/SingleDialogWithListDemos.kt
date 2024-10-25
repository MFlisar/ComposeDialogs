package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogStateWithData
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle2
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo

@Composable
fun ColumnScope.SingleDialogWithListDemos(style: ComposeDialogStyle2, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Clicking any item in the list below will open its dialog")

    val items = 1..100
    val showDialog = rememberDialogState<Int>(data = null)
    ListDialog(showDialog, style, icon)

    LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach {
            item(key = it) {
                Text(
                    text = "Item #$it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            showDialog.show(it)
                        }
                        .minimumInteractiveComponentSize()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun ListDialog(
    stateWithData: DialogStateWithData<Int>,
    style: ComposeDialogStyle2,
    icon: (@Composable () -> Unit)?
) {
    if (stateWithData.showing) {
        val context = LocalContext.current
        val data = stateWithData.requireData()
        DialogInfo(
            state = stateWithData,
            title = { Text("Item Info") },
            info = "This is the dialog for Item #$data",
            icon = icon,
            style = style,
            onEvent = { event ->
                context.showToast("Event $event")
            }
        )
    }
}