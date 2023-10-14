package com.michaelflisar.composedialogs.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogState

@Composable
fun DemoDialogRegion(title: String) {
    Text(
        modifier = Modifier.padding(all = 4.dp),
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun DemoDialogRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
fun RowScope.DemoDialogButton(
    state: DialogState,
    icon: ImageVector,
    label: String,
    description: String
) {
    ElevatedCard(
        modifier = Modifier.weight(1f)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
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