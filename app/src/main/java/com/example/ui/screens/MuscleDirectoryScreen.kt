package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.datasource.FitnessData
import com.example.data.model.BodyRecoveryAnalysis
import com.example.data.model.Exercise
import com.example.data.model.MuscleGroup
import com.example.ui.components.MuscleGroupSelectorRow
import com.example.ui.components.MuscleRecoveryView
import com.example.ui.theme.*

@Composable
fun MuscleDirectoryScreen(
    selectedMuscle: MuscleGroup,
    onSelectMuscle: (MuscleGroup) -> Unit,
    onInspectExercise: (Exercise) -> Unit,
    recoveryAnalysis: BodyRecoveryAnalysis? = null,
    onStartWorkoutWithMuscles: ((Set<MuscleGroup>) -> Unit)? = null
) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Kataloq, 1: openGym Bərpa
    var searchQuery by remember { mutableStateOf("") }

    val exercisesForMuscle = remember(selectedMuscle, searchQuery) {
        val list = FitnessData.getExercisesForMuscle(selectedMuscle)
        if (searchQuery.isBlank()) list
        else list.filter { it.nameAz.contains(searchQuery, ignoreCase = true) || it.equipment.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // openGym Inspired Mode Switcher (Kataloq vs Bədən Bərpası)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = { subTab = 0 },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (subTab == 0) CyanPrimary else DarkSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (subTab == 0) Color.Black else TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hərəkət Kataloqu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (subTab == 0) Color.Black else TextPrimary
                    )
                }

                FilledTonalButton(
                    onClick = { subTab = 1 },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (subTab == 1) OrangeAccent else DarkSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.HealthAndSafety,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (subTab == 1) Color.Black else TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "openGym Bərpa",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (subTab == 1) Color.Black else TextPrimary
                    )
                }
            }
        }

        if (subTab == 1 && recoveryAnalysis != null) {
            MuscleRecoveryView(
                recoveryAnalysis = recoveryAnalysis,
                onStartWorkoutWithMuscles = { muscles ->
                    onStartWorkoutWithMuscles?.invoke(muscles)
                },
                onSelectMuscleForCatalog = { muscle ->
                    onSelectMuscle(muscle)
                    subTab = 0
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("muscle_directory_screen"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Header
                item {
                    Column {
                        Text(
                            text = "Əzələ Qrupları & Hərəkətlər",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "İstədiyiniz əzələni seçin və onu ən çox inkişaf etdirən hərəkətləri animasiyalı şəkildə öyrənin.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

        // Muscle Selection Row (Sinə, Çiyin, Kürək, Biceps, Triceps, Abs, Ayaq)
        item {
            MuscleGroupSelectorRow(
                selectedMuscle = selectedMuscle,
                onSelectMuscle = onSelectMuscle
            )
        }

        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("muscle_search_input"),
                placeholder = { Text("Hərəkət və ya alət axtar...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyanPrimary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Təmizlə", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedBorderColor = CyanPrimary,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
        }

        // Muscle Details Card (Anatomy, Function, Growth Secret)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("muscle_info_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(selectedMuscle.color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = selectedMuscle.nameAz,
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = selectedMuscle.color.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${exercisesForMuscle.size} Hərəkət",
                                color = selectedMuscle.color,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedMuscle.anatomicalName,
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = selectedMuscle.descriptionAz,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Growth Tips Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurface
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "İnkişafın Qızıl Qaydası:",
                                    color = OrangeAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = selectedMuscle.growthTipsAz,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Exercises List Section Header
        item {
            Text(
                text = "${selectedMuscle.nameAz} Əzələsi Üçün Hərəkətlər",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Exercises Cards
        items(exercisesForMuscle) { exercise ->
            val isPrimary = exercise.primaryMuscle == selectedMuscle

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInspectExercise(exercise) }
                    .testTag("muscle_exercise_${exercise.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(selectedMuscle.color.copy(alpha = 0.18f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = selectedMuscle.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercise.nameAz,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isPrimary) selectedMuscle.color.copy(alpha = 0.25f) else CyanContainer.copy(alpha = 0.4f)
                                ) {
                                    Text(
                                        text = if (isPrimary) "Əsas Hədəf" else "Köməkçi",
                                        color = if (isPrimary) selectedMuscle.color else CyanOnContainer,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = "• ${exercise.equipment}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        FilledTonalIconButton(
                            onClick = { onInspectExercise(exercise) },
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "Animasiyaya Bax",
                                tint = OrangeAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = exercise.descriptionAz,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tövsiyə: ${exercise.recommendedSets} set x ${exercise.recommendedReps}",
                            color = CyanPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        TextButton(
                            onClick = { onInspectExercise(exercise) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Animasiya & Qayda", color = OrangeAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
}
}



