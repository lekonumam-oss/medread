package com.example.medread.model

/**
 * Subject.kt — one medical subject shown on the Home screen.
 */
data class Subject(
    val id: Int,
    val name: String,
    val icon: String,        // emoji icon
    val description: String,
)
