package com.example.medread.model

/**
 * StudyMaterial — a study material (PDF file) inside a subject.
 */
data class StudyMaterial(
    val id: Int,
    val subjectId: Int,   // links to its parent Subject
    val title: String,    // display name
    val fileName: String, // PDF filename in assets/pdfs/
)
