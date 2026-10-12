package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.ExerciseDbLibrary
import com.example.data.model.ExerciseAnimationType
import com.example.data.model.MuscleGroup
import com.example.data.model.UserAvatarConfig
import com.example.ui.theme.*

/**
 * Exercise Visual Player - dedicated solely to Exercise GIF animations.
 * Completely responsive: Uses 1:1 aspect ratio with adaptive constraints,
 * clean high-contrast container, and full-screen zoom capability with smooth scrolling.
 */
@Composable
fun ExerciseAnimationView(
    animationType: ExerciseAnimationType,
    primaryMuscle: MuscleGroup,
    modifier: Modifier = Modifier,
    exerciseName: String = "",
    secondaryMuscles: List<MuscleGroup> = emptyList(),
    customLottieUrl: String? = null,
    customExerciseDbUrl: String? = null,
    avatarConfig: UserAvatarConfig = UserAvatarConfig.DEFAULT,
    onCustomizeAvatarClick: (() -> Unit)? = null,
    description: String? = null,
    executionSteps: List<String> = emptyList(),
    targetTips: String? = null,
    difficulty: String? = null,
    equipment: String? = null
) {
    val exerciseDbInfo = remember(animationType, customExerciseDbUrl) {
        ExerciseDbLibrary.getExerciseDbInfo(animationType, customExerciseDbUrl)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("exercise_animation_container"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header info row: Badge + English/Db name + Target muscle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CyanPrimary.copy(alpha = 0.18f)
                    ) {
                        Text(
                            text = "İcra Qaydası",
                            color = CyanPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = exerciseDbInfo.englishName,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DarkBackground
                ) {
                    Text(
                        text = primaryMuscle.nameAz,
                        color = primaryMuscle.color,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Responsive 1:1 Aspect Ratio GIF Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                ExerciseDbGifPlayer(
                    gifUrl = exerciseDbInfo.gifUrl,
                    fallbackUrl = exerciseDbInfo.fallbackGifUrl,
                    exerciseName = exerciseName.ifBlank { exerciseDbInfo.englishName },
                    targetMuscle = primaryMuscle.nameAz,
                    equipment = equipment ?: exerciseDbInfo.equipment,
                    difficulty = difficulty,
                    description = description,
                    executionSteps = executionSteps,
                    targetTips = targetTips,
                    showFullscreenButton = true,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer info: Equipment & Zoom hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = equipment ?: exerciseDbInfo.equipment,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Böyütmək üçün toxunun",
                        color = CyanPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
