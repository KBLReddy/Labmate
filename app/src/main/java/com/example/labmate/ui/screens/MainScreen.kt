package com.example.labmate.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
                    ExperimentsListScreen(labRef = it.refId)
                }
            }
        }
    }
}
