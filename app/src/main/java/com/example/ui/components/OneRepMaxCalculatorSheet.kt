package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.OneRepMaxUtils

/**
 * openGym inspired 1RM (One Rep Max) Strength Calculator Bottom Sheet.
 * Provides interactive weight and rep inputs, calculation of 1RM with Epley and Brzycki
 * formulas, and breakdown of percentages for hypertrophy and strength goals.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRepMaxCalculatorSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var weightKg by remember { mutableFloatStateOf(80f) }
    var reps by remember { mutableIntStateOf(8) }

    val estimate = remember(weightKg, reps) {
        OneRepMaxUtils.calculate1Rm(weightKg, reps)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextSecondary) },
        modifier = modifier.testTag("one_rep_max_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "openGym 1RM Hesablayıcı",
                                color = OrangeAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Maksimal Güc & Yük Cədvəli",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Bağla", tint = TextSecondary)
                    }
                }
            }

            // Quick Preset Exercises Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Sinə Presi" to 80f,
                        "Skvat" to 100f,
                        "Dedlift" to 120f,
                        "Çiyin Presi" to 50f
                    ).forEach { (name, presetWeight) ->
                        FilledTonalButton(
                            onClick = {
                                weightKg = presetWeight
                                reps = 8
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (weightKg == presetWeight) CyanPrimary.copy(alpha = 0.2f) else DarkSurfaceVariant
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (weightKg == presetWeight) CyanPrimary else TextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Input Weight & Reps Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Weight Stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Qaldırılan Çəki", color = TextSecondary, fontSize = 13.sp)
                                Text("${weightKg.toInt()} kq", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilledTonalIconButton(
                                    onClick = { if (weightKg > 5f) weightKg -= 2.5f },
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                                ) {
                                    Text("-2.5", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                FilledTonalIconButton(
                                    onClick = { weightKg += 2.5f },
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                                ) {
                                    Text("+2.5", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                FilledTonalIconButton(
                                    onClick = { weightKg += 5f },
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = CyanPrimary.copy(alpha = 0.2f))
                                ) {
                                    Text("+5", color = CyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Divider(color = DarkSurfaceElevated, thickness = 1.dp)

                        // Reps Stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("İcra Edilən Təkrar", color = TextSecondary, fontSize = 13.sp)
                                Text("$reps təkrar", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black)
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledTonalIconButton(
                                    onClick = { if (reps > 1) reps -= 1 },
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "-1", tint = TextPrimary)
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DarkSurfaceElevated
                                ) {
                                    Text(
                                        text = "$reps",
                                        color = CyanPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                                FilledTonalIconButton(
                                    onClick = { if (reps < 30) reps += 1 },
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = DarkSurfaceElevated)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "+1", tint = TextPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // 1RM Result Hero Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CyanPrimary.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Təxmini 1RM (1 Təkrar Maksimum)",
                                color = CyanPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${estimate.finalEstimated1Rm} kq",
                                color = TextPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Epley: ${estimate.estimated1RmEpley} kq • Brzycki: ${estimate.estimated1RmBrzycki} kq",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(CyanPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color.Black)
                        }
                    }
                }
            }

            // Training Load Breakdown Header
            item {
                Text(
                    text = "Məşq Hədəflərinə Görə Faiz Bölgüsü",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Percent Breakdown Items
            items(estimate.percentBreakdown) { row ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when {
                                    row.percent >= 90 -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                    row.percent >= 80 -> OrangeAccent.copy(alpha = 0.2f)
                                    row.percent >= 70 -> LimeSuccess.copy(alpha = 0.2f)
                                    else -> CyanPrimary.copy(alpha = 0.2f)
                                }
                            ) {
                                Text(
                                    text = "${row.percent}%",
                                    color = when {
                                        row.percent >= 90 -> Color(0xFFEF4444)
                                        row.percent >= 80 -> OrangeAccent
                                        row.percent >= 70 -> LimeSuccess
                                        else -> CyanPrimary
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${row.weightKg} kq",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = row.goalTitleAz,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceElevated
                        ) {
                            Text(
                                text = row.repsTarget,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
