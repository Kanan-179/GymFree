package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.data.model.UserAvatarConfig
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailSheet(
    exercise: Exercise,
    avatarConfig: UserAvatarConfig = UserAvatarConfig.DEFAULT,
    onCustomizeAvatar: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onOpenTimer: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = DarkSurface,
        modifier = Modifier.testTag("exercise_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Title & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.nameAz,
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = exercise.primaryMuscle.color.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Əsas: ${exercise.primaryMuscle.nameAz}",
                                color = exercise.primaryMuscle.color,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        exercise.secondaryMuscles.forEach { sec ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = sec.color.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = sec.nameAz,
                                    color = sec.color,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Bağla", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Visual Exercise Player
            ExerciseAnimationView(
                animationType = exercise.animationType,
                primaryMuscle = exercise.primaryMuscle,
                exerciseName = exercise.nameAz,
                secondaryMuscles = exercise.secondaryMuscles,
                customLottieUrl = exercise.lottieUrl,
                customExerciseDbUrl = exercise.exerciseDbGifUrl,
                avatarConfig = avatarConfig,
                onCustomizeAvatarClick = onCustomizeAvatar,
                description = exercise.descriptionAz,
                executionSteps = exercise.executionStepsAz,
                targetTips = exercise.targetTipsAz,
                difficulty = exercise.difficulty,
                equipment = exercise.equipment
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickStatCard(
                    title = "Tövsiyə Set",
                    value = "${exercise.recommendedSets} Set",
                    icon = Icons.Default.FitnessCenter,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Təkrarlar",
                    value = exercise.recommendedReps,
                    icon = Icons.Default.Repeat,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "İstirahət",
                    value = "${exercise.restSeconds} san",
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action: Start Rest Timer
            Button(
                onClick = onOpenTimer,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start_rest_timer_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = CyanOnPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${exercise.restSeconds} Saniyəlik İstirahət Taymerini Aç",
                    color = CyanOnPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Purpose & Biomechanics
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hansı Əzələyə Təsir Edir?", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(exercise.descriptionAz, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step by Step Execution
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LimeSuccess, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Düzgün İcra Addımları", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    exercise.executionStepsAz.forEachIndexed { index, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(CyanPrimary.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = CyanPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pro Tip
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OrangeAccent.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Maksimum Əzələ Hissi Üçün", color = OrangeAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(exercise.targetTipsAz, color = TextPrimary, fontSize = 13.sp, lineHeight = 19.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Common Mistakes
            if (exercise.commonMistakesAz.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MuscleChest, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Qaçınılmalı Səhvlər", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        exercise.commonMistakesAz.forEach { mistake ->
                            Row(modifier = Modifier.padding(vertical = 3.dp)) {
                                Text("• ", color = MuscleChest, fontWeight = FontWeight.Bold)
                                Text(mistake, color = TextSecondary, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = TextMuted, fontSize = 11.sp)
            Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
