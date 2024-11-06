package com.example.labmate.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labmate.data.models.Experiment
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExperimentViewModel : ViewModel() {
    private val _experiments = MutableStateFlow<List<Experiment>>(emptyList())
    val experiments: StateFlow<List<Experiment>> = _experiments

    fun fetchExperiments(labRef: DocumentReference) {
        viewModelScope.launch {
            labRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val experimentsList = document.data?.map { (experimentName, experimentId) ->
                            Experiment(
                                experimentName = experimentName,
                                documentId = experimentId.toString()
                            )
                        } ?: emptyList()
                        _experiments.value = experimentsList
                        Log.d("Experiments", "Fetched experiments: $experimentsList")
                    } else {
                        Log.d("Experiments", "No data found for the lab reference")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Experiments", "Error fetching data for lab reference", exception)
                }
        }
    }
}
