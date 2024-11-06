package com.example.labmate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegulationScreen(
    modifier: Modifier = Modifier,
    onClickV20: () -> Unit = {},
    onClickV23: () -> Unit = {}
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onClickV20, modifier = Modifier.width(200.dp)) {
            Text("V20")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClickV23, modifier = Modifier.width(200.dp)) {
            Text("V23")
        }
    }
}
