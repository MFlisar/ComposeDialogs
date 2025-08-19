package com.michaelflisar.composedialogs.demo

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState

@Composable
actual fun RootSubPageHeader(selectedDemoPage: MutableIntState) {
    if (selectedDemoPage.value != -1) {
        Row {
            IconButton(
                onClick = {
                    selectedDemoPage.intValue = -1
                }
            ) {
                Icon(Icons.Default.ArrowBack, null)
            }
        }
    }
}