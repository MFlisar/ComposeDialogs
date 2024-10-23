package com.michaelflisar.composedialogs.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
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
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.core.styleDialog
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.rememberDialogColor
import com.michaelflisar.composedialogs.dialogs.color.specialOptionsColorDialog
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.rememberDialogDate
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import com.michaelflisar.composedialogs.dialogs.input.DialogInput
import com.michaelflisar.composedialogs.dialogs.input.DialogInputNumber
import com.michaelflisar.composedialogs.dialogs.number.DialogNumberPicker
import com.michaelflisar.composedialogs.dialogs.number.NumberPickerSetup
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInput
import com.michaelflisar.composedialogs.dialogs.input.rememberDialogInputNumber
import com.michaelflisar.composedialogs.dialogs.number.rememberDialogNumber
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgress
import com.michaelflisar.composedialogs.dialogs.time.DialogTime
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import com.michaelflisar.composedialogs.dialogs.list.DialogList
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
    List
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
            val infos = remember { mutableStateListOf<String>() }
            val dialog = rememberDialogState<Dialog>(data = null)
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MyTitle("Dialogs")
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

            when (dialog.data) {
                Dialog.Color -> {
                    val color = rememberDialogColor(Color.Blue.copy(alpha = .5f))
                    DialogColor(
                        title = "Color Dialog",
                        state = dialog,
                        color = color,
                        alphaSupported = true,
                        style = DialogDefaults.styleDialog(),
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
                    val date = rememberDialogDate()
                    DialogDate(
                        title = "Select Date",
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
                    val time = rememberDialogTime()
                    DialogTime(
                        title = "Select Time",
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
                    DialogInfo(
                        title = "Info Dialog",
                        state = dialog,
                        info = "Information",
                        infoLabel = "Important",
                        onEvent = {
                            infos.add("InfoDialog - Event $it")
                        }
                    )
                }
                Dialog.Progress -> {
                    DialogProgress(
                        title = "Progress Dialog",
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
                    val input = rememberDialogInput("")
                    DialogInput(
                        title = "Input Dialog",
                        state = dialog,
                        inputLabel = "Enter some text...",
                        input = input,
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
                    val input = rememberDialogInputNumber(0)
                    DialogInputNumber(
                        title = "Number Dialog",
                        state = dialog,
                        valueLabel = "Enter a valid Integer...",
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
                    val value = rememberDialogNumber(0)
                    DialogNumberPicker(
                        title = "Number Picker Dialog",
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
                Dialog.List ->  {
                    val selected = remember { mutableStateOf<Int?>(null) }
                    val items = List(100) { "Item $it"}
                    DialogList(
                        title = "List Dialog",
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
                null -> {
                    //
                }


            }
        }
    }
}