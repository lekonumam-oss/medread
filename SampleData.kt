package com.example.medread.model

/**
 * SampleData — all hard-coded content for the app.
 * In a real app you might load this from a JSON file or database.
 */
object SampleData {

    val subjects: List<Subject> = listOf(
        Subject(1, "Anatomy",       "🦴", "Study of body structure"),
        Subject(2, "Physiology",    "❤️", "Study of body functions"),
        Subject(3, "Biochemistry",  "🧪", "Study of chemical processes"),
        Subject(4, "Pathology",     "🔬", "Study of diseases"),
        Subject(5, "Pharmacology",  "💊", "Study of drugs and medicines"),
    )

    val materials: List<StudyMaterial> = listOf(
        // Anatomy
        StudyMaterial(1,  1, "Introduction to Anatomy",   "intro_anatomy.pdf"),
        StudyMaterial(2,  1, "Skeletal System",            "skeletal_system.pdf"),
        StudyMaterial(3,  1, "Muscular System",            "muscular_system.pdf"),
        // Physiology
        StudyMaterial(4,  2, "Introduction to Physiology", "intro_physiology.pdf"),
        StudyMaterial(5,  2, "Cardiovascular System",      "cardiovascular.pdf"),
        StudyMaterial(6,  2, "Nervous System",             "nervous_system.pdf"),
        // Biochemistry
        StudyMaterial(7,  3, "Introduction to Biochemistry","intro_biochemistry.pdf"),
        StudyMaterial(8,  3, "Proteins and Enzymes",       "proteins_enzymes.pdf"),
        StudyMaterial(9,  3, "Carbohydrate Metabolism",    "carb_metabolism.pdf"),
        // Pathology
        StudyMaterial(10, 4, "Introduction to Pathology",  "intro_pathology.pdf"),
        StudyMaterial(11, 4, "Cell Injury and Adaptation", "cell_injury.pdf"),
        StudyMaterial(12, 4, "Inflammation",               "inflammation.pdf"),
        // Pharmacology
        StudyMaterial(13, 5, "Introduction to Pharmacology","intro_pharmacology.pdf"),
        StudyMaterial(14, 5, "Drug Receptors",             "drug_receptors.pdf"),
        StudyMaterial(15, 5, "Autonomic Pharmacology",     "autonomic_pharmacology.pdf"),
    )

    /** Returns materials belonging to a specific subject. */
    fun forSubject(subjectId: Int): List<StudyMaterial> =
        materials.filter { it.subjectId == subjectId }
}
