package com.example.medread.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medread.model.Ward
import com.example.medread.model.WardActivity

/**
 * WardDetailScreen — shows all activities for one ward.
 * Each activity card expands to reveal a tappable step checklist.
 *
 * @param ward         the ward chosen on WardScreen
 * @param onBackClick  called when the user taps "← Back"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardDetailScreen(
    ward: Ward,
    onBackClick: () -> Unit
) {
    // Which activity card is currently open (null = all collapsed)
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    // Checked steps: key = "activityIndex-stepIndex"
    var checkedSteps by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(ward.icon, fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(ward.name, fontWeight = FontWeight.Bold)
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ward.activities.size) { actIdx ->
                val activity = ward.activities[actIdx]
                val isOpen = expandedIndex == actIdx
                val doneCount = activity.steps.indices
                    .count { si -> "$actIdx-$si" in checkedSteps }
                val totalSteps = activity.steps.size
                val progress = if (totalSteps == 0) 0f
                               else doneCount.toFloat() / totalSteps.toFloat()

                ActivityCard(
                    activity    = activity,
                    actIdx      = actIdx,
                    isOpen      = isOpen,
                    progress    = progress,
                    doneCount   = doneCount,
                    totalSteps  = totalSteps,
                    checkedSteps = checkedSteps,
                    onToggle    = {
                        expandedIndex = if (isOpen) null else actIdx
                    },
                    onStepChecked = { key ->
                        checkedSteps = if (key in checkedSteps)
                            checkedSteps - key
                        else
                            checkedSteps + key
                    }
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: WardActivity,
    actIdx: Int,
    isOpen: Boolean,
    progress: Float,
    doneCount: Int,
    totalSteps: Int,
    checkedSteps: Set<String>,
    onToggle: () -> Unit,
    onStepChecked: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {

            // ── Header — always visible ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        activity.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .weight(1f)
                                .height(5.dp),
                            color = if (progress == 1f) Color(0xFF2E7D32)
                                    else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "$doneCount/$totalSteps",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    if (isOpen) "▲" else "▼",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ── Expandable step checklist ──────────────────────────────────────
            AnimatedVisibility(visible = isOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(Modifier.height(2.dp))

                    activity.steps.forEachIndexed { stepIdx, step ->
                        val key = "$actIdx-$stepIdx"
                        val isDone = key in checkedSteps

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onStepChecked(key) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                if (isDone) "✅" else "⬜",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(top = 1.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "${stepIdx + 1}. $step",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isDone)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Completion banner
                    if (progress == 1f) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                "✅  Activity complete!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
