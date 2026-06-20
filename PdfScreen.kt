package com.example.medread.ui

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.medread.model.StudyMaterial
import com.example.medread.model.SampleData
import com.example.medread.ui.theme.MedReadTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * PdfScreen — renders a PDF from assets/pdfs/ page by page.
 * Uses Android's built-in PdfRenderer — no external library needed.
 *
 * @param material    the material to open
 * @param onBackClick called when the user taps "← Back"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfScreen(
    material: StudyMaterial,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // null = loading, empty list = error, non-empty = pages ready
    var pages by remember { mutableStateOf<List<Bitmap>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Load the PDF on a background thread when the screen first appears
    LaunchedEffect(material.fileName) {
        withContext(Dispatchers.IO) {
            try {
                pages = renderPdf(context, material.fileName)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(material.title, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium, maxLines = 1)
                        Text(material.fileName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("← Back", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE8E8E8))
        ) {
            when {
                errorMessage != null -> ErrorState(errorMessage!!)
                pages == null        -> LoadingState()
                pages!!.isEmpty()    -> ErrorState("PDF could not be read or has no pages.")
                else                 -> PageList(pages = pages!!)
            }
        }
    }
}

@Composable
private fun PageList(pages: List<Bitmap>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(pages) { index, bitmap ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Page ${index + 1}",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                    Text(
                        "Page ${index + 1} of ${pages.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Loading PDF…", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun ErrorState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📄", fontSize = 56.sp)
        Spacer(Modifier.height(16.dp))
        Text("Could not open PDF", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Card(colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Text(
                "💡 Add your PDF to:\napp/src/main/assets/pdfs/${message.substringAfterLast("/")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

// ── PDF rendering helper ──────────────────────────────────────────────────────

/**
 * Copies a PDF from assets to a temp file (PdfRenderer needs a seekable file,
 * not an InputStream), then renders every page as a Bitmap.
 * Must be called from a background thread.
 */
private fun renderPdf(context: android.content.Context, fileName: String): List<Bitmap> {
    val input  = context.assets.open("pdfs/$fileName")
    val tmp    = File(context.cacheDir, "tmp_$fileName")
    tmp.outputStream().use { input.copyTo(it) }
    input.close()

    val fd       = ParcelFileDescriptor.open(tmp, ParcelFileDescriptor.MODE_READ_ONLY)
    val renderer = PdfRenderer(fd)
    val pages    = mutableListOf<Bitmap>()

    for (i in 0 until renderer.pageCount) {
        val page   = renderer.openPage(i)
        val scale  = 2                                    // 2× for crisp text
        val bitmap = Bitmap.createBitmap(
            page.width * scale, page.height * scale, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(android.graphics.Color.WHITE)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        pages.add(bitmap)
    }

    renderer.close()
    fd.close()
    return pages
}

@Preview(showBackground = true)
@Composable
fun PdfScreenPreview() {
    MedReadTheme {
        PdfScreen(
            material = SampleData.materials[0],
            onBackClick = {}
        )
    }
}
