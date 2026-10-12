package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.ConnectedTrainerInfo
import com.example.data.datasource.StudentAssignedDay
import com.example.data.datasource.StudentAssignedExercise
import com.example.ui.theme.*
import com.example.util.QrPayload

@Composable
fun StudentSelfPortalView(
    studentProfile: QrPayload.StudentProfile,
    connectedTrainer: ConnectedTrainerInfo?,
    assignedDays: List<StudentAssignedDay>,
    onOpenStudentQr: () -> Unit,
    onOpenScanTrainerQr: () -> Unit,
    onOpenScanAssignmentQr: () -> Unit,
    onGenerateProgressQr: (Int) -> Unit,
    onDisconnectTrainer: () -> Unit,
    onUpdateExerciseProgress: (Int, String, Float, Boolean) -> Unit,
    onSaveProfile: (String, String, Float, Float, Float, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editingExerciseWeight by remember { mutableStateOf<Pair<Int, StudentAssignedExercise>?>(null) }

    val activeDay = assignedDays.getOrNull(selectedDayIndex) ?: assignedDays.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- 1. STUDENT IDENTITY & CONNECTED COACH HERO CARD ---
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_hero_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Profile Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(OrangeAccent.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = studentProfile.fullName,
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { showEditProfileDialog = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Profili redaktə et",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Məqsəd: ${studentProfile.goal} • Çəki: ${studentProfile.currentWeightKg} kq",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        // My Student QR Code trigger
                        IconButton(
                            onClick = onOpenStudentQr,
                            modifier = Modifier
                                .size(40.dp)
                                .background(DarkSurfaceVariant, CircleShape)
                                .testTag("open_student_qr_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "Tələbə QR Kodum",
                                tint = OrangeAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Connected Trainer status pill
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (connectedTrainer != null) LimeSuccess.copy(alpha = 0.12f) else DarkBackground,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (connectedTrainer != null) LimeSuccess.copy(alpha = 0.4f) else DarkSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (connectedTrainer != null) Icons.Default.Verified else Icons.Default.LinkOff,
                                contentDescription = null,
                                tint = if (connectedTrainer != null) LimeSuccess else TextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                if (connectedTrainer != null) {
                                    Text(
                                        text = "Məşqçim: ${connectedTrainer.trainerName}",
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${connectedTrainer.specialty} • ${connectedTrainer.connectedDate}",
                                        color = LimeSuccess,
                                        fontSize = 11.sp
                                    )
                                } else {
                                    Text(
                                        text = "Məşqçiyə qoşulmayıb",
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Müəllimin QR kodunu oxudaraq əlaqələnin",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if (connectedTrainer != null) {
                                TextButton(
                                    onClick = onDisconnectTrainer,
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text("Ləğv et", color = Color(0xFFFF5252), fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons for Student
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenScanAssignmentQr,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("scan_task_qr_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = CyanOnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tapşırıq Qəbul Et",
                                color = CyanOnPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (connectedTrainer == null) {
                            OutlinedButton(
                                onClick = onOpenScanTrainerQr,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(CyanPrimary)
                                )
                            ) {
                                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Müəllim QR Oxut", fontSize = 12.sp, color = CyanPrimary, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            OutlinedButton(
                                onClick = onOpenStudentQr,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(DarkSurfaceVariant)
                                )
                            ) {
                                Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Tələbə QR-ım", fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }

        // --- 2. ASSIGNED WORKOUT DAYS SELECTOR ---
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Müəllimin Təyin Etdiyi Cədvəl:",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${assignedDays.size} Məşq Günü",
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(assignedDays.size) { idx ->
                        val day = assignedDays[idx]
                        val isSelected = idx == selectedDayIndex
                        val completedExercises = day.exercises.count { it.isCompleted }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) CyanPrimary else DarkSurface,
                            border = if (!isSelected && completedExercises > 0) androidx.compose.foundation.BorderStroke(1.dp, LimeSuccess.copy(alpha = 0.5f)) else null,
                            modifier = Modifier
                                .clickable { selectedDayIndex = idx }
                                .testTag("student_day_tab_$idx")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${day.dayOfWeek}-ci Gün",
                                    color = if (isSelected) CyanOnPrimary else TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = day.dayTitle.substringBefore(":"),
                                    color = if (isSelected) CyanOnPrimary else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) DarkBackground.copy(alpha = 0.3f) else DarkSurfaceVariant
                                ) {
                                    Text(
                                        text = "$completedExercises / ${day.exercises.size} icra",
                                        color = if (isSelected) CyanOnPrimary else LimeSuccess,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. EXERCISE CARDS FOR ACTIVE DAY ---
        if (activeDay != null && activeDay.exercises.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = activeDay.dayTitle,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Təlimatçı: ${activeDay.trainerName}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    // Generate Progress QR for Trainer
                    FilledTonalButton(
                        onClick = { onGenerateProgressQr(activeDay.dayOfWeek) },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = LimeSuccess.copy(alpha = 0.18f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("send_progress_qr_button")
                    ) {
                        Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = LimeSuccess, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Müəllimə Göndər", color = LimeSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(activeDay.exercises) { ex ->
                StudentAssignedExerciseCard(
                    exercise = ex,
                    onToggleComplete = {
                        onUpdateExerciseProgress(
                            activeDay.dayOfWeek,
                            ex.id,
                            ex.actualWeightKg,
                            !ex.isCompleted
                        )
                    },
                    onEditWeight = {
                        editingExerciseWeight = activeDay.dayOfWeek to ex
                    }
                )
            }
        } else {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Hələ məşq tapşırığı yoxdur",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Müəlliminizin sizə verdiyi tapşırıq QR kodunu oxudaraq cədvəlinizi aktivləşdirin.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onOpenScanAssignmentQr,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = CyanOnPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("QR Oxut & Tapşırığı Yüklə", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Modal: Edit Actual Weight Dialog
    editingExerciseWeight?.let { (dayNum, ex) ->
        var currentWeightStr by remember { mutableStateOf(ex.actualWeightKg.toString()) }
        var isDone by remember { mutableStateOf(ex.isCompleted) }

        AlertDialog(
            onDismissRequest = { editingExerciseWeight = null },
            title = {
                Text(text = "Qaldırdığınız Çəki (${ex.exerciseName})", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(
                        text = "Müəllimin təyin etdiyi hədəf: ${ex.targetWeightKg} kq (${ex.targetSets} set x ${ex.targetReps} təkrar)",
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = currentWeightStr,
                        onValueChange = { currentWeightStr = it },
                        label = { Text("Faktiki Çəkiniz (kq)", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isDone,
                            onCheckedChange = { isDone = it },
                            colors = CheckboxDefaults.colors(checkedColor = LimeSuccess)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hərəkət tamamlandı kimi qeyd edilsin",
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = currentWeightStr.toFloatOrNull() ?: ex.actualWeightKg
                        onUpdateExerciseProgress(dayNum, ex.id, w, isDone)
                        editingExerciseWeight = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Yadda Saxla", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingExerciseWeight = null }) {
                    Text("İmtina", color = TextMuted)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Modal: Edit Student Profile Dialog
    if (showEditProfileDialog) {
        var name by remember { mutableStateOf(studentProfile.fullName) }
        var phone by remember { mutableStateOf(studentProfile.phone) }
        var curW by remember { mutableStateOf(studentProfile.currentWeightKg.toString()) }
        var tarW by remember { mutableStateOf(studentProfile.targetWeightKg.toString()) }
        var height by remember { mutableStateOf(studentProfile.heightCm.toString()) }
        var goal by remember { mutableStateOf(studentProfile.goal) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text("Tələbə Profilini Redaktə Et", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Ad & Soyad", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Əlaqə Nömrəsi", color = TextMuted) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = curW,
                            onValueChange = { curW = it },
                            label = { Text("Çəki (kq)", color = TextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = DarkSurfaceVariant,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        OutlinedTextField(
                            value = tarW,
                            onValueChange = { tarW = it },
                            label = { Text("Hədəf (kq)", color = TextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = DarkSurfaceVariant,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    OutlinedTextField(
                        value = goal,
                        onValueChange = { goal = it },
                        label = { Text("Məqsəd", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cW = curW.toFloatOrNull() ?: studentProfile.currentWeightKg
                        val tW = tarW.toFloatOrNull() ?: studentProfile.targetWeightKg
                        val h = height.toFloatOrNull() ?: studentProfile.heightCm
                        onSaveProfile(name.ifBlank { "Tələbə" }, phone, cW, tW, h, goal.ifBlank { "Əzələ Kütləsi" })
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Yadda Saxla", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("İmtina", color = TextMuted)
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
private fun StudentAssignedExerciseCard(
    exercise: StudentAssignedExercise,
    onToggleComplete: () -> Unit,
    onEditWeight: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = if (exercise.isCompleted) androidx.compose.foundation.BorderStroke(1.dp, LimeSuccess.copy(alpha = 0.4f)) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Done checkbox button
                IconButton(
                    onClick = onToggleComplete,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (exercise.isCompleted) LimeSuccess else DarkSurfaceVariant,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (exercise.isCompleted) Icons.Default.Check else Icons.Outlined.CheckCircle,
                        contentDescription = "Tamamla",
                        tint = if (exercise.isCompleted) Color.Black else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.exerciseName,
                        color = if (exercise.isCompleted) TextMuted else TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = OrangeAccent.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = exercise.muscleGroup,
                                color = OrangeAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${exercise.targetSets} set x ${exercise.targetReps} təkrar",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Actual Weight Pill Button (Tappable to edit)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onEditWeight() }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Hədəf: ${exercise.targetWeightKg}kq", color = TextMuted, fontSize = 9.sp)
                        Text(
                            text = "${exercise.actualWeightKg} kq",
                            color = CyanPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Trainer's specific instructions/notes
            if (exercise.trainerNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Məşqçi qeydi: ${exercise.trainerNotes}",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}
