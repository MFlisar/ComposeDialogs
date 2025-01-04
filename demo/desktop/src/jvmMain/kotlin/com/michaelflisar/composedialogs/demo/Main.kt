package com.michaelflisar.composedialogs.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.StyleOptions
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.rememberDialogColor
import com.michaelflisar.composedialogs.dialogs.color.styleWindowsColorDialog
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.rememberDialogDate
import com.michaelflisar.composedialogs.dialogs.date.styleWindowsDateDialog
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import com.michaelflisar.composedialogs.dialogs.info.styleWindowsInfoDialog
import com.michaelflisar.composedialogs.dialogs.input.DialogInput
import com.michaelflisar.composedialogs.dialogs.input.DialogInputNumber
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInput
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInputNumber
import com.michaelflisar.composedialogs.dialogs.input.styleWindowsInputDialog
import com.michaelflisar.composedialogs.dialogs.list.DialogList
import com.michaelflisar.composedialogs.dialogs.list.styleWindowsListDialog
import com.michaelflisar.composedialogs.dialogs.menu.DialogMenu
import com.michaelflisar.composedialogs.dialogs.menu.MenuItem
import com.michaelflisar.composedialogs.dialogs.menu.styleWindowsMenuDialog
import com.michaelflisar.composedialogs.dialogs.number.DialogNumberPicker
import com.michaelflisar.composedialogs.dialogs.number.NumberPickerSetup
import com.michaelflisar.composedialogs.dialogs.number.rememberDialogNumber
import com.michaelflisar.composedialogs.dialogs.number.styleWindowsNumberDialog
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgress
import com.michaelflisar.composedialogs.dialogs.progress.styleWindowsProgressDialog
import com.michaelflisar.composedialogs.dialogs.time.DialogTime
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import com.michaelflisar.composedialogs.dialogs.time.styleWindowsTimeDialog
import com.michaelflisar.toolbox.composables.MyCheckbox
import com.michaelflisar.toolbox.composables.MyDropdown
import com.michaelflisar.toolbox.composables.MyRow
import com.michaelflisar.toolbox.composables.MyTitle
import com.michaelflisar.toolbox.ui.MyScrollableLazyColumn

enum class Dialog {
    Color,
    Date,
    Time,
    Info,
    Progress,
    Input,
    Number,
    NumberPicker,
    List,
    Menu
}

@OptIn(ExperimentalStdlibApi::class, ExperimentalLayoutApi::class)
fun main() {

    application {

        Window(
            title = "ComposeDialogs Demo",
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 800.dp,
                height = 600.dp
            )
        ) {
            val styles = listOf("Dialog", "Bottom Sheet", "Windows Dialog", "Fullscreen Dialog")
            val selectedStyle = remember { mutableStateOf(0) }
            val selectedIconPosition = remember { mutableStateOf(StyleOptions.IconMode.Begin) }
            val showIcon = remember { mutableStateOf(false) }
            val infos = remember { mutableStateListOf<String>() }
            val dialog = rememberDialogState<Dialog>(data = null)
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MyTitle("Dialogs")
                MyRow {
                    MyDropdown(modifier = Modifier.weight(1f), title = "Dialog Mode", items = styles, selected = selectedStyle)
                    MyCheckbox(modifier = Modifier.wrapContentWidth(), title = "Icon", checked = showIcon)
                    MyDropdown<StyleOptions.IconMode>(modifier = Modifier.weight(1f), title = "Icon Position", items = StyleOptions.IconMode.entries, selected = selectedIconPosition, mapper = { item, dropdown -> item.name}, enabled = showIcon.value)
                }
               FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    Dialog.entries.forEach { dlg ->
                        Button(
                            onClick = {
                                dialog.show(dlg)
                            }
                        ) {
                            Text("Show ${dlg.name} Dialog")
                        }
                    }
                }
                HorizontalDivider()
                MyTitle("Infos")
                MyScrollableLazyColumn(
                    modifier = Modifier.weight(1f),
                    itemSpacing = 2.dp
                ) {
                    items(infos.size) {
                        Text(infos[it])
                    }
                }
            }

            val getStyle = @Composable { windowsDialogStyle: @Composable () -> ComposeDialogStyle ->
                val options = StyleOptions(iconMode = selectedIconPosition.value)
                when (selectedStyle.value) {
                    0 -> DialogDefaults.styleDialog(swipeDismissable = false, options = options)
                    1 -> DialogDefaults.styleBottomSheet(options = options, animateShow = true)
                    2 -> windowsDialogStyle()
                    3 -> DialogDefaults.styleFullscreenDialog()
                    else -> error("Invalid style")
                }
            }
            val getIcon: (@Composable () -> Unit)? = if (showIcon.value) {
                { Icon(Icons.Default.Info, null) }
            } else null

            when (dialog.data) {
                Dialog.Color -> {
                    val color = rememberDialogColor(Color.Blue.copy(alpha = .5f))
                    val style = getStyle {
                        DialogDefaults.styleWindowsColorDialog("Color Dialog")
                    }
                    DialogColor(
                        style = style,
                        title = { Text("Color Dialog") },
                        icon = getIcon,
                        state = dialog,
                        color = color,
                        alphaSupported = true,
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected color: #${color.value.toArgb().toHexString()}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.Date -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsDateDialog("Select Date")
                    }
                    val date = rememberDialogDate()
                    DialogDate(
                        style = style,
                        title = { Text("Select Date") },
                        icon = getIcon,
                        state = dialog,
                        date = date,
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected date: ${date.value}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.Time -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsTimeDialog("Select Time")
                    }
                    val time = rememberDialogTime()
                    DialogTime(
                        style = style,
                        title = { Text("Select Time") },
                        icon = getIcon,
                        state = dialog,
                        time = time,
                        //setup = DialogTimeDefaults.setup(is24Hours = true),
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected time: ${time.value}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.Info -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsInfoDialog("Info Dialog")
                    }
                    DialogInfo(
                        style = style,
                        title = { Text("Info Dialog") },
                        icon = getIcon,
                        state = dialog,
                        info = "Information",
                        infoLabel = "Important",
                        onEvent = {
                            infos.add("InfoDialog - Event $it")
                        }
                    )
                }

                Dialog.Progress -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsProgressDialog("Progress Dialog")
                    }
                    DialogProgress(
                        style = style,
                        title = { Text("Progress Dialog") },
                        icon = getIcon,
                        state = dialog,
                        content = {
                            Text("Loading...")
                        },
                        onEvent = {
                            infos.add("InfoDialog - Event $it")
                        }
                    )
                }

                Dialog.Input -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsInputDialog("Input Dialog")
                    }
                    val input = rememberDialogInput("")
                    DialogInput(
                        style = style,
                        title = { Text("Input Dialog") },
                        icon = getIcon,
                        state = dialog,
                        label = "Enter some text...",
                        value = input,
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected input: ${input.value}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.Number -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsInputDialog("Number Dialog")
                    }
                    val input = rememberDialogInputNumber(0)
                    DialogInputNumber(
                        style = style,
                        title = { Text("Number Dialog") },
                        icon = getIcon,
                        state = dialog,
                        label = "Enter a valid Integer...",
                        value = input,
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected number input: ${input.value}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.NumberPicker -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsNumberDialog("Number Picker Dialog")
                    }
                    val value = rememberDialogNumber(0)
                    DialogNumberPicker(
                        style = style,
                        title = { Text("Number Picker Dialog") },
                        icon = getIcon,
                        state = dialog,
                        value = value,
                        setup = NumberPickerSetup(
                            min = 0, max = 1000, stepSize = 10, stepSize2 = 100
                        ),
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected number picker value: ${value.value}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.List -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsListDialog("List Dialog")
                    }
                    val selected = remember { mutableStateOf<Int?>(null) }
                    val items = List(100) { "Item $it" }
                    DialogList(
                        style = style,
                        title = { Text("List Dialog") },
                        icon = getIcon,
                        description = "Some optional description",
                        state = dialog,
                        items = items,
                        itemIdProvider = { items.indexOf(it) },
                        selectionMode = DialogList.SelectionMode.SingleSelect(
                            selected = selected,
                            selectOnRadioButtonClickOnly = false
                        ),
                        itemContents = DialogList.ItemDefaultContent(
                            text = { it }
                        ),
                        onEvent = {
                            if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                                infos.add("Selected list value: ${selected.value?.let { "Index = $it | Item = ${items[it]}" }}")
                            } else {
                                infos.add("Event $it")
                            }
                        }
                    )
                }

                Dialog.Menu -> {
                    val style = getStyle {
                        DialogDefaults.styleWindowsMenuDialog("Menu Dialog")
                    }
                    val items = listOf(
                        MenuItem.Item(
                            "Item 1",
                            "Description 1",
                            { Icon(Icons.Default.Info, null) }) {
                            infos.add("Item 1 clicked")
                        },
                        MenuItem.Item(
                            "Item 2",
                            "Description 2",
                            { Icon(Icons.Default.Info, null) }) {
                            infos.add("Item 2 clicked")
                        },
                        MenuItem.Divider,
                        MenuItem.SubMenu("Sub Menu 1",
                            "Description",
                            { Icon(Icons.Default.Info, null) },
                            listOf(
                                MenuItem.Item(
                                    "Sub Item 1",
                                    "Description 1",
                                    { Icon(Icons.Default.Info, null) }) {
                                    infos.add("Sub Item 1 clicked")
                                },
                                MenuItem.Item(
                                    "Sub Item 2",
                                    "Description 2",
                                    { Icon(Icons.Default.Info, null) }) {
                                    infos.add("Sub Item 2 clicked")
                                },
                                MenuItem.Item(
                                    "Sub Item 3",
                                    "Description 3",
                                    { Icon(Icons.Default.Info, null) }) {
                                    infos.add("Sub Item 3 clicked")
                                },
                                MenuItem.SubMenu("Sub Sub Menu 4",
                                    "Description",
                                    { Icon(Icons.Default.Info, null) },
                                    listOf(
                                        MenuItem.Item(
                                            "Sub Sub Item 1",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 1 clicked")
                                        },
                                        MenuItem.Item(
                                            "Sub Sub Item 2",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 2 clicked")
                                        },
                                        MenuItem.Item(
                                            "Sub Sub Item 3",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 3 clicked")
                                        },
                                        MenuItem.Item(
                                            "Sub Sub Item 4",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 4 clicked")
                                        },
                                        MenuItem.Item(
                                            "Sub Sub Item 5",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 5 clicked")
                                        },
                                        MenuItem.Item(
                                            "Sub Sub Item 6",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 6 clicked")
                                        },
                                        MenuItem.Item(
                                            "Sub Sub Item 7",
                                            "Description",
                                            { Icon(Icons.Default.Info, null) }) {
                                            infos.add("Sub Sub Item 7 clicked")
                                        },
                                    )
                                )
                            )
                        ),
                        MenuItem.Custom {
                            Text("Custom Content", color = Color.Red)
                        }
                    )
                    DialogMenu(
                        style = style,
                        title = { Text("Menu Dialog") },
                        icon = getIcon,
                        items = items,
                        state = dialog
                    )
                }

                null -> {
                    //
                }


            }
        }
    }
}