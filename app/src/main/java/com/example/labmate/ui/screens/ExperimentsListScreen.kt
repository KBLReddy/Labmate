package com.example.labmate.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.labmate.data.models.Experiment
import com.google.firebase.firestore.DocumentReference

@Composable
fun ExperimentsListScreen(
    modifier: Modifier = Modifier,
    experimentViewModel: ExperimentViewModel = viewModel(),
    labRef: DocumentReference
) {
    LaunchedEffect(labRef) {
        experimentViewModel.fetchExperiments(labRef)
    }
    val experiments = experimentViewModel.experiments.collectAsState()
    LazyColumn(modifier.fillMaxSize()) {
        items(experiments.value) { experiment ->
            ItemExperimentsListScreen(word = experiment.experimentName)
        }
    }
}

@Composable
fun ItemExperimentsListScreen(modifier: Modifier = Modifier, word: String) {
    Column(modifier.padding(4.dp)) {
        Text(text = word)
    }
}
