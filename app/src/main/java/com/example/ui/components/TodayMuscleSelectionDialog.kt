package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.datasource.WorkoutPreset
import com.example.data.datasource.WorkoutRecommendationEngine
import com.example.data.model.MuscleGroup
import com.example.ui.theme.*

@Composable
fun TodayMuscleSelectionDialog(
    initialSelectedMuscles: Set<MuscleGroup>,
    onDismiss: () -> Unit,
    onConfirm: (Set<MuscleGroup>) -> Unit
) {
    var selectedMuscles by remember {
        mutableStateOf(
            if (initialSelectedMuscles.isNotEmpty()) initialSelectedMuscles else setOf(MuscleGroup.SINE, MuscleGroup.TRICEPS)
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .testTag("today_muscle_selection_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(1.5.dp, DarkBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header with Icon & Question
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CyanPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bugünkü Məşq Hədəfiniz",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Text(
                            text = "Bu gün hansı əzələləri məşq edəcəksiniz?",
                            fontSize = 12.sp,
                            color = CyanPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Bir və ya bir neçə əzələ seçin. Sistem sizə elmi prinsiplərə uyğun sıralanmış hərəkətlər tövsiyə edəcək.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Split Presets
                    Text(
                        text = "SÜRƏTLİ HAZIR SPLİTLƏR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WorkoutRecommendationEngine.quickPresets.forEach { preset ->
                            val isPresetSelected = selectedMuscles == preset.muscles
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isPresetSelected) CyanPrimary.copy(alpha = 0.2f) else DarkSurfaceElevated,
                                border = BorderStroke(
                                    1.dp,
                                    if (isPresetSelected) CyanPrimary else DarkBorderSubtle
                                ),
                                modifier = Modifier
                                    .clickable { selectedMuscles = preset.muscles }
                                    .testTag("preset_${preset.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = preset.icon, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = preset.nameAz,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPresetSelected) CyanPrimary else TextPrimary
                                        )
                                        Text(
                                            text = preset.descriptionAz,
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Individual Muscle Multi-Selection
                    Text(
                        text = "VƏ YA FƏRDİ ƏZƏLƏLƏRİ SEÇİN (${selectedMuscles.size} seçilib)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    MuscleGroup.entries.forEach { muscle ->
                        val isSelected = selectedMuscles.contains(muscle)
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) muscle.color.copy(alpha = 0.16f) else DarkSurfaceElevated,
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) muscle.color else DarkBorderSubtle
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedMuscles = if (isSelected) {
                                        if (selectedMuscles.size > 1) selectedMuscles - muscle else selectedMuscles
                                    } else {
                                        selectedMuscles + muscle
                                    }
                                }
                                .testTag("muscle_option_${muscle.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(muscle.color, CircleShape)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = muscle.nameAz,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TextPrimary else TextSecondary
                                    )
                                    Text(
                                        text = muscle.anatomicalName,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(muscle.color),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Scientific rationale helper
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkBackground,
                        border = BorderStroke(1.dp, DarkBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CyanPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Elmi qanun: Böyük əzələ qrupları (Sinə, Kürək, Ayaq) öncə, kiçik köməkçi əzələlər (Çiyin, Qollar, Qarın) isə sonra məşq edildikdə maksimum güc və inkişaf əldə olunur.",
                                fontSize = 11.sp,
                                color = TextMuted,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("dialog_cancel_btn"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text("Sonra Seçərəm", fontSize = 12.sp, color = TextMuted)
                    }

                    Button(
                        onClick = {
                            onConfirm(selectedMuscles)
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("dialog_confirm_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanPrimary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Hərəkətləri Göstər 🔥",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}
