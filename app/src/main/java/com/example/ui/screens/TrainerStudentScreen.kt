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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.AppUserRole
import com.example.data.datasource.ConnectedTrainerInfo
import com.example.data.datasource.FitnessData
import com.example.data.datasource.StudentAssignedDay
import com.example.data.db.StudentEntity
import com.example.data.db.StudentWeightLogEntity
import com.example.data.db.StudentWorkoutExerciseEntity
import com.example.data.model.Exercise
import com.example.data.model.MuscleGroup
import com.example.ui.components.CameraQrScannerModal
import com.example.ui.components.QrCodeDisplayDialog
import com.example.ui.components.QrDisplayType
import com.example.ui.theme.*
import com.example.util.QrCodeUtils
import com.example.util.QrPayload
import java.text.SimpleDateFormat
import java.util.*

data class QrDialogState(
    val type: QrDisplayType,
    val title: String,
    val subtitle: String,
    val payloadText: String,
    val details: List<Pair<String, String>> = emptyList()
)

data class ScannerPromptState(
    val title: String,
    val subtitle: String,
    val samplePayload: String? = null
)

@Composable
fun TrainerStudentScreen(
    students: List<StudentEntity>,
    selectedStudent: StudentEntity?,
    selectedDay: Int,
    dayExercises: List<StudentWorkoutExerciseEntity>,
    allStudentExercises: List<StudentWorkoutExerciseEntity>,
    weightLogs: List<StudentWeightLogEntity>,
    onSelectStudent: (Long?) -> Unit,
    onSelectDay: (Int) -> Unit,
    onAddStudent: (String, String, Float, Float, Float, String, String, Boolean) -> Unit,
    onDeleteStudent: (Long) -> Unit,
    onAddExerciseToStudent: (Long, Int, String, String, String, String, Float, Int, Int, String) -> Unit,
    onUpdateExerciseWeight: (Long, Float, Boolean, Long, String) -> Unit,
    onDeleteStudentExercise: (Long) -> Unit,
    onAssignSamplePlan: (Long) -> Unit,
    onInspectExercise: (Exercise) -> Unit,
    userRole: AppUserRole = AppUserRole.TRAINER,
    onUserRoleChange: (AppUserRole) -> Unit = {},
    trainerProfile: QrPayload.TrainerProfile = QrPayload.TrainerProfile("TR-1", "Məşqçi"),
    studentProfile: QrPayload.StudentProfile = QrPayload.StudentProfile("ST-1", "Tələbə"),
    connectedTrainer: ConnectedTrainerInfo? = null,
    studentAssignedDays: List<StudentAssignedDay> = emptyList(),
    onDisconnectTrainer: () -> Unit = {},
    onUpdateStudentExerciseProgress: (Int, String, Float, Boolean) -> Unit = { _, _, _, _ -> },
    onSaveStudentProfile: (String, String, Float, Float, Float, String) -> Unit = { _, _, _, _, _, _ -> },
    onHandleScannedQr: (String) -> Unit = {},
    onGetAssignmentQr: (Int) -> String = { "" },
    onGetStudentProgressQr: (Int) -> String = { "" },
    modifier: Modifier = Modifier
) {
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var editingExerciseForWeight by remember { mutableStateOf<StudentWorkoutExerciseEntity?>(null) }
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Günlük Məşqlər, 1: Kq Tərəqqi Tarixçəsi

    var activeQrDialog by remember { mutableStateOf<QrDialogState?>(null) }
    var activeScanner by remember { mutableStateOf<ScannerPromptState?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("trainer_student_screen")
    ) {
        // Mode Switcher (Məşqçi vs Tələbə)
        RoleSwitcherHeader(
            currentRole = userRole,
            onRoleChange = onUserRoleChange
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (userRole == AppUserRole.STUDENT) {
                // STUDENT VIEW
                StudentSelfPortalView(
                    studentProfile = studentProfile,
                    connectedTrainer = connectedTrainer,
                    assignedDays = studentAssignedDays,
                    onOpenStudentQr = {
                        val encoded = QrCodeUtils.encodeStudentProfile(studentProfile)
                        activeQrDialog = QrDialogState(
                            type = QrDisplayType.STUDENT_PROFILE,
                            title = "Tələbə QR Kodum",
                            subtitle = studentProfile.fullName,
                            payloadText = encoded,
                            details = listOf(
                                "Tələbə ID" to studentProfile.studentId,
                                "Çəki" to "${studentProfile.currentWeightKg} kq",
                                "Hədəf Çəki" to "${studentProfile.targetWeightKg} kq",
                                "Məqsəd" to studentProfile.goal
                            )
                        )
                    },
                    onOpenScanTrainerQr = {
                        val sample = QrCodeUtils.encodeTrainerProfile(
                            QrPayload.TrainerProfile("TR-7721", "Kənan Müəllim", "Baş Məşqçi & Bodibildinq", "+994501234567")
                        )
                        activeScanner = ScannerPromptState(
                            title = "Müəllimin QR Kodunu Oxut",
                            subtitle = "Müəlliminizin telefonundakı QR kodu kameraya tutun",
                            samplePayload = sample
                        )
                    },
                    onOpenScanAssignmentQr = {
                        val sample = QrCodeUtils.encodeWorkoutAssignment(
                            QrPayload.WorkoutAssignment(
                                assignmentId = "DEMO-TASK-1",
                                trainerName = "Kənan Müəllim",
                                studentName = studentProfile.fullName,
                                dayOfWeek = 1,
                                dayTitle = "1-ci Gün: Sinə & Triceps",
                                exercises = listOf(
                                    com.example.util.AssignedExerciseItem("bench", "Barbell Bench Press", "Sinə", 60f, 4, 10, "Dirsəklərə diqqət et"),
                                    com.example.util.AssignedExerciseItem("incline", "Incline Dumbbell Press", "Sinə", 22f, 4, 10, "Yuxarı sinəni sıx")
                                )
                            )
                        )
                        activeScanner = ScannerPromptState(
                            title = "Müəllimdən Tapşırıq QR Oxut",
                            subtitle = "Müəllimin təyin etdiyi məşq QR kodunu kameraya tutun",
                            samplePayload = sample
                        )
                    },
                    onGenerateProgressQr = { dayNum ->
                        val encoded = onGetStudentProgressQr(dayNum)
                        if (encoded.isNotBlank()) {
                            activeQrDialog = QrDialogState(
                                type = QrDisplayType.PROGRESS_REPORT,
                                title = "Məşq Nəticəsi QR Kodu",
                                subtitle = "$dayNum-ci Günün İcra Hesabatı",
                                payloadText = encoded,
                                details = listOf(
                                    "Tələbə" to studentProfile.fullName,
                                    "Gün" to "$dayNum-ci Gün"
                                )
                            )
                        }
                    },
                    onDisconnectTrainer = onDisconnectTrainer,
                    onUpdateExerciseProgress = onUpdateStudentExerciseProgress,
                    onSaveProfile = onSaveStudentProfile
                )
            } else {
                // TRAINER VIEW
                if (selectedStudent == null) {
                    // VIEW 1: Directory of All Students
                    StudentsDirectoryView(
                        students = students,
                        trainerProfile = trainerProfile,
                        onSelectStudent = onSelectStudent,
                        onOpenTrainerQr = {
                            val encoded = QrCodeUtils.encodeTrainerProfile(trainerProfile)
                            activeQrDialog = QrDialogState(
                                type = QrDisplayType.TRAINER_PROFILE,
                                title = "Məşqçi QR Kodum",
                                subtitle = trainerProfile.trainerName,
                                payloadText = encoded,
                                details = listOf(
                                    "Məşqçi ID" to trainerProfile.trainerId,
                                    "İxtisas" to trainerProfile.specialty,
                                    "Əlaqə" to trainerProfile.phone
                                )
                            )
                        },
                        onOpenScanStudentQr = {
                            val sample = QrCodeUtils.encodeStudentProfile(
                                QrPayload.StudentProfile("ST-9944", "Elvin Məmmədov", "+994507778899", 78f, 85f, 182f, "Əzələ Kütləsi", "Həftədə 4 gün")
                            )
                            activeScanner = ScannerPromptState(
                                title = "Tələbə QR Kodunu Oxut",
                                subtitle = "Tələbənizin telefonundakı QR kodu oxudaraq dərhal siyahınıza əlavə edin",
                                samplePayload = sample
                            )
                        },
                        onOpenAddStudent = { showAddStudentDialog = true },
                        onDeleteStudent = onDeleteStudent
                    )
                } else {
                    // VIEW 2: Selected Student's Weekly Schedule & Weight Management
                    StudentDetailScheduleView(
                        student = selectedStudent,
                        selectedDay = selectedDay,
                        dayExercises = dayExercises,
                        allExercises = allStudentExercises,
                        weightLogs = weightLogs,
                        activeSubTab = activeSubTab,
                        onSubTabChange = { activeSubTab = it },
                        onBack = { onSelectStudent(null) },
                        onSelectDay = onSelectDay,
                        onOpenAddExercise = { showAddExerciseDialog = true },
                        onEditExerciseWeight = { editingExerciseForWeight = it },
                        onToggleExerciseComplete = { ex ->
                            onUpdateExerciseWeight(
                                ex.id,
                                ex.actualWeightKg,
                                !ex.isCompleted,
                                selectedStudent.id,
                                ex.exerciseName
                            )
                        },
                        onDeleteExercise = onDeleteStudentExercise,
                        onDeleteStudent = { onDeleteStudent(selectedStudent.id) },
                        onAssignSamplePlan = { onAssignSamplePlan(selectedStudent.id) },
                        onInspectExercise = onInspectExercise,
                        onSendAssignmentQr = { dayNum ->
                            val encoded = onGetAssignmentQr(dayNum)
                            if (encoded.isNotBlank()) {
                                activeQrDialog = QrDialogState(
                                    type = QrDisplayType.ASSIGNMENT,
                                    title = "Tapşırıq QR Kodu",
                                    subtitle = "${selectedStudent.fullName} • $dayNum-ci Gün",
                                    payloadText = encoded,
                                    details = listOf(
                                        "Tələbə" to selectedStudent.fullName,
                                        "Məşq Günü" to "$dayNum-ci Gün",
                                        "Hərəkət Sayı" to "${dayExercises.size} hərəkət"
                                    )
                                )
                            }
                        },
                        onScanStudentProgressQr = {
                            val sample = QrCodeUtils.encodeStudentProgress(
                                QrPayload.StudentProgressReport(
                                    reportId = "PROG-DEMO",
                                    studentName = selectedStudent.fullName,
                                    dayOfWeek = selectedDay,
                                    dayTitle = "$selectedDay-ci Gün",
                                    completedCount = dayExercises.size,
                                    results = dayExercises.map {
                                        com.example.util.ProgressExerciseItem(it.exerciseName, it.targetWeightKg, true)
                                    }
                                )
                            )
                            activeScanner = ScannerPromptState(
                                title = "Tələbənin Nəticə QR-nı Oxut",
                                subtitle = "Tələbənin tamamladığı hərəkətlər və qaldırdığı çəkilər bazanızda yenilənəcək",
                                samplePayload = sample
                            )
                        }
                    )
                }
            }

            // QR Code Display Dialog
            activeQrDialog?.let { dialog ->
                QrCodeDisplayDialog(
                    type = dialog.type,
                    title = dialog.title,
                    subtitle = dialog.subtitle,
                    payloadText = dialog.payloadText,
                    detailsSummary = dialog.details,
                    onDismiss = { activeQrDialog = null }
                )
            }

            // Camera QR Scanner Modal
            activeScanner?.let { scanner ->
                CameraQrScannerModal(
                    title = scanner.title,
                    subtitle = scanner.subtitle,
                    samplePayloadForTesting = scanner.samplePayload,
                    onQrCodeScanned = { scannedText ->
                        onHandleScannedQr(scannedText)
                        activeScanner = null
                    },
                    onDismiss = { activeScanner = null }
                )
            }
        }

        // Dialog: Add New Student
        if (showAddStudentDialog) {
            AddStudentDialog(
                onDismiss = { showAddStudentDialog = false },
                onConfirm = { name, phone, curW, tarW, h, goal, notes, assignDefault ->
                    onAddStudent(name, phone, curW, tarW, h, goal, notes, assignDefault)
                    showAddStudentDialog = false
                }
            )
        }

        // Dialog: Add Exercise To Student Day
        if (showAddExerciseDialog && selectedStudent != null) {
            AddStudentExerciseDialog(
                dayNumber = selectedDay,
                onDismiss = { showAddExerciseDialog = false },
                onConfirm = { exId, exName, muscle, targetKg, sets, reps, notes ->
                    val dayTitle = getDayTitle(selectedDay)
                    onAddExerciseToStudent(
                        selectedStudent.id,
                        selectedDay,
                        dayTitle,
                        exId,
                        exName,
                        muscle,
                        targetKg,
                        sets,
                        reps,
                        notes
                    )
                    showAddExerciseDialog = false
                }
            )
        }

        // Dialog: Edit Weight & Completion for an Exercise
        editingExerciseForWeight?.let { exercise ->
            EditExerciseWeightDialog(
                exercise = exercise,
                onDismiss = { editingExerciseForWeight = null },
                onSave = { newWeightKg, isDone ->
                    onUpdateExerciseWeight(
                        exercise.id,
                        newWeightKg,
                        isDone,
                        exercise.studentId,
                        exercise.exerciseName
                    )
                    editingExerciseForWeight = null
                }
            )
        }
    }
}

// -------------------------------------------------------------
// VIEW 1: Directory of All Students
// -------------------------------------------------------------
@Composable
private fun StudentsDirectoryView(
    students: List<StudentEntity>,
    trainerProfile: QrPayload.TrainerProfile,
    onSelectStudent: (Long) -> Unit,
    onOpenTrainerQr: () -> Unit,
    onOpenScanStudentQr: () -> Unit,
    onOpenAddStudent: () -> Unit,
    onDeleteStudent: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredStudents = remember(students, searchQuery) {
        if (searchQuery.isBlank()) students
        else students.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
            it.phone.contains(searchQuery) ||
            it.goal.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Coach Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(CyanPrimary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = CyanPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = trainerProfile.trainerName,
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = "${trainerProfile.specialty} • ${students.size} Tələbə",
                                color = CyanPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tələbələrinizə günlər üzrə tapşırıq verin, proqramı QR kod ilə onların telefonuna göndərin və nəticələrini izləyin.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // QR Actions: Coach QR & Scan Student QR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenTrainerQr,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("coach_qr_button")
                        ) {
                            Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = CyanOnPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Məşqçi QR-ım", color = CyanOnPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onOpenScanStudentQr,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("scan_student_qr_btn"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(DarkSurfaceVariant)
                            )
                        ) {
                            Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Tələbə QR Oxut", fontSize = 12.sp, color = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onOpenAddStudent,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("add_student_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(DarkSurfaceVariant)
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Əllə Tələbə Əlavə Et", color = TextMuted, fontSize = 13.sp)
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_student_field"),
                placeholder = { Text("Tələbə adı və ya məqsəd ilə axtar...", color = TextMuted, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Təmizlə", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanPrimary,
                    unfocusedBorderColor = DarkSurfaceVariant,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
        }

        if (filteredStudents.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonSearch,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isBlank()) "Hələ heç bir tələbə əlavə edilməyib" else "Axtarışa uyğun tələbə tapılmadı",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Aşağıdakı düymə ilə yeni tələbə qeydiyyatdan keçirin.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onOpenAddStudent,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = CyanOnPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("İlk Tələbəni Əlavə Et", color = CyanOnPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                        }
                    }
                }
            }
        } else {
            items(filteredStudents, key = { it.id }) { student ->
                StudentCardItem(
                    student = student,
                    onOpenStudent = { onSelectStudent(student.id) },
                    onDelete = { onDeleteStudent(student.id) }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Student Card Item in Directory
// -------------------------------------------------------------
@Composable
private fun StudentCardItem(
    student: StudentEntity,
    onOpenStudent: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenStudent() }
            .testTag("student_card_${student.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Initials circle
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(DarkSurfaceVariant, CircleShape)
                            .border(1.5.dp, CyanPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = student.fullName.split(" ")
                            .take(2)
                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .joinToString("")
                        Text(
                            text = if (initials.isNotEmpty()) initials else "T",
                            color = CyanPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = student.fullName,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (student.phone.isNotBlank()) {
                            Text(
                                text = student.phone,
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = OrangeAccent.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = student.goal,
                        color = OrangeAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metrics row: Weight, Target, Height
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Cari Çəki", color = TextMuted, fontSize = 10.sp)
                    Text(
                        "${student.currentWeightKg} kq",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )

                Column {
                    Text("Hədəf Çəki", color = TextMuted, fontSize = 10.sp)
                    Text(
                        "${student.targetWeightKg} kq",
                        color = LimeSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Column {
                    Text("Boy", color = TextMuted, fontSize = 10.sp)
                    Text(
                        "${student.heightCm.toInt()} sm",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            if (student.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Qeyd: ${student.notes}",
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Sil",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Button(
                    onClick = onOpenStudent,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Məşq Cədvəli & Kq ➔",
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = DarkSurface,
            title = { Text("Tələbəni silinsin?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "${student.fullName} adlı tələbə və onun bütün məşq cədvəli silinəcək.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Sil", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Ləğv et", color = TextMuted)
                }
            }
        )
    }
}

// -------------------------------------------------------------
// VIEW 2: Selected Student's Weekly Schedule & Weight Management
// -------------------------------------------------------------
@Composable
private fun StudentDetailScheduleView(
    student: StudentEntity,
    selectedDay: Int,
    dayExercises: List<StudentWorkoutExerciseEntity>,
    allExercises: List<StudentWorkoutExerciseEntity>,
    weightLogs: List<StudentWeightLogEntity>,
    activeSubTab: Int,
    onSubTabChange: (Int) -> Unit,
    onBack: () -> Unit,
    onSelectDay: (Int) -> Unit,
    onOpenAddExercise: () -> Unit,
    onEditExerciseWeight: (StudentWorkoutExerciseEntity) -> Unit,
    onToggleExerciseComplete: (StudentWorkoutExerciseEntity) -> Unit,
    onDeleteExercise: (Long) -> Unit,
    onDeleteStudent: () -> Unit = {},
    onAssignSamplePlan: () -> Unit,
    onInspectExercise: (Exercise) -> Unit,
    onSendAssignmentQr: (Int) -> Unit = {},
    onScanStudentProgressQr: () -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Back and Student Profile Header
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
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f, fill = false)) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DarkBackground, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Geri",
                                    tint = CyanPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = student.fullName,
                                    color = TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Məqsəd: ${student.goal}",
                                    color = OrangeAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = DarkBackground
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Scale,
                                        contentDescription = null,
                                        tint = LimeSuccess,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${student.currentWeightKg} kq",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            IconButton(
                                onClick = { showDeleteConfirm = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DarkBackground, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Tələbəni Sil",
                                    tint = Color(0xFFEF5350),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    if (student.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Məşqçi Təlimatı: ${student.notes}",
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sub Tabs: "Günlük Hərəkətlər" vs "Kq Tərəqqi Tarixçəsi"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkBackground, RoundedCornerShape(10.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (activeSubTab == 0) CyanPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSubTabChange(0) }
                        ) {
                            Text(
                                text = "Günlük Hərəkətlər",
                                color = if (activeSubTab == 0) CyanOnPrimary else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (activeSubTab == 1) CyanPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSubTabChange(1) }
                        ) {
                            Text(
                                text = "Kq Tərəqqi Tarixçəsi",
                                color = if (activeSubTab == 1) CyanOnPrimary else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        if (activeSubTab == 0) {
            // SUB TAB 0: DAY OF WEEK SELECTOR & EXERCISE SCHEDULE
            item {
                Column {
                    Text(
                        text = "Məşq Gününü Seçin:",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 7 Days Horizontal Selector (1..7)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(7) { index ->
                            val dayNum = index + 1
                            val dayShort = getDayShortName(dayNum)
                            val isSelected = selectedDay == dayNum
                            val countForDay = allExercises.count { it.dayOfWeek == dayNum }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) CyanPrimary else DarkSurface,
                                border = if (!isSelected && countForDay > 0) androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.4f)) else null,
                                modifier = Modifier
                                    .clickable { onSelectDay(dayNum) }
                                    .testTag("day_selector_$dayNum")
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$dayNum-ci Gün",
                                        color = if (isSelected) CyanOnPrimary else TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = dayShort,
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
                                            text = "$countForDay hərəkət",
                                            color = if (isSelected) CyanOnPrimary else CyanPrimary,
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

            // QR Actions for Assignment & Student Progress
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSendAssignmentQr(selectedDay) },
                        colors = ButtonDefaults.buttonColors(containerColor = LimeSuccess),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("send_task_qr_btn")
                    ) {
                        Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Tapşırığı QR ilə Göndər", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onScanStudentProgressQr,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(DarkSurfaceVariant)
                        )
                    ) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Nəticə QR Oxut", fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }

            // Day Title and Add Exercise Action
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = getDayTitle(selectedDay),
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${dayExercises.size} hərəkət təyin olunub",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onOpenAddExercise,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_exercise_to_student_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = CyanOnPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hərəkət Əlavə Et", color = CyanOnPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (dayExercises.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
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
                                tint = TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Bu gün üçün heç bir hərəkət təyin edilməyib",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tələbəyə bu gün edəcəyi hərəkətləri və icra edəcəyi çəkiləri (kq) təyin edin.",
                                color = TextMuted,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onOpenAddExercise,
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("+ Hərəkət Seç", color = CyanOnPrimary, fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = onAssignSamplePlan,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Şablon Proqram Yüklə", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                items(dayExercises, key = { it.id }) { exercise ->
                    StudentExerciseCard(
                        exercise = exercise,
                        onEditWeight = { onEditExerciseWeight(exercise) },
                        onToggleComplete = { onToggleExerciseComplete(exercise) },
                        onDelete = { onDeleteExercise(exercise.id) },
                        onInspect = {
                            // Find corresponding exercise in library for 3D animation
                            val found = FitnessData.allExercises.find { it.id == exercise.exerciseId }
                                ?: FitnessData.allExercises.first()
                            onInspectExercise(found)
                        }
                    )
                }
            }
        } else {
            // SUB TAB 1: WEIGHT PROGRESSION LOGS
            if (weightLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Hələ çəki qeydləri qeydə alınmayıb",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tələbə məşq etdikcə və siz çəkini yenilədikcə bütün kq artımları burada xronoloji olaraq görünəcək.",
                                color = TextMuted,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(weightLogs, key = { it.id }) { log ->
                    WeightLogItemCard(log = log)
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = DarkSurface,
            title = { Text("Tələbə silinsin?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "${student.fullName} adlı tələbə və onun bütün məşq cədvəli silinəcək.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteStudent()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Sil", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Ləğv et", color = TextMuted)
                }
            }
        )
    }
}

// -------------------------------------------------------------
// Student Exercise Card with KG, Sets, Reps & Animation Preview
// -------------------------------------------------------------
@Composable
private fun StudentExerciseCard(
    exercise: StudentWorkoutExerciseEntity,
    onEditWeight: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onInspect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("student_exercise_card_${exercise.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (exercise.isCompleted) DarkSurfaceVariant.copy(alpha = 0.7f) else DarkSurface
        ),
        border = if (exercise.isCompleted) androidx.compose.foundation.BorderStroke(1.dp, LimeSuccess.copy(alpha = 0.5f)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row: Exercise Name, Muscle chip, and Status Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onToggleComplete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (exercise.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = if (exercise.isCompleted) "Tamamlandı" else "Gözləyir",
                            tint = if (exercise.isCompleted) LimeSuccess else TextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column {
                        Text(
                            text = exercise.exerciseName,
                            color = if (exercise.isCompleted) LimeSuccess else TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarkBackground
                        ) {
                            Text(
                                text = exercise.muscleGroup,
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Sil",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // WEIGHT & METRICS HIGHLIGHT BOX
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Target Weight
                Column {
                    Text(text = "Hədəf Çəki", color = TextMuted, fontSize = 10.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${exercise.targetWeightKg}",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(text = " kq", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }

                // Actual Lifted Weight
                Column {
                    Text(text = "İcra Edilən Çəki", color = CyanPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${exercise.actualWeightKg}",
                            color = CyanPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(text = " kq", color = CyanPrimary, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }

                // Sets x Reps
                Column {
                    Text(text = "Yanaşma & Təkrar", color = TextMuted, fontSize = 10.sp)
                    Text(
                        text = "${exercise.targetSets} set × ${exercise.targetReps}",
                        color = OrangeAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (exercise.trainerNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = exercise.trainerNotes,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Edit Weight & Inspect Animation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Inspect Animation Button (allows coach to show technique to student)
                OutlinedButton(
                    onClick = onInspect,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Animasiyaya Bax", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                // Edit Weight / Status Button
                Button(
                    onClick = onEditWeight,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = CyanOnPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Çəkini Dəyiş (Kq)", color = CyanOnPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Weight Progression Log Card
// -------------------------------------------------------------
@Composable
private fun WeightLogItemCard(log: StudentWeightLogEntity) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("az")) }
    val dateStr = remember(log.timestamp) { dateFormat.format(Date(log.timestamp)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = log.exerciseName,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateStr,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CyanPrimary.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${log.weightKg}",
                        color = CyanPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " kq",
                        color = CyanPrimary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Dialog: Add New Student
// -------------------------------------------------------------
@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, currentWeight: Float, targetWeight: Float, height: Float, goal: String, notes: String, assignDefault: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var weightText by remember { mutableStateOf("75") }
    var targetWeightText by remember { mutableStateOf("80") }
    var heightText by remember { mutableStateOf("178") }
    var selectedGoal by remember { mutableStateOf("Əzələ Kütləsi") }
    var notes by remember { mutableStateOf("") }
    var assignDefaultPlan by remember { mutableStateOf(true) }

    val goals = listOf("Əzələ Kütləsi", "Çəki Atmaq", "Güc Artımı", "Bədən Formalaşdırma")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = CyanPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Yeni Tələbə Qeydiyyatı", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tələbənin Adı və Soyadı *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_student_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = DarkSurfaceVariant,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Telefon Nömrəsi") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = DarkSurfaceVariant,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Çəki (kq)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = targetWeightText,
                        onValueChange = { targetWeightText = it },
                        label = { Text("Hədəf (kq)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = heightText,
                        onValueChange = { heightText = it },
                        label = { Text("Boy (sm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                Text("Məşq Məqsədi:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(goals) { goal ->
                        val isSelected = selectedGoal == goal
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) CyanPrimary else DarkBackground,
                            modifier = Modifier.clickable { selectedGoal = goal }
                        ) {
                            Text(
                                text = goal,
                                color = if (isSelected) CyanOnPrimary else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Məşqçi Qeydləri (Zədə, diet və s.)") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = DarkSurfaceVariant,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Row(
                    modifier = Modifier.clickable { assignDefaultPlan = !assignDefaultPlan },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = assignDefaultPlan,
                        onCheckedChange = { assignDefaultPlan = it },
                        colors = CheckboxDefaults.colors(checkedColor = CyanPrimary)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Standart 3-günlük məşq şablonunu təyin et",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val curW = weightText.toFloatOrNull() ?: 75f
                        val tarW = targetWeightText.toFloatOrNull() ?: curW
                        val h = heightText.toFloatOrNull() ?: 175f
                        onConfirm(name, phone, curW, tarW, h, selectedGoal, notes, assignDefaultPlan)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                modifier = Modifier.testTag("confirm_add_student_button")
            ) {
                Text("Qeyd Et", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ləğv et", color = TextMuted)
            }
        }
    )
}

// -------------------------------------------------------------
// Dialog: Add Exercise To Student Day
// -------------------------------------------------------------
@Composable
fun AddStudentExerciseDialog(
    dayNumber: Int,
    onDismiss: () -> Unit,
    onConfirm: (exerciseId: String, exerciseName: String, muscleGroup: String, targetWeightKg: Float, sets: Int, reps: Int, notes: String) -> Unit
) {
    var selectedExercise by remember { mutableStateOf(FitnessData.allExercises.first()) }
    var isCustomExercise by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    var customMuscle by remember { mutableStateOf("Sinə") }
    var weightText by remember { mutableStateOf("50") }
    var setsText by remember { mutableStateOf("4") }
    var repsText by remember { mutableStateOf("10") }
    var trainerNotes by remember { mutableStateOf("") }
    var selectedMuscleFilter by remember { mutableStateOf<MuscleGroup?>(null) }

    val filteredList = remember(selectedMuscleFilter) {
        if (selectedMuscleFilter == null) FitnessData.allExercises
        else FitnessData.allExercises.filter { it.primaryMuscle == selectedMuscleFilter }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text(
                text = "${getDayShortName(dayNumber)} üçün Hərəkət Əlavə Et",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Toggle Preset vs Custom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkBackground, RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (!isCustomExercise) CyanPrimary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isCustomExercise = false }
                    ) {
                        Text(
                            text = "Kataloqdan Seç (32 Hərəkət)",
                            color = if (!isCustomExercise) CyanOnPrimary else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isCustomExercise) CyanPrimary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isCustomExercise = true }
                    ) {
                        Text(
                            text = "Fərdi Hərəkət Yaz",
                            color = if (isCustomExercise) CyanOnPrimary else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                if (!isCustomExercise) {
                    // Muscle filter chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(MuscleGroup.values().toList()) { m ->
                            val isSel = selectedMuscleFilter == m
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) CyanPrimary else DarkBackground,
                                modifier = Modifier.clickable {
                                    selectedMuscleFilter = if (isSel) null else m
                                }
                            ) {
                                Text(
                                    text = m.nameAz,
                                    color = if (isSel) CyanOnPrimary else TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Exercise selector box
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkBackground),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LazyColumn(modifier = Modifier.height(130.dp)) {
                            items(filteredList) { ex ->
                                val isSelected = selectedExercise.id == ex.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedExercise = ex }
                                        .background(if (isSelected) CyanPrimary.copy(alpha = 0.2f) else Color.Transparent)
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = ex.nameAz,
                                        color = if (isSelected) CyanPrimary else TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = ex.primaryMuscle.nameAz,
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Hərəkətin adı") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                // Target Weight (KG), Sets, Reps
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Çəki (Kq)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = setsText,
                        onValueChange = { setsText = it },
                        label = { Text("Yanaşma") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.9f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = { repsText = it },
                        label = { Text("Təkrar") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.9f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = DarkSurfaceVariant,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                OutlinedTextField(
                    value = trainerNotes,
                    onValueChange = { trainerNotes = it },
                    label = { Text("Məşqçi tapşırığı / Texnika qeydi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = DarkSurfaceVariant,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val w = weightText.toFloatOrNull() ?: 50f
                    val s = setsText.toIntOrNull() ?: 4
                    val r = repsText.toIntOrNull() ?: 10
                    val (exId, exName, muscle) = if (isCustomExercise) {
                        Triple("custom_${System.currentTimeMillis()}", customName.ifBlank { "Məşq Hərəkəti" }, customMuscle)
                    } else {
                        Triple(selectedExercise.id, selectedExercise.nameAz, selectedExercise.primaryMuscle.nameAz)
                    }
                    onConfirm(exId, exName, muscle, w, s, r, trainerNotes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Text("Əlavə Et", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ləğv et", color = TextMuted)
            }
        }
    )
}

// -------------------------------------------------------------
// Dialog: Edit Weight & Completion For Exercise
// -------------------------------------------------------------
@Composable
fun EditExerciseWeightDialog(
    exercise: StudentWorkoutExerciseEntity,
    onDismiss: () -> Unit,
    onSave: (newWeightKg: Float, isDone: Boolean) -> Unit
) {
    var weightValue by remember { mutableFloatStateOf(exercise.actualWeightKg) }
    var isDone by remember { mutableStateOf(exercise.isCompleted) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        title = {
            Text(
                text = "${exercise.exerciseName} - Çəki İzləməsi",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Tələbənin Qaldırdığı Çəki:",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                // Large KG Display
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = String.format(Locale.US, "%.1f", weightValue),
                                color = CyanPrimary,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = " KG",
                                color = CyanPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                }

                // Quick Stepper Buttons (+/- 1.25, +/- 2.5, +/- 5.0)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(-5f, -2.5f, -1.25f, +1.25f, +2.5f, +5f).forEach { delta ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkBackground,
                            modifier = Modifier.clickable {
                                weightValue = (weightValue + delta).coerceAtLeast(0f)
                            }
                        ) {
                            Text(
                                text = if (delta > 0) "+$delta" else "$delta",
                                color = if (delta > 0) LimeSuccess else OrangeAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Completed Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkBackground, RoundedCornerShape(10.dp))
                        .clickable { isDone = !isDone }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Məşq Tamamlandı Olararaq Qeyd Et",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = isDone,
                        onCheckedChange = { isDone = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LimeSuccess,
                            checkedTrackColor = LimeSuccess.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(weightValue, isDone) },
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
            ) {
                Text("Yadda Saxla", color = CyanOnPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ləğv et", color = TextMuted)
            }
        }
    )
}

// Helper: Day Titles in Azerbaijani
fun getDayTitle(dayNumber: Int): String {
    return when (dayNumber) {
        1 -> "1-ci Gün: Bazar ertəsi"
        2 -> "2-ci Gün: Çərşənbə axşamı"
        3 -> "3-cü Gün: Çərşənbə"
        4 -> "4-cü Gün: Cümə axşamı"
        5 -> "5-ci Gün: Cümə"
        6 -> "6-cı Gün: Şənbə"
        7 -> "7-ci Gün: Bazar"
        else -> "$dayNumber-ci Gün"
    }
}

fun getDayShortName(dayNumber: Int): String {
    return when (dayNumber) {
        1 -> "B.ertəsi"
        2 -> "Ç.axşamı"
        3 -> "Çərşənbə"
        4 -> "C.axşamı"
        5 -> "Cümə"
        6 -> "Şənbə"
        7 -> "Bazar"
        else -> "Gün $dayNumber"
    }
}

@Composable
fun RoleSwitcherHeader(
    currentRole: AppUserRole,
    onRoleChange: (AppUserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkSurface,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (currentRole == AppUserRole.TRAINER) CyanPrimary else Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onRoleChange(AppUserRole.TRAINER) }
                    .testTag("role_trainer_btn")
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = if (currentRole == AppUserRole.TRAINER) CyanOnPrimary else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Məşqçi Rejimi",
                        color = if (currentRole == AppUserRole.TRAINER) CyanOnPrimary else TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (currentRole == AppUserRole.STUDENT) OrangeAccent else Color.Transparent,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onRoleChange(AppUserRole.STUDENT) }
                    .testTag("role_student_btn")
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (currentRole == AppUserRole.STUDENT) Color.Black else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tələbə Rejimi",
                        color = if (currentRole == AppUserRole.STUDENT) Color.Black else TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
