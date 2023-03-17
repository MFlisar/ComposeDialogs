package com.michaelflisar.composedialogs.demo.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    label: String = "",
    items: List<String>,
    selected: String,
    enabled: Boolean = true,
    onItemSelected: (index: Int, item: String) -> Unit = { _, _ -> }
) {
    Spinner(
        modifier,
        label,
        items,
        selected,
        { item -> item },
        enabled,
        onItemSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    label: String = "",
    items: List<T>,
    selected: T,
    itemToString: @Composable (T) -> String,
    enabled: Boolean = true,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    var selected by remember { mutableStateOf(selected) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = modifier
                .focusable(false)
                .menuAnchor(),
            readOnly = true,
            enabled = enabled,
            value = itemToString(selected),
            onValueChange = { },
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = modifier
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        if (item != selected) {
                            selected = item
                            onItemSelected.invoke(index, item)
                        }
                        expanded = false
                    },
                    text = {
                        Text(text = itemToString(item))
                    }
                )
            }
        }
    }
}