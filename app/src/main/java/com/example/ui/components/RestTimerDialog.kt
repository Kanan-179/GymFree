package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun RestTimerDialog(
    initialSeconds: Int = 60,
    onDismiss: () -> Unit
) {
    var totalSeconds by remember { mutableIntStateOf(initialSeconds) }
    var remainingSeconds by remember { mutableIntStateOf(initialSeconds) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        }
    }

    val progress = if (totalSeconds > 0) {
        remainingSeconds.toFloat() / totalSeconds.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "timer_progress"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("rest_timer_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Set Arası İstirahət",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Bağla", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Circular Progress
                Box(
                    modifier = Modifier.size(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 12.dp.toPx()
                        // Track
                        drawCircle(
                            color = DarkSurfaceVariant,
                            radius = (size.minDimension - strokeW) / 2f,
                            style = Stroke(width = strokeW)
                        )
                        // Arc
                        val sweep = animatedProgress * 360f
                        drawArc(
                            color = if (remainingSeconds <= 5) LimeSuccess else CyanPrimary,
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = Offset(strokeW / 2f, strokeW / 2f),
                            size = Size(size.width - strokeW, size.height - strokeW),
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val minutes = remainingSeconds / 60
                        val seconds = remainingSeconds % 60
                        val timeStr = String.format("%02d:%02d", minutes, seconds)

                        Text(
                            text = timeStr,
                            color = TextPrimary,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = if (remainingSeconds == 0) "Vaxt Bitdi! Növbəti Setə Başla!" else "Nəfəsi Dərinləşdir",
                            color = if (remainingSeconds == 0) LimeSuccess else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Time adjustment buttons (+15s, -15s)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = {
                            if (remainingSeconds > 15) {
                                remainingSeconds -= 15
                                totalSeconds = maxOf(totalSeconds - 15, remainingSeconds)
                            }
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DarkSurfaceVariant)
                    ) {
                        Text("-15 san", color = TextPrimary)
                    }

                    FilledTonalIconButton(
                        onClick = { isRunning = !isRunning },
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = CyanPrimary)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pauza" else "Davam et",
                            tint = CyanOnPrimary
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            remainingSeconds += 15
                            totalSeconds += 15
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DarkSurfaceVariant)
                    ) {
                        Text("+15 san", color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Preset Chips (45s, 60s, 90s, 120s)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(45, 60, 90, 120).forEach { sec ->
                        val isSelected = totalSeconds == sec
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                totalSeconds = sec
                                remainingSeconds = sec
                                isRunning = true
                            },
                            label = { Text("${sec}s", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                                selectedLabelColor = CyanPrimary,
                                containerColor = DarkSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_timer_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = if (remainingSeconds == 0) LimeSuccess else CyanPrimary)
                ) {
                    Text(
                        text = if (remainingSeconds == 0) "Məşqə Qayıt" else "Taymeri Keç",
                        color = if (remainingSeconds == 0) Color.Black else CyanOnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
