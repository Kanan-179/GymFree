package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.BodyRecoveryAnalysis
import com.example.data.model.MuscleGroup
import com.example.data.model.MuscleRecoveryState
import com.example.data.model.MuscleRecoveryStatus
import com.example.ui.theme.*

/**
 * openGym inspired Anatomical Muscle Recovery and Tissue Fatigue Visualizer.
 * Displays live body recovery score, per-muscle fatigue status, recovery percentage,
 * and scientific recommendations in Azerbaijani.
 */
@Composable
fun MuscleRecoveryView(
    recoveryAnalysis: BodyRecoveryAnalysis,
    onStartWorkoutWithMuscles: (Set<MuscleGroup>) -> Unit,
    onSelectMuscleForCatalog: (MuscleGroup) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<MuscleRecoveryStatus?>(null) }

    val filteredStates = remember(recoveryAnalysis, selectedFilter) {
        if (selectedFilter == null) {
            recoveryAnalysis.muscleStates
        } else {
            recoveryAnalysis.muscleStates.filter { it.status == selectedFilter }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("muscle_recovery_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Overall Body Readiness Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.HealthAndSafety,
                                    contentDescription = null,
                                    tint = LimeSuccess,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "openGym Bərpa Modeli",
                                    color = LimeSuccess,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bədən Hazırlıq İndeksi",
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Circular Readiness Score Gauge
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(DarkSurfaceVariant, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${recoveryAnalysis.overallReadinessScore}%",
                                    color = when {
                                        recoveryAnalysis.overallReadinessScore >= 80 -> LimeSuccess
                                        recoveryAnalysis.overallReadinessScore >= 50 -> OrangeAccent
                                        else -> Color(0xFFEF4444)
                                    },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Bərpa",
                                    color = TextMuted,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val readinessDescription = when {
                        recoveryAnalysis.overallReadinessScore >= 85 ->
                            "Bədəniniz yüksək hazırlıq səviyyəsindədir. Hədəf əzələləri tam güclə məşq etdirmək üçün ən optimal vaxtdır."
                        recoveryAnalysis.overallReadinessScore >= 60 ->
                            "Əzələlərin böyük hissəsi bərpa mərhələsindədir. Hazır olan əzələ qruplarını məşq etdirə bilərsiniz."
                        else ->
                            "Bədən yüksək yorğunluqdadır. Zədələrin qarşısını almaq üçün yüngül bərpa və ya istirahət tövsiyə edilir."
                    }

                    Text(
                        text = readinessDescription,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action: Auto-select recommended fresh muscles
                    if (recoveryAnalysis.recommendedMuscles.isNotEmpty()) {
                        Button(
                            onClick = { onStartWorkoutWithMuscles(recoveryAnalysis.recommendedMuscles.take(2).toSet()) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = CyanOnPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            val freshNames = recoveryAnalysis.recommendedMuscles.take(2).joinToString(" & ") { it.nameAz }
                            Text(
                                text = "Optimal Məşq Seç ($freshNames)",
                                color = CyanOnPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Filter Chips Row
        item {
            Column {
                Text(
                    text = "Əzələ Toxuması Statusu",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { selectedFilter = null },
                        label = { Text("Hamısı (7)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                            selectedLabelColor = CyanPrimary
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == MuscleRecoveryStatus.OPTIMAL,
                        onClick = {
                            selectedFilter = if (selectedFilter == MuscleRecoveryStatus.OPTIMAL) null else MuscleRecoveryStatus.OPTIMAL
                        },
                        label = { Text("Hazır", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LimeSuccess.copy(alpha = 0.2f),
                            selectedLabelColor = LimeSuccess
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == MuscleRecoveryStatus.RECOVERING,
                        onClick = {
                            selectedFilter = if (selectedFilter == MuscleRecoveryStatus.RECOVERING) null else MuscleRecoveryStatus.RECOVERING
                        },
                        label = { Text("Bərpa", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OrangeAccent.copy(alpha = 0.2f),
                            selectedLabelColor = OrangeAccent
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == MuscleRecoveryStatus.DETRAINED,
                        onClick = {
                            selectedFilter = if (selectedFilter == MuscleRecoveryStatus.DETRAINED) null else MuscleRecoveryStatus.DETRAINED
                        },
                        label = { Text("İşlənməmiş", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6366F1).copy(alpha = 0.2f),
                            selectedLabelColor = Color(0xFF818CF8)
                        )
                    )
                }
            }
        }

        // Per-Muscle Recovery Cards
        items(filteredStates) { state ->
            MuscleRecoveryCard(
                state = state,
                onTrainClick = { onStartWorkoutWithMuscles(setOf(state.muscle)) },
                onCatalogClick = { onSelectMuscleForCatalog(state.muscle) }
            )
        }
    }
}

@Composable
private fun MuscleRecoveryCard(
    state: MuscleRecoveryState,
    onTrainClick: () -> Unit,
    onCatalogClick: () -> Unit
) {
    val progressFloat by animateFloatAsState(
        targetValue = state.recoveryPercent / 100f,
        label = "recovery_bar"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("recovery_card_${state.muscle.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Muscle Name + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(state.muscle.color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = state.muscle.nameAz,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${state.lastTrainedDateStr})",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(state.status.badgeColorHex).copy(alpha = 0.18f)
                ) {
                    Text(
                        text = state.status.titleAz,
                        color = Color(state.status.badgeColorHex),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recovery Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { progressFloat },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(state.status.badgeColorHex),
                    trackColor = DarkSurfaceElevated
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${state.recoveryPercent}%",
                    color = Color(state.status.badgeColorHex),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sports Science Recommendation Text
            Text(
                text = state.recommendationAz,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onCatalogClick,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSecondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hərəkətlərə Bax", fontSize = 12.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = onTrainClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (state.status == MuscleRecoveryStatus.OPTIMAL || state.status == MuscleRecoveryStatus.DETRAINED) {
                            CyanPrimary.copy(alpha = 0.2f)
                        } else {
                            DarkSurfaceElevated
                        }
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (state.status == MuscleRecoveryStatus.OPTIMAL || state.status == MuscleRecoveryStatus.DETRAINED) CyanPrimary else TextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Məşqə Sal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.status == MuscleRecoveryStatus.OPTIMAL || state.status == MuscleRecoveryStatus.DETRAINED) CyanPrimary else TextPrimary
                    )
                }
            }
        }
    }
}
