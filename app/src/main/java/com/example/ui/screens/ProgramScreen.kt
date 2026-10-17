package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.FitnessData
import com.example.data.datasource.RecommendedPlan
import com.example.data.model.Exercise
import com.example.data.model.MuscleGroup
import com.example.data.model.SetType
import com.example.data.model.WorkoutDay
import com.example.ui.components.OpenGymSetLoggingTable
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveWorkoutSession

enum class ProgramDisplayMode {
    TODAY_RECOMMENDED,
    THREE_DAY_PROGRAM
}

@Composable
fun ProgramScreen(
    selectedDayNumber: Int,
    todayMuscles: Set<MuscleGroup>,
    recommendedPlan: RecommendedPlan,
    activeSession: ActiveWorkoutSession?,
    completedBannerMessage: String?,
    onSelectDay: (Int) -> Unit,
    onOpenTodayMuscleSelector: () -> Unit,
    onInspectExercise: (Exercise) -> Unit,
    onStartWorkout: (WorkoutDay) -> Unit,
    onStartRecommendedWorkout: (RecommendedPlan) -> Unit,
    onFinishWorkout: () -> Unit,
    onCancelWorkout: () -> Unit,
    onToggleSet: (String, Int, Int) -> Unit,
    onDismissBanner: () -> Unit,
    onOpenTimer: () -> Unit,
    onUpdateSetLog: ((String, Int, Float, Int, SetType) -> Unit)? = null,
    onToggleSetLoggedDone: ((String, Int, Exercise) -> Unit)? = null,
    onToggleSuperset: ((String, String) -> Unit)? = null,
    onOpen1RmCalculator: (() -> Unit)? = null
) {
    var displayMode by remember { mutableStateOf(ProgramDisplayMode.TODAY_RECOMMENDED) }

    val currentDay = FitnessData.threeDayProgram.firstOrNull { it.dayNumber == selectedDayNumber }
        ?: FitnessData.threeDayProgram.first()

    val isCurrentDayActive = activeSession?.dayNumber == currentDay.dayNumber && displayMode == ProgramDisplayMode.THREE_DAY_PROGRAM
    val isRecommendedSessionActive = activeSession?.dayNumber == 0 && displayMode == ProgramDisplayMode.TODAY_RECOMMENDED

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("program_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Celebration Banner if workout just saved
        if (completedBannerMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LimeSuccess.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Celebration, contentDescription = null, tint = LimeSuccess)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = completedBannerMessage,
                            color = LimeSuccess,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDismissBanner) {
                            Icon(Icons.Default.Close, contentDescription = "Bağla", tint = LimeSuccess)
                        }
                    }
                }
            }
        }

        // Hero Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(CyanContainer.copy(alpha = 0.35f), DarkSurfaceVariant)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = LimeSuccess.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LimeSuccess, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ağıllı Məşq Tövsiyə Sistemi", color = LimeSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            FilledTonalIconButton(
                                onClick = onOpenTimer,
                                modifier = Modifier.size(36.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = "İstirahət Taymeri", tint = CyanPrimary, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Məşq Proqramı & Tövsiyələr",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Bu gün məşq edəcəyiniz əzələləri seçin və sistem sizə ən effektiv hərəkətləri anatomik animasiyalarla təqdim etsin.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Mode Switcher Tabs: [🎯 Bu Günün Hədəfi (Tövsiyələr)] / [📅 3 Günlük Standart Plan]
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceVariant, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { displayMode = ProgramDisplayMode.TODAY_RECOMMENDED }
                        .testTag("tab_today_recommended"),
                    shape = RoundedCornerShape(11.dp),
                    color = if (displayMode == ProgramDisplayMode.TODAY_RECOMMENDED) CyanPrimary else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = if (displayMode == ProgramDisplayMode.TODAY_RECOMMENDED) Color.Black else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Günün Tövsiyəsi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (displayMode == ProgramDisplayMode.TODAY_RECOMMENDED) Color.Black else TextSecondary
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { displayMode = ProgramDisplayMode.THREE_DAY_PROGRAM }
                        .testTag("tab_three_day_program"),
                    shape = RoundedCornerShape(11.dp),
                    color = if (displayMode == ProgramDisplayMode.THREE_DAY_PROGRAM) CyanPrimary else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = if (displayMode == ProgramDisplayMode.THREE_DAY_PROGRAM) Color.Black else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "3 Günlük Plan",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (displayMode == ProgramDisplayMode.THREE_DAY_PROGRAM) Color.Black else TextSecondary
                        )
                    }
                }
            }
        }

        if (displayMode == ProgramDisplayMode.TODAY_RECOMMENDED) {
            // Target Muscles Banner with "Əzələləri Dəyiş" Button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Bugünkü Əzələ Hədəfiniz",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Text(
                                    text = recommendedPlan.titleAz,
                                    fontSize = 13.sp,
                                    color = CyanPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = onOpenTodayMuscleSelector,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanPrimary.copy(alpha = 0.18f),
                                    contentColor = CyanPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("change_today_muscles_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Dəyişdir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Selected Muscle Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            todayMuscles.forEach { muscle ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = muscle.color.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, muscle.color.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(muscle.color, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = muscle.nameAz,
                                            color = muscle.color,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Rationale summary
                        Text(
                            text = recommendedPlan.subtitleAz,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Start / Finish Workout Actions for Recommended Plan
                        if (!isRecommendedSessionActive) {
                            Button(
                                onClick = { onStartRecommendedWorkout(recommendedPlan) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("start_recommended_workout_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Bu Məşqə Başla (${recommendedPlan.exercises.size} hərəkət)", color = Color.Black, fontWeight = FontWeight.Black)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onCancelWorkout,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                                ) {
                                    Text("Ləğv et")
                                }

                                Button(
                                    onClick = onFinishWorkout,
                                    modifier = Modifier
                                        .weight(2f)
                                        .testTag("finish_recommended_workout_btn"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = LimeSuccess)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Məşqi Tamamla", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }

                            // openGym Live Session Volume & 1RM Bar
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DarkSurfaceElevated,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = LimeSuccess, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Canlı Tonaj: ${activeSession?.totalVolumeKg?.toInt() ?: 0} kq",
                                            color = LimeSuccess,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        onOpen1RmCalculator?.let { open1Rm ->
                                            TextButton(
                                                onClick = open1Rm,
                                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Icon(Icons.Default.Calculate, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("1RM", color = OrangeAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        TextButton(
                                            onClick = onOpenTimer,
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(Icons.Default.Timer, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Taymer", color = CyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Elmi Sıralanmış Tövsiyələr (${recommendedPlan.exercises.size})",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "~${recommendedPlan.totalEstimatedMinutes} dəq",
                        color = OrangeAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Recommended Exercise List
            itemsIndexed(recommendedPlan.exercises) { index, recEx ->
                val exercise = recEx.exercise
                val completedSets = activeSession?.completedSets?.get(exercise.id) ?: 0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onInspectExercise(exercise) }
                        .testTag("rec_exercise_card_${exercise.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    border = BorderStroke(1.dp, DarkBorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Step / Order badge
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(CyanPrimary.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = CyanPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
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

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = exercise.primaryMuscle.color.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = exercise.primaryMuscle.nameAz,
                                            color = exercise.primaryMuscle.color,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Text(
                                        text = "${exercise.recommendedSets}x ${exercise.recommendedReps}",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Play Animation Button
                            FilledTonalIconButton(
                                onClick = { onInspectExercise(exercise) },
                                modifier = Modifier.size(38.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = "Animasiyaya Bax",
                                    tint = OrangeAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Scientific Role & Rationale Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkBackground,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = recEx.roleBadge,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanPrimary
                                )
                                Text(
                                    text = recEx.recommendationReasonAz,
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        // Interactive Live Set Checkers when recommended session is active
                        if (isRecommendedSessionActive && activeSession != null) {
                            OpenGymSetLoggingTable(
                                exercise = exercise,
                                activeSession = activeSession,
                                allExercisesInDay = recommendedPlan.exercises.map { it.exercise },
                                onUpdateSetLog = { exId, setIdx, w, r, type ->
                                    onUpdateSetLog?.invoke(exId, setIdx, w, r, type)
                                },
                                onToggleSetDone = { exId, setIdx, ex ->
                                    if (onToggleSetLoggedDone != null) {
                                        onToggleSetLoggedDone(exId, setIdx, ex)
                                    } else {
                                        onToggleSet(exId, setIdx, ex.recommendedSets)
                                    }
                                },
                                onToggleSuperset = { a, b ->
                                    onToggleSuperset?.invoke(a, b)
                                },
                                onOpen1RmCalculator = {
                                    onOpen1RmCalculator?.invoke()
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // Day Navigation Tabs (Gün 1, Gün 2, Gün 3)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FitnessData.threeDayProgram.forEach { day ->
                        val isSelected = day.dayNumber == selectedDayNumber
                        val isSessionActive = activeSession?.dayNumber == day.dayNumber

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSelectDay(day.dayNumber) }
                                .testTag("day_tab_${day.dayNumber}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) CyanPrimary else DarkSurfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (isSessionActive) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(OrangeAccent, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                                Text(
                                    text = "GÜN ${day.dayNumber}",
                                    color = if (isSelected) Color.Black else TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = when(day.dayNumber) {
                                        1 -> "İtələmə"
                                        2 -> "Çəkmə"
                                        else -> "Ayaq & Abs"
                                    },
                                    color = if (isSelected) Color.Black else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

        // Current Day Target Muscles & Start Button
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = currentDay.nameAz,
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentDay.targetMusclesSummaryAz,
                                color = CyanPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AccessTime, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("~${currentDay.estimatedMinutes} dəq", color = TextPrimary, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Muscle pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        currentDay.targetMuscles.forEach { muscle ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = muscle.color.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = muscle.nameAz,
                                    color = muscle.color,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Start / Finish Workout Actions
                    if (!isCurrentDayActive) {
                        Button(
                            onClick = { onStartWorkout(currentDay) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_workout_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = CyanOnPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Bu Günün Məşqinə Başla", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onCancelWorkout,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                            ) {
                                Text("Ləğv et")
                            }

                            Button(
                                onClick = onFinishWorkout,
                                modifier = Modifier
                                    .weight(2f)
                                    .testTag("finish_workout_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LimeSuccess)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Məşqi Tamamla", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }

                        // openGym Live Session Volume & 1RM Bar for 3-Day Program
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceElevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = LimeSuccess, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Canlı Tonaj: ${activeSession?.totalVolumeKg?.toInt() ?: 0} kq",
                                        color = LimeSuccess,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    onOpen1RmCalculator?.let { open1Rm ->
                                        TextButton(
                                            onClick = open1Rm,
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(Icons.Default.Calculate, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("1RM", color = OrangeAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    TextButton(
                                        onClick = onOpenTimer,
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.Timer, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Taymer", color = CyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "${currentDay.exercises.size} Hərəkət Planı",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Exercise Cards
        itemsIndexed(currentDay.exercises) { index, exercise ->
            val completedSets = activeSession?.completedSets?.get(exercise.id) ?: 0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInspectExercise(exercise) }
                    .testTag("exercise_card_${exercise.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Exercise Number Badge
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(CyanPrimary.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = CyanPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
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
                                    color = exercise.primaryMuscle.color.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Hədəf: ${exercise.primaryMuscle.nameAz}",
                                        color = exercise.primaryMuscle.color,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = "${exercise.recommendedSets}x ${exercise.recommendedReps}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Play Animation Indicator Icon
                        FilledTonalIconButton(
                            onClick = { onInspectExercise(exercise) },
                            modifier = Modifier.size(38.dp),
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

                    // Equipment & Focus line
                    Text(
                        text = "Alət: ${exercise.equipment} • İstirahət: ${exercise.restSeconds} san",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    // Interactive Live Set Checkers when workout is active
                    if (isCurrentDayActive && activeSession != null) {
                        OpenGymSetLoggingTable(
                            exercise = exercise,
                            activeSession = activeSession,
                            allExercisesInDay = currentDay.exercises,
                            onUpdateSetLog = { exId, setIdx, w, r, type ->
                                onUpdateSetLog?.invoke(exId, setIdx, w, r, type)
                            },
                            onToggleSetDone = { exId, setIdx, ex ->
                                if (onToggleSetLoggedDone != null) {
                                    onToggleSetLoggedDone(exId, setIdx, ex)
                                } else {
                                    onToggleSet(exId, setIdx, ex.recommendedSets)
                                }
                            },
                            onToggleSuperset = { a, b ->
                                onToggleSuperset?.invoke(a, b)
                            },
                            onOpen1RmCalculator = {
                                onOpen1RmCalculator?.invoke()
                            }
                        )
                    }
                }
            }
        }
    }
}
}
