package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.data.model.LoggedSet
import com.example.data.model.SetType
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveWorkoutSession

/**
 * openGym inspired Live Exercise Set Logger and Superset Controller.
 * Displays interactive set tables, Set Types (Working, Warm-up, Drop-set, Failure),
 * live weight/reps counters, real-time tonnage, and Superset links.
 */
@Composable
fun OpenGymSetLoggingTable(
    exercise: Exercise,
    activeSession: ActiveWorkoutSession,
    allExercisesInDay: List<Exercise>,
    onUpdateSetLog: (exerciseId: String, setIndex: Int, weightKg: Float, reps: Int, type: SetType) -> Unit,
    onToggleSetDone: (exerciseId: String, setIndex: Int, exercise: Exercise) -> Unit,
    onToggleSuperset: (exerciseAId: String, exerciseBId: String) -> Unit,
    onOpen1RmCalculator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sets = activeSession.loggedSetsMap[exercise.id] ?: (1..exercise.recommendedSets).map {
        LoggedSet(it, SetType.WORKING, 0f, 10, false)
    }

    val supersetPartnerId = activeSession.supersetPairs[exercise.id]
    val supersetPartner = supersetPartnerId?.let { id -> allExercisesInDay.firstOrNull { it.id == id } }

    val exerciseVolume = sets.filter { it.isCompleted }.sumOf { (it.weightKg * it.reps).toDouble() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("opengym_set_table_${exercise.id}")
    ) {
        Divider(color = DarkSurfaceElevated, thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Superset status and header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (supersetPartner != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = OrangeAccent.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Link, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SUPERSET: ${supersetPartner.nameAz}",
                                color = OrangeAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                } else {
                    Text(
                        text = "openGym Set Jurnalı",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Real-time volume badge for this exercise
            if (exerciseVolume > 0) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = LimeSuccess.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Tonaj: ${exerciseVolume.toInt()} kq",
                        color = LimeSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SET", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(36.dp))
            Text("NÖV", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(42.dp))
            Text("ÇƏKİ (KQ)", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("TƏKRAR", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("BİTDİ", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(44.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Set Rows
        sets.forEachIndexed { setIdx, loggedSet ->
            val isDone = loggedSet.isCompleted

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isDone) DarkSurfaceElevated.copy(alpha = 0.7f) else DarkSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .then(
                        if (isDone) Modifier.border(1.dp, LimeSuccess.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        else Modifier
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Set Number
                    Text(
                        text = "${loggedSet.setNumber}",
                        color = if (isDone) LimeSuccess else TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(32.dp)
                    )

                    // Set Type Chip (click cycles through: WORKING -> WARMUP -> DROP_SET -> FAILURE)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when (loggedSet.type) {
                            SetType.WORKING -> CyanPrimary.copy(alpha = 0.2f)
                            SetType.WARMUP -> OrangeAccent.copy(alpha = 0.2f)
                            SetType.DROP_SET -> Color(0xFF818CF8).copy(alpha = 0.2f)
                            SetType.FAILURE -> Color(0xFFEF4444).copy(alpha = 0.2f)
                        },
                        modifier = Modifier
                            .clickable {
                                val nextType = when (loggedSet.type) {
                                    SetType.WORKING -> SetType.WARMUP
                                    SetType.WARMUP -> SetType.DROP_SET
                                    SetType.DROP_SET -> SetType.FAILURE
                                    SetType.FAILURE -> SetType.WORKING
                                }
                                onUpdateSetLog(exercise.id, setIdx, loggedSet.weightKg, loggedSet.reps, nextType)
                            }
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = loggedSet.type.shortCode,
                            color = when (loggedSet.type) {
                                SetType.WORKING -> CyanPrimary
                                SetType.WARMUP -> OrangeAccent
                                SetType.DROP_SET -> Color(0xFF818CF8)
                                SetType.FAILURE -> Color(0xFFEF4444)
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    // Weight stepper
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "-",
                            color = TextSecondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    val newW = (loggedSet.weightKg - 2.5f).coerceAtLeast(0f)
                                    onUpdateSetLog(exercise.id, setIdx, newW, loggedSet.reps, loggedSet.type)
                                }
                                .padding(horizontal = 4.dp)
                        )
                        Text(
                            text = "${loggedSet.weightKg.toInt()}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                        Text(
                            text = "+",
                            color = CyanPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    val newW = loggedSet.weightKg + 2.5f
                                    onUpdateSetLog(exercise.id, setIdx, newW, loggedSet.reps, loggedSet.type)
                                }
                                .padding(horizontal = 4.dp)
                        )
                    }

                    // Reps stepper
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "-",
                            color = TextSecondary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    val newR = (loggedSet.reps - 1).coerceAtLeast(1)
                                    onUpdateSetLog(exercise.id, setIdx, loggedSet.weightKg, newR, loggedSet.type)
                                }
                                .padding(horizontal = 4.dp)
                        )
                        Text(
                            text = "${loggedSet.reps}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                        Text(
                            text = "+",
                            color = CyanPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    val newR = (loggedSet.reps + 1).coerceAtMost(50)
                                    onUpdateSetLog(exercise.id, setIdx, loggedSet.weightKg, newR, loggedSet.type)
                                }
                                .padding(horizontal = 4.dp)
                        )
                    }

                    // Completion Checkbox
                    IconButton(
                        onClick = { onToggleSetDone(exercise.id, setIdx, exercise) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDone) LimeSuccess else DarkSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Superset & 1RM Helper Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Find next exercise to pair with
            val currentIndex = allExercisesInDay.indexOfFirst { it.id == exercise.id }
            val nextExercise = if (currentIndex != -1 && currentIndex < allExercisesInDay.lastIndex) {
                allExercisesInDay[currentIndex + 1]
            } else if (currentIndex > 0) {
                allExercisesInDay[currentIndex - 1]
            } else null

            if (nextExercise != null) {
                TextButton(
                    onClick = { onToggleSuperset(exercise.id, nextExercise.id) },
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = if (supersetPartner != null) Icons.Default.LinkOff else Icons.Default.Link,
                        contentDescription = null,
                        tint = if (supersetPartner != null) Color(0xFFEF4444) else OrangeAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (supersetPartner != null) "Superset-i Ayır" else "Superset Qoş (${nextExercise.nameAz.take(12)}...)",
                        color = if (supersetPartner != null) Color(0xFFEF4444) else OrangeAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TextButton(
                onClick = onOpen1RmCalculator,
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("1RM Hesabla", color = CyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
