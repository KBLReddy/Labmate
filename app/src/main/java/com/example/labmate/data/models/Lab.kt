package com.example.labmate.data.models

import com.google.firebase.firestore.DocumentReference

data class Lab(
    val name:String,
    val refId: DocumentReference
)
