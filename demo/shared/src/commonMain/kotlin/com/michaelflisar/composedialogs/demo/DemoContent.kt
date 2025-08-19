package com.michaelflisar.composedialogs.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.demo.classes.DemoStyle
import com.michaelflisar.composedialogs.demo.composables.SegmentedControl
import com.michaelflisar.composedialogs.demo.demos.BillingDemos
import com.michaelflisar.composedialogs.demo.demos.ColorDemos
import com.michaelflisar.composedialogs.demo.demos.CustomDemos
import com.michaelflisar.composedialogs.demo.demos.DateTimeDemos
import com.michaelflisar.composedialogs.demo.demos.InfoDemos
import com.michaelflisar.composedialogs.demo.demos.InputDemos
import com.michaelflisar.composedialogs.demo.demos.ListDemos
import com.michaelflisar.composedialogs.demo.demos.NumberDemos
import com.michaelflisar.composedialogs.demo.demos.ProgressDemos
import com.michaelflisar.composedialogs.demo.demos.SingleDialogWithListDemos


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DemoContent(
    modifier: Modifier,
    showInfo: (info: String) -> Unit,
) {
    val selectedDemoPage = rememberSaveable { mutableIntStateOf(-1) }

    BackHandler(selectedDemoPage.intValue != -1) {
        selectedDemoPage.intValue = -1
    }

    // Dialog Setting States
    val demoStyle = rememberSaveable { mutableStateOf(DemoStyle.Dialog) }
    val swipeDismiss = rememberSaveable { mutableStateOf(false) }
    val showIcon = rememberSaveable { mutableStateOf(true) }

    val style = if (demoStyle.value == DemoStyle.BottomSheet) {
        DialogDefaults.styleBottomSheet(
            dragHandle = true,
            animateShow = true
            // ...
        )
    } else {
        DialogDefaults.styleDialog(
            swipeDismissable = swipeDismiss.value
            // ...
        )
    }
    val icon: @Composable (() -> Unit)? = if (showIcon.value) {
        { Icon(Icons.Default.Home, null) }
    } else null

    // -----------------
    // Demos
    // -----------------

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (selectedDemoPage.intValue == -1) {
            RootContent(
                selectedDemoPage,
                demoStyle,
                swipeDismiss,
                showIcon
            )
        } else {
            RootSubPageHeader(selectedDemoPage)
            when (selectedDemoPage.intValue) {

                // Predefined dialog demos
                0 -> InfoDemos(style, icon, showInfo)
                1 -> InputDemos(style, icon, showInfo)
                2 -> NumberDemos(style, icon, showInfo)
                3 -> DateTimeDemos(style, icon, showInfo)
                4 -> ColorDemos(style, icon, showInfo)
                5 -> ListDemos(style, icon, showInfo)
                6 -> ProgressDemos(style, icon, showInfo)
                7 -> SingleDialogWithListDemos(style, icon, showInfo)
                8 -> BillingDemos(style, icon, showInfo)

                // Manually created custom demos
                99 -> CustomDemos(style, icon, showInfo)
            }
        }
    }
}

@Composable
expect fun RootSubPageHeader(selectedDemoPage: MutableIntState)

@Composable
private fun ColumnScope.RootContent(
    selectedDemoPage: MutableIntState,
    style: MutableState<DemoStyle>,
    swipeDismiss: MutableState<Boolean>,
    showIcon: MutableState<Boolean>
) {
    Text("Settings", style = MaterialTheme.typography.titleMedium)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SettingsRowSegmentedControl(style, DemoStyle.entries)
        AnimatedVisibility(visible = style.value == DemoStyle.Dialog) {
            SettingsRowCheckbox(swipeDismiss, "Support SwipeDismiss?")
        }
        SettingsRowCheckbox(showIcon, "Show Icon?")
    }


    Text("Demos", style = MaterialTheme.typography.titleMedium)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MainButton(selectedDemoPage, 0, "Info Demos")
        MainButton(selectedDemoPage, 1, "Input Demos")
        MainButton(selectedDemoPage, 2, "Number Demos")
        MainButton(selectedDemoPage, 3, "Date/Time Demos")
        MainButton(selectedDemoPage, 4, "Color Demos")
        MainButton(selectedDemoPage, 5, "List Demos")
        MainButton(selectedDemoPage, 6, "Progress Demos")
        MainButton(selectedDemoPage, 7, "Single Dialog with List")
        MainButton(selectedDemoPage, 8, "Billing Demos")
        HorizontalDivider()
        MainButton(selectedDemoPage, 99, "Custom Demos")
    }
}

@Composable
private fun MainButton(selectedDemoPage: MutableState<Int>, page: Int, text: String) {
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { selectedDemoPage.value = page }
    ) {
        Text(text)
    }
}

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
        items = items.map { it.toString() },
        selectedIndex = items.indexOf(state.value)
    ) {
        state.value = items[it]
    }
}