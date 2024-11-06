package com.example.labmate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.labmate.data.models.Lab

@Composable
fun LabsListScreen(
    modifier: Modifier = Modifier,
    labsViewModel: LabsViewModel = viewModel(),
    onClick: (Lab) -> Unit = {}
) {
    val labs = labsViewModel.labs.collectAsState()
    LazyColumn(modifier.fillMaxSize()) {
        items(labs.value) { lab ->
            ItemLabsListComposable(text = lab.name, onClick = { onClick(lab) })
        }
    }
}

@Composable
fun ItemLabsListComposable(modifier: Modifier = Modifier, text: String, onClick: () -> Unit = {}) {
    Card(modifier.fillMaxWidth().padding(4.dp)) {
        Text(text, fontSize = 24.sp, modifier = Modifier.padding(8.dp).clickable { onClick() })
    }
}
