package com.example.medread

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.medread.model.StudyMaterial
import com.example.medread.model.SampleData
import com.example.medread.model.Subject
import com.example.medread.model.Ward
import com.example.medread.ui.HomeScreen
import com.example.medread.ui.PdfScreen
import com.example.medread.ui.SubjectScreen
import com.example.medread.ui.WardDetailScreen
import com.example.medread.ui.WardScreen
import com.example.medread.ui.theme.MedReadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedReadTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedReadApp()
                }
            }
        }
    }
}

// ── Navigation state ──────────────────────────────────────────────────────────

private sealed class Screen {
    object Home : Screen()
    data class SubjectMaterials(val subject: Subject) : Screen()
    data class Pdf(val material: StudyMaterial) : Screen()
    object Wards : Screen()
    data class WardDetail(val ward: Ward) : Screen()
}

@Composable
private fun MedReadApp() {
    var currentScreen: Screen by remember { mutableStateOf(Screen.Home) }

    when (val screen = currentScreen) {

        is Screen.Home -> HomeScreen(
            onSubjectClick = { currentScreen = Screen.SubjectMaterials(it) },
            onWardClick    = { currentScreen = Screen.Wards }
        )

        is Screen.SubjectMaterials -> SubjectScreen(
            subject         = screen.subject,
            onBackClick     = { currentScreen = Screen.Home },
            onMaterialClick = { currentScreen = Screen.Pdf(it) }
        )

        is Screen.Pdf -> PdfScreen(
            material    = screen.material,
            onBackClick = {
                val subject = SampleData.subjects.first { s: Subject -> s.id == screen.material.subjectId }
                currentScreen = Screen.SubjectMaterials(subject)
            }
        )

        is Screen.Wards -> WardScreen(
            onBackClick  = { currentScreen = Screen.Home },
            onWardClick  = { currentScreen = Screen.WardDetail(it) }
        )

        is Screen.WardDetail -> WardDetailScreen(
            ward        = screen.ward,
            onBackClick = { currentScreen = Screen.Wards }
        )
    }
}
