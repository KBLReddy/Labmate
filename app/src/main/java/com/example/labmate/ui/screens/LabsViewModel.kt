package com.example.labmate.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labmate.data.models.Experiment
import com.example.labmate.data.models.Lab
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LabsViewModel : ViewModel() {
    private val _labs = MutableStateFlow<List<Lab>>(emptyList())
    val labs: StateFlow<List<Lab>> = _labs
    private val _selectedLab = MutableStateFlow<Lab?>(null)
    private val _selectedExperiment = MutableStateFlow<Experiment?>(null)
    val selectedExperiment: StateFlow<Experiment?> = _selectedExperiment
    val selectedLab: StateFlow<Lab?> = _selectedLab
    private val db = FirebaseFirestore.getInstance()

    init {
        fetchLabs()
    }

    private fun fetchLabs() {
        viewModelScope.launch {
            db.collection("labs").document("mQhgDKjd44fXf89V3QBC").get()
                .addOnSuccessListener { document ->
                    try {
                        // Check if the document exists
                        if (document.exists()) {
                            // Map through the data safely and create Lab objects
                            val labList = document.data?.mapNotNull { (labName, labRef) ->
                                val reference = labRef as? DocumentReference
                                reference?.let {
                                    Lab(name = labName, refId = reference) // Assuming each lab has one experiment ref
                                }
                            } ?: emptyList()
                            _labs.value = labList
                            Log.d("Labs", "Fetched labs: $labList")
                            Log.d("Labs", "${labList[0].refId}")
                        } else {
                            Log.w("Labs", "No such document")
                        }
                    } catch (e: Exception) {
                        Log.e("Labs", "Error processing lab data", e)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Labs", "Error getting lab documents.", exception)
                }
        }
    }

    fun setSelectedLab(lab: Lab) {
        _selectedLab.value = lab
    }
    fun setSelectedExperiment(experiment: Experiment){
        _selectedExperiment.value = experiment
    }
}
