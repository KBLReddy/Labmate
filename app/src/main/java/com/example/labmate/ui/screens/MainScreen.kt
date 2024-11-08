package com.example.labmate.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class MainScreen(val title: String) {
    Regulation("Regulation"),
    Labs("Labs"),
    Experiments("List of Experiments"),
    Experiment("Experiment")
}
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val labsViewModel: LabsViewModel = viewModel()


    Scaffold(){ innerPadding ->
        NavHost(
            startDestination = MainScreen.Regulation.name,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route =MainScreen.Regulation.name) {
                RegulationScreen(onClickV20 = {
                    Log.d("MainScreen", "Navigating to labs list")
                    navController.navigate(MainScreen.Labs.name)
                })
            }

            composable(route = MainScreen.Labs.name) {
                LabsListScreen(labsViewModel = labsViewModel, onClick = { lab ->
                    labsViewModel.setSelectedLab(lab)
                    navController.navigate(MainScreen.Experiments.name)
                })
            }

            composable(route = MainScreen.Experiments.name) {
                val selectedLab = labsViewModel.selectedLab.collectAsState().value
                selectedLab?.let {
                    ExperimentsListScreen(labsViewModel = labsViewModel,labRef = it.refId, onClick = {experiment->
                        labsViewModel.setSelectedExperiment(experiment)
                        navController.navigate(MainScreen.Experiment.name)
                    })
                }
            }
            composable(route = MainScreen.Experiment.name){
                val selectedExperiment = labsViewModel.selectedExperiment.collectAsState().value
                    PdfViewerScreenFromFirebase(expStorage = selectedExperiment?.documentId ?: "gs://labmate-e8ac0.appspot.com/AES LAB/Exp_1.pdf")
            }
        }
    }
}
