package com.michaelflisar.composedialogs.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*
import com.michaelflisar.composedialogs.demo.classes.DemoStyle
import com.michaelflisar.composedialogs.demo.classes.DemoTheme
import com.michaelflisar.composedialogs.demo.views.SegmentedControl
import com.michaelflisar.composedialogs.dialogs.color.DialogColor
import com.michaelflisar.composedialogs.dialogs.color.DialogColorLabelStyle
import com.michaelflisar.composedialogs.dialogs.color.rememberDialogColorState
import com.michaelflisar.composedialogs.dialogs.datetime.DialogDate
import com.michaelflisar.composedialogs.dialogs.datetime.DialogTime
import com.michaelflisar.composedialogs.dialogs.info.DialogInfo
import com.michaelflisar.composedialogs.dialogs.info.DialogInput
import com.michaelflisar.composedialogs.dialogs.info.DialogInputValidator
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgress
import com.michaelflisar.composedialogs.dialogs.progress.DialogProgressStyle
import com.michaelflisar.testcompose.ui.theme.ComposeDialogDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class DemoActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val theme = rememberSaveable { mutableStateOf(DemoTheme.System) }
            val dynamicTheme = rememberSaveable { mutableStateOf(false) }

            ComposeDialogDemoTheme(
                darkTheme = theme.value.isDark(),
                dynamicColor = dynamicTheme.value
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )

                        // Dialog Setting States
                        val style = rememberSaveable { mutableStateOf(DemoStyle.Dialog) }
                        val swipeDismiss = rememberSaveable { mutableStateOf(false) }
                        val showIcon = rememberSaveable { mutableStateOf(true) }

                        // UI Settings
                        val dialogSettings = rememberDialogState()
                        DialogSettings(
                            dialogSettings,
                            theme,
                            dynamicTheme,
                            style,
                            swipeDismiss,
                            showIcon
                        )
                        OutlinedButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                            onClick = {
                                dialogSettings.show()
                            }) {
                            Text("Settings")
                        }

                        // UI Example Dialogs
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(
                                    bottom = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val s = if (style.value == DemoStyle.BottomSheet) {
                                DialogDefaults.styleBottomSheet(
                                    // dragHandle = true,
                                    //hideAnimated = true,
                                    // resizeContent = true // only use this with scrollable content!
                                    // peekHeight = 0.dp
                                )
                            } else DialogDefaults.styleDialog(swipeDismissable = swipeDismiss.value)
                            val icon = if (showIcon.value) {
                                DialogIcon(rememberVectorPainter(Icons.Default.Home))
                            } else null

                            // -----------------
                            // Buttons for Demo Dialogs...
                            // -----------------

                            // 1) Info Dialogs
                            DemoDialogRegion("Info Dialogs")
                            DemoDialogInfo1(s, icon)
                            DemoDialogInfo2(s, icon)

                            // 2) Input Dialogs
                            DemoDialogRegion("Input Dialogs")
                            DemoDialogInput1(s, icon)

                            // 3) DateTime Dialogs
                            DemoDialogRegion("DateTime Dialogs")
                            DemoDialogDate1(icon)
                            DemoDialogTime1(s, icon)

                            // 4) Progress Dialogs
                            DemoDialogRegion("Progress Dialogs")
                            DemoDialogProgress1(s, icon)
                            DemoDialogProgress2(s, icon)
                            DemoDialogProgress3(s, icon)

                            // 4) Color Dialogs
                            DemoDialogRegion("Color Dialogs")
                            DemoDialogColor1(s, icon)
                            DemoDialogColor2(s, icon)

                            // X) Custom
                            DemoDialogRegion("Custom Dialogs")
                            DemoDialogCustom1(s, icon)
                            DemoDialogCustom2(s, icon)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun DialogSettings(
        state: DialogState,
        theme: MutableState<DemoTheme>,
        dynamicTheme: MutableState<Boolean>,
        style: MutableState<DemoStyle>,
        swipeDismiss: MutableState<Boolean>,
        icon: MutableState<Boolean>
    ) {
        if (state.showing) {
            Dialog(
                state = state,
                title = DialogDefaults.title("Settings"),
                style = DialogDefaults.styleBottomSheet(peekHeight = 0.dp) // disable peek
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "App Theme",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    SettingsRowSegmentedControl(theme, DemoTheme.values().toList())
                    SettingsRowCheckbox(dynamicTheme, "Dynamic Colors?")
                    Text(
                        "Demo Dialog Appearance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    SettingsRowSegmentedControl(style, DemoStyle.values().toList())
                    AnimatedVisibility(visible = style.value == DemoStyle.Dialog) {
                        SettingsRowCheckbox(swipeDismiss, "Dialog - SwipeDismiss?")
                    }
                    SettingsRowCheckbox(icon, "Show Icon?")
                }
            }
        }
    }

    // ----------------
    // Demos - Predefined Dialogs
    // ----------------

    @Composable
    private fun DemoDialogInfo1(style: DialogStyle, icon: DialogIcon?) {

        val state = rememberDialogState()
        if (state.showing) {
            DialogInfo(
                state = state,
                title = DialogDefaults.title("Dialog Info"),
                info = "Simple Info Dialog",
                icon = icon,
                style = style,
                onEvent = { event ->
                    showToast("Event $event")
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Info,
            "Dialog Info 1",
            "Shows a basic info dialog"
        )
    }

    @Composable
    private fun DemoDialogInfo2(style: DialogStyle, icon: DialogIcon?) {

        val state = rememberDialogState(
            showing = false,
            buttonPositiveEnabled = false,
            dismissAllowed = false
        )
        if (state.showing) {
            var currentIcon by remember { mutableStateOf(icon) }
            var time by rememberSaveable { mutableStateOf(10) }
            val iconDone = DialogIcon(rememberVectorPainter(Icons.Default.Check))
            LaunchedEffect(Unit) {
                launch {
                    while (time > 0) {
                        delay(1000)
                        time--
                    }
                    state.enableButton(DialogButtonType.Positive, true)
                    state.dismissable(true)
                    currentIcon = iconDone
                }
            }

            DialogInfo(
                state = state,
                title = DialogDefaults.title("Dialog Info 2"),
                info = if (time == 0) "Dialog can be dismissed" else "Dialog can be dismissed in $time seconds...",
                icon = currentIcon,
                style = style,
                onEvent = {
                    showToast("Event $it")
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Info,
            "Dialog Info 2",
            "Shows a simple dialog with updating text that can't be closed until before 10s are over"
        )
    }

    @Composable
    private fun DemoDialogInput1(style: DialogStyle, icon: DialogIcon?) {

        val text = "Hello"
        val state = rememberDialogState(
            showing = false,
            buttonPositiveEnabled = text.isNotEmpty(),
            dismissAllowed = text.isNotEmpty()
        )
        if (state.showing) {

            // special state for input dialog
            val input = rememberSaveable { mutableStateOf(text) }

            // input dialog
            DialogInput(
                state = state,
                title = DialogDefaults.title("Dialog Input"),
                input = input,
                inputLabel = "Input",
                icon = icon,
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        // we should probably handle the input value in this case
                        showToast("Submitted Input: ${input.value}")
                    } else {
                        showToast("Event $it")
                    }
                },
                validator = DialogInputValidator(
                    validate = {
                        if (it.isNotEmpty())
                            null
                        else
                            "Empty input is not allowed!"
                    }
                ),
                onTextStateChanged = { valid, _ ->
                    state.enableButton(DialogButtonType.Positive, valid)
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.TextFields,
            "Show Dialog Input",
            "Shows an input dialog that does not accept an empty input"
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DemoDialogDate1(icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {

            // special state for date dialog
            val date = rememberDatePickerState()

            DialogDate(
                state = state,
                date = date,
                icon = icon,
                title = DialogDefaults.titleSmall("Select Date"),
                // dialog date does support bottom sheet style only!
                style = DialogDefaults.styleBottomSheet(peekHeight = 0.dp),
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast(
                            "Selected Date: ${
                                date.selectedDateMillis?.let { Date(it) }?.toLocaleString()
                            }"
                        )
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.CalendarMonth,
            "Show Dialog DateTime",
            "Shows an date picker dialog"
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DemoDialogTime1(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {

            // special state for time dialog
            val time = rememberTimePickerState()

            DialogTime(
                state = state,
                time = time,
                icon = icon,
                title = DialogDefaults.titleSmall("Select Time"),
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast("Selected Time: ${time.hour}:${time.minute} | is24 = ${time.is24hour}")
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Schedule,
            "Show Dialog Time",
            "Shows an time picker dialog"
        )
    }

    @Composable
    private fun DemoDialogProgress1(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {

            DialogProgress(
                state = state,
                label = "Working...",
                progressStyle = DialogProgressStyle.Indeterminate(linear = true),
                icon = icon,
                title = DialogDefaults.title("Progress Dialog"),
                buttons = DialogDefaults.buttons(
                    positive = DialogButton("Stop")
                ),
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast("Progress Dialog closed by button")
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Downloading,
            "Show Dialog Progress",
            "Shows an endless LINEAR progress dialog"
        )
    }

    @Composable
    private fun DemoDialogProgress2(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {
            DialogProgress(
                state = state,
                label = "Working...",
                progressStyle = DialogProgressStyle.Indeterminate(linear = false),
                icon = icon,
                title = DialogDefaults.title("Progress Dialog"),
                buttons = DialogDefaults.buttons(
                    positive = DialogButton("Stop")
                ),
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast("Progress Dialog closed by button")
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Downloading,
            "Show Dialog Progress",
            "Shows an endless CIRCULAR progress dialog"
        )
    }

    @Composable
    private fun DemoDialogProgress3(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState(
            dismissAllowed = false,
            buttonPositiveEnabled = false
        )
        if (state.showing) {


            var time by rememberSaveable { mutableStateOf(10) }
            LaunchedEffect(Unit) {
                launch {
                    while (time > 0) {
                        delay(1000)
                        time--
                    }
                    state.enableButton(DialogButtonType.Positive, true)
                    state.dismissable(true)
                }
            }

            val progressStyle by remember {
                derivedStateOf { DialogProgressStyle.Determinate(linear = true, 10 - time, 10) }
            }

            DialogProgress(
                state = state,
                label = "Working...",
                progressStyle = progressStyle,
                icon = icon,
                title = DialogDefaults.title("Progress Dialog"),
                buttons = DialogDefaults.buttons(
                    positive = DialogButton("Stop")
                ),
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast("Progress Dialog closed by button")
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.Downloading,
            "Show Dialog Progress",
            "Shows an LINEAR progress dialog for 10 seconds (not cancelable)"
        )
    }

    @Composable
    private fun DemoDialogColor1(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {

            val color = remember { mutableStateOf(Color.Blue.copy(alpha = .5f)) }

            DialogColor(
                state = state,
                color = color,
                alphaSupported = true,
                icon = icon,
                title = DialogDefaults.title("Color Dialog"),
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast("Selected color: #${Integer.toHexString(color.value.toArgb())}")
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.ColorLens,
            "Show Color Dialog",
            "Shows a color dialog (alpha supported)"
        )
    }

    @Composable
    private fun DemoDialogColor2(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {

            val color = remember { mutableStateOf(Color.Red) }
            DialogColor(
                state = state,
                color = color,
                alphaSupported = false,
                labelStyle = DialogColorLabelStyle.Percent,
                icon = icon,
                title = DialogDefaults.title("Color Dialog"),
                style = style,
                onEvent = {
                    if (it is DialogEvent.Button && it.button == DialogButtonType.Positive) {
                        showToast("Selected color: #${Integer.toHexString(color.value.toArgb())}")
                    } else {
                        showToast("Event $it")
                    }
                }
            )
        }
        DemoDialogButton(
            state,
            Icons.Default.ColorLens,
            "Show Color Dialog",
            "Shows a color dialog (alpha NOT supported + RGB values are shown in percentages in all sliders)"
        )
    }

    // ----------------
    // Demos - Custom Dialogs
    // ----------------

    @Composable
    private fun DemoDialogCustom1(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {
            Dialog(
                state = state,
                style = style,
                icon = icon,
                title = DialogDefaults.title("Custom Dialog")
            ) {
                var checked by rememberSaveable { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                    Text(text = "Show Details")
                }
                AnimatedVisibility(visible = checked) {
                    Column {
                        Text(text = "Detail 1...")
                        Text(text = "Detail 2...")
                        Text(text = "Detail 3...")
                    }
                }
            }
        }
        DemoDialogButton(
            state,
            Icons.Default.Build,
            "Show Custom Dialog (Resizing)",
            "Shows a custom dialog with resizeable content"
        )
    }

    @Composable
    private fun DemoDialogCustom2(style: DialogStyle, icon: DialogIcon?) {
        val state = rememberDialogState()
        if (state.showing) {
            Dialog(
                state = state,
                style = style,
                icon = icon,
                title = DialogDefaults.title("Custom Dialog")
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    for (i in 1..50)
                        Text(modifier = Modifier.fillMaxWidth(), text = "Row $i")
                }
            }
        }
        DemoDialogButton(
            state,
            Icons.Default.Build,
            "Show Custom Dialog (Nested Scrolling)",
            "Shows a custom dialog with scrollable content"
        )
    }

    // ----------------
    // Helper functions
    // ----------------

    @Composable
    private fun SettingsRowCheckbox(state: MutableState<Boolean>, title: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = state.value, onCheckedChange = { state.value = it })
            Text(title)
        }
    }

    @Composable
    private fun <T> SettingsRowSegmentedControl(
        state: MutableState<T>,
        items: List<T>
    ) {
        SegmentedControl(
            modifier = Modifier.wrapContentWidth(),
            items = items.map { it.toString() }
        ) {
            state.value = items[it]
        }
    }

    @Composable
    private fun DemoDialogRegion(title: String) {
        Text(
            modifier = Modifier.padding(all = 4.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

    @Composable
    private fun DemoDialogButton(
        state: DialogState,
        icon: ImageVector,
        label: String,
        description: String
    ) {
        ElevatedCard(
            modifier = Modifier
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .clickable { state.show() }
                .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = ""
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(text = label, style = MaterialTheme.typography.titleSmall)
                    Text(text = description, style = MaterialTheme.typography.bodySmall)
                }
            }

        }
    }

    var lastToast: Toast? = null

    private fun showToast(text: String) {
        lastToast?.cancel()
        lastToast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        lastToast?.show()
    }
}