package com.michaelflisar.composedialogs.demo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedemobaseactivity.DemoBaseActivity
import com.michaelflisar.composedemobaseactivity.classes.DemoTheme
import com.michaelflisar.composedemobaseactivity.classes.listSaverKeepEntryStateList
import com.michaelflisar.composedemobaseactivity.composables.DemoAppThemeRegion
import com.michaelflisar.composedemobaseactivity.composables.DemoCollapsibleRegion
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.styleBottomSheet
import com.michaelflisar.composedialogs.core.styleDialog
import com.michaelflisar.composedialogs.demo.classes.DemoStyle
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
import com.michaelflisar.composedialogs.demo.views.SegmentedControl

class MainActivity : DemoBaseActivity() {

    @Composable
    override fun Content(modifier: Modifier, theme: DemoTheme, dynamicTheme: Boolean) {

        val selectedDemoPage = rememberSaveable { mutableIntStateOf(-1) }

        BackHandler(selectedDemoPage.intValue != -1) {
            selectedDemoPage.intValue = -1
        }

        val expandedRootRegions = rememberSaveable(Unit, saver = listSaverKeepEntryStateList()) {
            mutableStateListOf(2)
        }

        // Dialog Setting States
        val style = rememberSaveable { mutableStateOf(DemoStyle.Dialog) }
        val swipeDismiss = rememberSaveable { mutableStateOf(false) }
        val showIcon = rememberSaveable { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
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
            val icon: @Composable (() -> Unit)? = if (showIcon.value) {
                { Icon(Icons.Default.Home, null) }
            } else null

            // -----------------
            // Demos
            // -----------------

            if (selectedDemoPage.intValue == -1) {
                RootContent(
                    modifier = modifier
                        .padding(top = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    theme,
                    dynamicTheme,
                    expandedRootRegions,
                    selectedDemoPage,
                    style,
                    swipeDismiss,
                    showIcon
                )
            } else {
                when (selectedDemoPage.intValue) {

                    // Predefined dialog demos
                    0 -> InfoDemos(s, icon)
                    1 -> InputDemos(s, icon)
                    2 -> NumberDemos(s, icon)
                    3 -> DateTimeDemos(s, icon)
                    4 -> ColorDemos(s, icon)
                    5 -> ListDemos(s, icon)
                    6 -> ProgressDemos(s, icon)
                    7 -> SingleDialogWithListDemos(s, icon)
                    8 -> BillingDemos(s, icon)

                    // Manually created custom demos
                    99 -> CustomDemos(s, icon)
                }
            }
        }
    }

    // ----------------
    // UI - Content
    // ----------------

    @Composable
    private fun RootContent(
        modifier: Modifier,
        theme: DemoTheme,
        dynamicTheme: Boolean,
        expandedRootRegions: SnapshotStateList<Int>,
        selectedDemoPage: MutableIntState,
        style: MutableState<DemoStyle>,
        swipeDismiss: MutableState<Boolean>,
        showIcon: MutableState<Boolean>
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            DemoAppThemeRegion(theme, dynamicTheme, id = 0, expandedIds = expandedRootRegions)

            DemoCollapsibleRegion(title = "Settings", id = 1, expandedIds = expandedRootRegions) {
                SettingsRowSegmentedControl(style, DemoStyle.values().toList())
                AnimatedVisibility(visible = style.value == DemoStyle.Dialog) {
                    SettingsRowCheckbox(swipeDismiss, "Support SwipeDismiss?")
                }
                SettingsRowCheckbox(showIcon, "Show Icon?")
            }

            DemoCollapsibleRegion(title = "Demos", id = 2, expandedIds = expandedRootRegions) {
                MainButton(selectedDemoPage, 0, "Info Demos")
                MainButton(selectedDemoPage, 1, "Input Demos")
                MainButton(selectedDemoPage, 2, "Number Demos")
                MainButton(selectedDemoPage, 3, "Date/Time Demos")
                MainButton(selectedDemoPage, 4, "Color Demos")
                MainButton(selectedDemoPage, 5, "List Demos")
                MainButton(selectedDemoPage, 6, "Progress Demos")
                MainButton(selectedDemoPage, 7, "Single Dialog with List")
                MainButton(selectedDemoPage, 8, "Billing Demos")
                Divider()
                MainButton(selectedDemoPage, 99, "Custom Demos")
            }
        }
    }

    // ----------------
    // Helper functions
    // ----------------

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
}