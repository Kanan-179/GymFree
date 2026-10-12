package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ExerciseDetailSheet
import com.example.ui.components.RestTimerDialog
import com.example.ui.components.TodayMuscleSelectionDialog
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MuscleDirectoryScreen
import com.example.ui.screens.ProgramScreen
import com.example.ui.screens.TrainerStudentScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                FitnessAppMain()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessAppMain(
    viewModel: WorkoutViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val selectedDayNumber by viewModel.selectedDayNumber.collectAsStateWithLifecycle()
    val selectedMuscle by viewModel.selectedMuscle.collectAsStateWithLifecycle()
    val inspectedExercise by viewModel.inspectedExercise.collectAsStateWithLifecycle()
    val restTimerSeconds by viewModel.restTimerSeconds.collectAsStateWithLifecycle()
    val activeSession by viewModel.activeSession.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val totalWorkoutsCount by viewModel.totalWorkoutsCount.collectAsStateWithLifecycle()
    val completedBannerMessage by viewModel.completedBannerMessage.collectAsStateWithLifecycle()
    val todayMuscles by viewModel.todayMuscles.collectAsStateWithLifecycle()
    val recommendedPlan by viewModel.recommendedPlan.collectAsStateWithLifecycle()
    val showTodaySelectionDialog by viewModel.showTodaySelectionDialog.collectAsStateWithLifecycle()

    // Trainer / Student States
    val students by viewModel.students.collectAsStateWithLifecycle()
    val selectedStudent by viewModel.selectedStudent.collectAsStateWithLifecycle()
    val selectedStudentDay by viewModel.selectedStudentDay.collectAsStateWithLifecycle()
    val studentDayExercises by viewModel.currentStudentDayExercises.collectAsStateWithLifecycle()
    val studentAllExercises by viewModel.currentStudentAllExercises.collectAsStateWithLifecycle()
    val studentWeightLogs by viewModel.currentStudentWeightLogs.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val trainerProfile by viewModel.trainerProfile.collectAsStateWithLifecycle()
    val studentProfile by viewModel.studentProfile.collectAsStateWithLifecycle()
    val connectedTrainer by viewModel.connectedTrainer.collectAsStateWithLifecycle()
    val studentAssignedDays by viewModel.studentAssignedDays.collectAsStateWithLifecycle()
    val toastNotification by viewModel.toastNotification.collectAsStateWithLifecycle()

    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(toastNotification) {
        toastNotification?.let { msg ->
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_fitness_logo),
                            contentDescription = "FitMəşq Loqo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "FitMəşq",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = LimeSuccess.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "100% PULSUZ",
                                        color = LimeSuccess,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "3 Günlük Məşq & Əzələ Bələdçisi",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                },
                actions = {
                    // Quick Target Muscle selector trigger
                    IconButton(
                        onClick = { viewModel.openTodaySelectionDialog() },
                        modifier = Modifier.testTag("top_muscle_select_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Bu gün hansı əzələ?",
                            tint = CyanPrimary
                        )
                    }

                    // Quick Rest Timer trigger
                    IconButton(
                        onClick = { viewModel.openRestTimer(60) },
                        modifier = Modifier.testTag("top_timer_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "İstirahət Taymeri",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Group else Icons.Outlined.Group,
                            contentDescription = "Məşqçi & Tələbə"
                        )
                    },
                    label = { Text("Məşqçi/Tələbə", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanPrimary,
                        indicatorColor = CyanPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_students")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.FitnessCenter else Icons.Outlined.FitnessCenter,
                            contentDescription = "3 Günlük Proqram"
                        )
                    },
                    label = { Text("3 Günlük", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanPrimary,
                        indicatorColor = CyanPrimary,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_program")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.AccessibilityNew else Icons.Outlined.AccessibilityNew,
                            contentDescription = "Əzələ Qrupları"
                        )
                    },
                    label = { Text("Əzələlər", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = OrangeAccent,
                        indicatorColor = OrangeAccent,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_muscles")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 3) Icons.Filled.History else Icons.Outlined.History,
                            contentDescription = "Gündəlik"
                        )
                    },
                    label = { Text("Gündəlik", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = LimeSuccess,
                        indicatorColor = LimeSuccess,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("tab_history")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> TrainerStudentScreen(
                    students = students,
                    selectedStudent = selectedStudent,
                    selectedDay = selectedStudentDay,
                    dayExercises = studentDayExercises,
                    allStudentExercises = studentAllExercises,
                    weightLogs = studentWeightLogs,
                    onSelectStudent = { viewModel.selectStudent(it) },
                    onSelectDay = { viewModel.selectStudentDay(it) },
                    onAddStudent = { name, phone, curW, tarW, h, goal, notes, assignDefault ->
                        viewModel.addStudent(name, phone, curW, tarW, h, goal, notes, assignDefault)
                    },
                    onDeleteStudent = { viewModel.deleteStudent(it) },
                    onAddExerciseToStudent = { sId, day, title, exId, name, muscle, w, s, r, notes ->
                        viewModel.addExerciseToStudent(sId, day, title, exId, name, muscle, w, s, r, notes)
                    },
                    onUpdateExerciseWeight = { exId, w, done, sId, name ->
                        viewModel.updateExerciseWeightAndStatus(exId, w, done, sId, name)
                    },
                    onDeleteStudentExercise = { viewModel.deleteStudentExercise(it) },
                    onAssignSamplePlan = { viewModel.assignSamplePlanToStudent(it) },
                    onInspectExercise = { viewModel.inspectExercise(it) },
                    userRole = userRole,
                    onUserRoleChange = { viewModel.setUserRole(it) },
                    trainerProfile = trainerProfile,
                    studentProfile = studentProfile,
                    connectedTrainer = connectedTrainer,
                    studentAssignedDays = studentAssignedDays,
                    onDisconnectTrainer = { viewModel.disconnectTrainer() },
                    onUpdateStudentExerciseProgress = { d, id, w, done ->
                        viewModel.updateStudentExerciseProgress(d, id, w, done)
                    },
                    onSaveStudentProfile = { n, p, cW, tW, h, g ->
                        viewModel.saveStudentProfile(n, p, cW, tW, h, g)
                    },
                    onHandleScannedQr = { viewModel.handleScannedQr(it) },
                    onGetAssignmentQr = { viewModel.getAssignmentQrForCurrentStudent(it) },
                    onGetStudentProgressQr = { viewModel.getStudentProgressQr(it) }
                )
                1 -> ProgramScreen(
                    selectedDayNumber = selectedDayNumber,
                    todayMuscles = todayMuscles,
                    recommendedPlan = recommendedPlan,
                    activeSession = activeSession,
                    completedBannerMessage = completedBannerMessage,
                    onSelectDay = { viewModel.selectDay(it) },
                    onOpenTodayMuscleSelector = { viewModel.openTodaySelectionDialog() },
                    onInspectExercise = { viewModel.inspectExercise(it) },
                    onStartWorkout = { viewModel.startWorkout(it) },
                    onStartRecommendedWorkout = { viewModel.startRecommendedWorkout(it) },
                    onFinishWorkout = { viewModel.finishWorkout() },
                    onCancelWorkout = { viewModel.cancelActiveWorkout() },
                    onToggleSet = { exId, setIdx, total -> viewModel.toggleSetCompletion(exId, setIdx, total) },
                    onDismissBanner = { viewModel.dismissCompletedBanner() },
                    onOpenTimer = { viewModel.openRestTimer(60) }
                )
                2 -> MuscleDirectoryScreen(
                    selectedMuscle = selectedMuscle,
                    onSelectMuscle = { viewModel.selectMuscle(it) },
                    onInspectExercise = { viewModel.inspectExercise(it) }
                )
                3 -> HistoryScreen(
                    historyList = historyList,
                    totalCount = totalWorkoutsCount,
                    onDeleteHistory = { viewModel.deleteHistoryItem(it) }
                )
            }
        }
    }

    // Modal "Bu gün hansı əzələləri məşq edirsən?" Dialog
    if (showTodaySelectionDialog) {
        TodayMuscleSelectionDialog(
            initialSelectedMuscles = todayMuscles,
            onDismiss = { viewModel.closeTodaySelectionDialog() },
            onConfirm = { selected ->
                viewModel.applyTodayMuscles(selected)
            }
        )
    }

    // Modal Exercise Details & Animation Sheet
    inspectedExercise?.let { exercise ->
        ExerciseDetailSheet(
            exercise = exercise,
            onDismiss = { viewModel.inspectExercise(null) },
            onOpenTimer = {
                viewModel.openRestTimer(exercise.restSeconds)
            }
        )
    }

    // Modal Rest Countdown Timer Dialog
    restTimerSeconds?.let { seconds ->
        RestTimerDialog(
            initialSeconds = seconds,
            onDismiss = { viewModel.closeRestTimer() }
        )
    }
}

// Kept for screenshot test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
