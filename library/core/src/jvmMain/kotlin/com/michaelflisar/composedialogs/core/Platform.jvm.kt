package com.michaelflisar.composedialogs.core

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp

@Composable
actual fun isLandscape() = false

@Composable
actual fun stringOk() = "OK"

@Composable
actual fun DialogDefaults.defaultDialogStyle() = styleDialog()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // empty
}

@Composable
actual fun DialogContentScrollableColumn(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    val contentHeight = remember { mutableStateOf(0) }
    Box(
        modifier = modifier,// .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(end = 4.dp)
                .wrapContentHeight()
                .onSizeChanged {
                    contentHeight.value = it.height
                }

        ) {
            content()
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).height(contentHeight.value.dp),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}

@Composable
actual fun DialogContentScrollableLazyColumn(
    modifier: Modifier,
    state: LazyListState,
    content: LazyListScope.() -> Unit
) {
    val contentHeight = remember { mutableStateOf(0) }
    Box(
        modifier = modifier
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
                .wrapContentHeight()
                .onSizeChanged {
                    contentHeight.value = it.height
                },
            state = state
        ) {
            content()
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).height(contentHeight.value.dp),
            adapter = rememberScrollbarAdapter(state)
        )
    }
}

@Composable
actual fun updateStatusbarColor(darkStatusBar: Boolean) {}