package com.example.labmate.ui.screens

// Imports
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// Firebase PDF URL Function

/**
 * Fetches the download URL of a PDF from Firebase Storage.
 */
fun fetchPdfUrlFromFirebase(onUrlFetched: (String?) -> Unit) {
    val storage = Firebase.storage
    val storageReference = storage.getReferenceFromUrl("gs://labmate-e8ac0.appspot.com/AES LAB/Exp_1.pdf")

    storageReference.downloadUrl
        .addOnSuccessListener { uri ->
            onUrlFetched(uri.toString())
        }
        .addOnFailureListener { exception ->
            Log.e("FirebaseDownload", "Failed to get PDF URL: ${exception.message}")
            onUrlFetched(null)
        }
}

// Composable Functions

/**
 * Displays a PDF from a URL in a WebView.
 */
@Composable
fun PdfWebViewer(pdfUrl: String) {
    val context = LocalContext.current

    AndroidView(factory = {
        WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")
        }
    })
}

/**
 * Downloads and displays a PDF from Firebase in a WebView.
 */
@Composable
fun PdfViewerScreenFromFirebaseUrl() {
    var pdfUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fetchPdfUrlFromFirebase { url ->
            pdfUrl = url
        }
    }

    if (pdfUrl == null) {
        Text(text = "Loading PDF...")
    } else {
        PdfWebViewer(pdfUrl = pdfUrl!!)
    }
}
