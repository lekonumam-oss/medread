package com.example.medread.model

/**
 * WardActivity — one clinical task inside a ward (has a step checklist).
 */
data class WardActivity(
    val title: String,
    val steps: List<String>,
)

/**
 * Ward — one of the four hospital wards the student rotates through.
 */
data class Ward(
    val id: Int,
    val name: String,        // display name, e.g. "Medical Ward"
    val icon: String,        // emoji
    val tagline: String,     // short description shown on the list card
    val activities: List<WardActivity>,
)

/**
 * WardData — all four wards with their activities.
 * Extend activities here without touching any UI code.
 */
object WardData {

    val wards: List<Ward> = listOf(

        // ── 1. Medical Ward ───────────────────────────────────────────────────
        Ward(
            id = 1,
            name = "Medical Ward",
            icon = "🩺",
            tagline = "Internal medicine, chronic disease management",
            activities = listOf(
                WardActivity(
                    "Patient History Taking",
                    listOf(
                        "Chief complaint",
                        "History of present illness",
                        "Past medical history (DM, HTN, IHD…)",
                        "Drug history & allergies",
                        "Family & social history",
                    )
                ),
                WardActivity(
                    "Physical Examination",
                    listOf(
                        "General appearance & vitals",
                        "Cardiovascular exam (JVP, apex, S1/S2)",
                        "Respiratory exam (percussion, auscultation)",
                        "Abdominal exam (liver, spleen, ascites)",
                        "Neurological screening",
                    )
                ),
                WardActivity(
                    "Case Presentation (Ward Round)",
                    listOf(
                        "Patient ID & diagnosis in one sentence",
                        "Key history points",
                        "Relevant examination findings",
                        "Investigations summary",
                        "Assessment & management plan",
                    )
                ),
                WardActivity(
                    "Drug Chart Review",
                    listOf(
                        "Verify patient identity & allergies",
                        "Check correct drug, dose, route, frequency",
                        "Look for drug–drug interactions",
                        "Confirm renal/hepatic dose adjustments",
                        "Sign & time the prescription",
                    )
                )
            )
        ),

        // ── 2. Gynecology Ward ────────────────────────────────────────────────
        Ward(
            id = 2,
            name = "Gyn Ward",
            icon = "🌸",
            tagline = "Obstetrics & gynaecology rotations",
            activities = listOf(
                WardActivity(
                    "Obstetric History Taking",
                    listOf(
                        "LMP & EDD (Naegele's rule)",
                        "Gravida / Para / Abortion",
                        "Antenatal care visits & scans",
                        "Previous obstetric complications",
                        "Current pregnancy symptoms (bleeding, pain, discharge)",
                    )
                ),
                WardActivity(
                    "Per Abdomen Examination",
                    listOf(
                        "Fundal height (weeks vs. cm)",
                        "Lie & presentation (Leopold's manoeuvres)",
                        "Engagement of presenting part",
                        "Fetal heart rate (Pinnard / Doppler)",
                        "Uterine contraction frequency & duration",
                    )
                ),
                WardActivity(
                    "CTG Interpretation",
                    listOf(
                        "Baseline FHR (110–160 bpm normal)",
                        "Baseline variability (5–25 bpm reassuring)",
                        "Identify accelerations (reassuring)",
                        "Identify decelerations (early / late / variable)",
                        "Overall classification: normal / suspicious / pathological",
                    )
                ),
                WardActivity(
                    "Vaginal Delivery Checklist",
                    listOf(
                        "Confirm full dilatation & station",
                        "Maternal position & pushing technique",
                        "Control delivery of the head (Ritgen's)",
                        "Check for cord around neck",
                        "Active management of 3rd stage (oxytocin)",
                    )
                )
            )
        ),

        // ── 3. Pediatrics Ward ────────────────────────────────────────────────
        Ward(
            id = 3,
            name = "Ped Ward",
            icon = "👶",
            tagline = "Neonates, infants & children",
            activities = listOf(
                WardActivity(
                    "Pediatric History Taking",
                    listOf(
                        "Chief complaint (from parent/caregiver)",
                        "Birth history (gestation, birth weight, APGAR)",
                        "Feeding history (breast / formula)",
                        "Immunisation status",
                        "Developmental history (milestones)",
                    )
                ),
                WardActivity(
                    "Growth Assessment",
                    listOf(
                        "Measure weight (kg) & plot on WHO chart",
                        "Measure height/length & plot",
                        "Measure head circumference (< 3 yrs)",
                        "Calculate BMI & z-score",
                        "Assess nutritional status (SAM / MAM / normal)",
                    )
                ),
                WardActivity(
                    "Developmental Milestones",
                    listOf(
                        "Gross motor (head control, sitting, walking)",
                        "Fine motor (palmar grasp, pincer, drawing)",
                        "Language (cooing, babbling, words, sentences)",
                        "Social (smiling, stranger anxiety, play)",
                        "Flag red flags for referral",
                    )
                ),
                WardActivity(
                    "Neonatal Examination",
                    listOf(
                        "APGAR score at 1 & 5 minutes",
                        "Head (fontanelles, sutures, caput)",
                        "Eyes (red reflex), palate, hips (Barlow/Ortolani)",
                        "Cardiovascular: murmurs, femoral pulses",
                        "Genitalia, spine, limbs, skin",
                    )
                )
            )
        ),

        // ── 4. Surgery Ward ───────────────────────────────────────────────────
        Ward(
            id = 4,
            name = "Surgery Ward",
            icon = "🔪",
            tagline = "Pre-op, intra-op & post-op care",
            activities = listOf(
                WardActivity(
                    "Pre-op Assessment",
                    listOf(
                        "Confirm diagnosis & consent signed",
                        "NPO status (6 hrs solids, 2 hrs clear fluids)",
                        "Allergies & current medications",
                        "Bloods: FBC, U&E, coagulation, crossmatch",
                        "Mark operative site with patient awake",
                    )
                ),
                WardActivity(
                    "Surgical History Taking",
                    listOf(
                        "Presenting complaint & duration",
                        "Previous surgeries & anaesthetic problems",
                        "Bleeding disorders / anticoagulant use",
                        "Cardiovascular & respiratory fitness (METs)",
                        "ASA classification",
                    )
                ),
                WardActivity(
                    "Wound Care Protocol",
                    listOf(
                        "Hand hygiene & don gloves",
                        "Remove old dressing, inspect wound",
                        "Irrigate with normal saline",
                        "Apply appropriate dressing (dry / moist)",
                        "Document: size, colour, exudate, odour",
                    )
                ),
                WardActivity(
                    "Post-op Monitoring",
                    listOf(
                        "Airway, breathing, SpO2 on arrival to ward",
                        "BP, HR, temperature every 30 min × 2 hrs",
                        "Pain score & analgesic requirements",
                        "Drain output & urine output (> 0.5 ml/kg/hr)",
                        "Nausea, vomiting, bowel sounds",
                    )
                )
            )
        )
    )
}
