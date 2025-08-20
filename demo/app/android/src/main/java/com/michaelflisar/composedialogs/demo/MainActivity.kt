package com.michaelflisar.composedialogs.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.michaelflisar.democomposables.DemoScaffold

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                DemoScaffold(
                    appName = stringResource(R.string.app_name)
                ) { modifier, showInfo ->
                    DemoContent(
                        modifier = modifier,
                        showInfo = showInfo
                    )
                }
            }
        }
    }
}
