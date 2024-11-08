package com.example.labmate.ui.screens

// Imports
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// Utility Functions

/**
 * Saves the input stream (e.g., PDF file) to local storage for PDF rendering.
 */
fun savePdfToLocalStorage(context: Context, inputStream: InputStream): File {
    val file = File(context.cacheDir, "temp.pdf")
    FileOutputStream(file).use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    return file
}

/**
 * Renders all pages in a PDF file as Bitmap images.
 */
fun renderPdfPages(context: Context, pdfFile: File): List<Bitmap> {
    val bitmaps = mutableListOf<Bitmap>()
    val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val pdfRenderer = PdfRenderer(fileDescriptor)

    for (i in 0 until pdfRenderer.pageCount) {
        val page = pdfRenderer.openPage(i)
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        bitmaps.add(bitmap)
        page.close()
    }

    pdfRenderer.close()
    fileDescriptor.close()
    return bitmaps
}

// Firebase Download Function

/**
 * Downloads a PDF file from Firebase Storage, given a Firebase Storage gs:// URL.
 */
fun downloadPdfFromFirebase(context: Context, onDownloadComplete: (File) -> Unit={},expStorage: String) {
    val storage = Firebase.storage
    val storageReference = storage.getReferenceFromUrl(expStorage)

    // Define a local file to store the downloaded PDF
    val localFile = File(context.cacheDir, "Exp_1.pdf")

    storageReference.getFile(localFile)
        .addOnSuccessListener {
            onDownloadComplete(localFile)
        }
        .addOnFailureListener { exception ->
            Log.e("FirebaseDownload", "Failed to download PDF: ${exception.message}")
        }
}

// Composable Functions

/**
 * Displays a single page of a PDF as an image.
 */
@Composable
fun PdfPage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Renders a list of Bitmaps (PDF pages) in a scrollable LazyColumn.
 */
@Composable
fun PdfViewer(bitmapPages: List<Bitmap>) {
    LazyColumn {
        items(bitmapPages) { bitmap ->
            PdfPage(bitmap = bitmap)
        }
    }
}

/**
 * Manages the process of rendering PDF pages in the UI from a local file.
 */
@Composable
fun PdfViewerScreen(pdfFile: File) {
    val context = LocalContext.current
    val bitmapPages by remember { mutableStateOf(renderPdfPages(context, pdfFile)) }

    if (bitmapPages.isEmpty()) {
        Text(text = "Loading PDF...")
    } else {
        PdfViewer(bitmapPages = bitmapPages)
    }
}

/**
 * Downloads and displays a PDF from Firebase in a scrollable viewer.
 */
@Composable
fun PdfViewerScreenFromFirebase(expStorage: String) {
    val context = LocalContext.current
    var pdfFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(Unit) {
        downloadPdfFromFirebase(expStorage = expStorage ,context = context, onDownloadComplete = { downloadedFile ->
            pdfFile = downloadedFile
        })
    }

    if (pdfFile == null) {
        Text(text = "Downloading PDF...")
    } else {
        PdfViewerScreen(pdfFile = pdfFile!!)
    }
}