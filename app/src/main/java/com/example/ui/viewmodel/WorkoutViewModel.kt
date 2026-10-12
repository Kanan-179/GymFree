package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.FitnessData
import com.example.data.datasource.TodayWorkoutPreferences
import com.example.data.datasource.UserAvatarPreferences
import com.example.data.datasource.WorkoutRecommendationEngine
import com.example.data.datasource.RecommendedPlan
import com.example.data.db.WorkoutDatabase
import com.example.data.db.WorkoutHistoryEntity
import com.example.data.model.Exercise
import com.example.data.model.MuscleGroup
import com.example.data.model.UserAvatarConfig
import com.example.data.model.WorkoutDay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ActiveWorkoutSession(
    val dayNumber: Int,
    val dayTitle: String,
    val isRunning: Boolean = false,
    val startedTimestamp: Long = 0L,
    val elapsedSeconds: Int = 0,
    // exerciseId -> completed sets count
    val completedSets: Map<String, Int> = emptyMap(),
    // exerciseId -> user weight notes
    val exerciseNotes: Map<String, String> = emptyMap(),
    // Total exercises for finish calculation
    val totalExercisesInSession: Int = 6
)

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val db = WorkoutDatabase.getDatabase(application)
    private val historyDao = db.workoutHistoryDao()
    private val trainerRepo = com.example.data.repository.TrainerRepository(db.trainerDao())
    private val avatarPrefs = UserAvatarPreferences(application)
    private val todayPrefs = TodayWorkoutPreferences(application)
    private val rolePrefs = com.example.data.datasource.UserRolePreferences(application)

    // User Role (Məşqçi vs Tələbə)
    private val _userRole = MutableStateFlow(rolePrefs.getUserRole())
    val userRole: StateFlow<com.example.data.datasource.AppUserRole> = _userRole.asStateFlow()

    private val _trainerProfile = MutableStateFlow(rolePrefs.getTrainerProfile())
    val trainerProfile: StateFlow<com.example.util.QrPayload.TrainerProfile> = _trainerProfile.asStateFlow()

    private val _studentProfile = MutableStateFlow(rolePrefs.getStudentProfile())
    val studentProfile: StateFlow<com.example.util.QrPayload.StudentProfile> = _studentProfile.asStateFlow()

    private val _connectedTrainer = MutableStateFlow(rolePrefs.getConnectedTrainer())
    val connectedTrainer: StateFlow<com.example.data.datasource.ConnectedTrainerInfo?> = _connectedTrainer.asStateFlow()

    private val _studentAssignedDays = MutableStateFlow(rolePrefs.getStudentAssignedWorkouts())
    val studentAssignedDays: StateFlow<List<com.example.data.datasource.StudentAssignedDay>> = _studentAssignedDays.asStateFlow()

    private val _toastNotification = MutableStateFlow<String?>(null)
    val toastNotification: StateFlow<String?> = _toastNotification.asStateFlow()

    // Trainer / Student Management
    val students: StateFlow<List<com.example.data.db.StudentEntity>> = trainerRepo.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedStudentId = MutableStateFlow<Long?>(null)
    val selectedStudentId: StateFlow<Long?> = _selectedStudentId.asStateFlow()

    private val _selectedStudentDay = MutableStateFlow(1) // 1: Bazar ertəsi, 2: Çərşənbə axşamı, ...
    val selectedStudentDay: StateFlow<Int> = _selectedStudentDay.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val selectedStudent: StateFlow<com.example.data.db.StudentEntity?> = _selectedStudentId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else trainerRepo.getStudentById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentStudentDayExercises: StateFlow<List<com.example.data.db.StudentWorkoutExerciseEntity>> =
        combine(_selectedStudentId, _selectedStudentDay) { studentId, day ->
            studentId to day
        }.flatMapLatest { (studentId, day) ->
            if (studentId == null) flowOf(emptyList())
            else trainerRepo.getExercisesForStudentAndDay(studentId, day)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentStudentAllExercises: StateFlow<List<com.example.data.db.StudentWorkoutExerciseEntity>> =
        _selectedStudentId.flatMapLatest { studentId ->
            if (studentId == null) flowOf(emptyList())
            else trainerRepo.getAllExercisesForStudent(studentId)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val currentStudentWeightLogs: StateFlow<List<com.example.data.db.StudentWeightLogEntity>> =
        _selectedStudentId.flatMapLatest { studentId ->
            if (studentId == null) flowOf(emptyList())
            else trainerRepo.getWeightLogs(studentId)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User's custom athlete avatar configuration
    private val _avatarConfig = MutableStateFlow(avatarPrefs.loadAvatarConfig())
    val avatarConfig: StateFlow<UserAvatarConfig> = _avatarConfig.asStateFlow()

    // Today's target muscles & recommendation
    private val _todayMuscles = MutableStateFlow(todayPrefs.getSavedMuscles())
    val todayMuscles: StateFlow<Set<MuscleGroup>> = _todayMuscles.asStateFlow()

    private val _recommendedPlan = MutableStateFlow(
        WorkoutRecommendationEngine.generatePlan(_todayMuscles.value)
    )
    val recommendedPlan: StateFlow<RecommendedPlan> = _recommendedPlan.asStateFlow()

    // Dialog state for "Bu gün hansı əzələləri məşq edirsən?" popup
    private val _showTodaySelectionDialog = MutableStateFlow(!todayPrefs.hasAnsweredToday() || todayPrefs.isAlwaysAskOnLaunch())
    val showTodaySelectionDialog: StateFlow<Boolean> = _showTodaySelectionDialog.asStateFlow()

    val historyList: StateFlow<List<WorkoutHistoryEntity>> = historyDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalWorkoutsCount: StateFlow<Int> = historyDao.getTotalWorkoutsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Selected Day in Program (1, 2, 3)
    private val _selectedDayNumber = MutableStateFlow(1)
    val selectedDayNumber: StateFlow<Int> = _selectedDayNumber.asStateFlow()

    // Selected Muscle in Muscle Directory (Sinə, Çiyin, Kürək, Biceps, Triceps, Abs, Ayaq)
    private val _selectedMuscle = MutableStateFlow(MuscleGroup.SINE)
    val selectedMuscle: StateFlow<MuscleGroup> = _selectedMuscle.asStateFlow()

    // Active Exercise for Bottom Sheet Detail & Animation
    private val _inspectedExercise = MutableStateFlow<Exercise?>(null)
    val inspectedExercise: StateFlow<Exercise?> = _inspectedExercise.asStateFlow()

    // Rest Timer Dialog State
    private val _restTimerSeconds = MutableStateFlow<Int?>(null)
    val restTimerSeconds: StateFlow<Int?> = _restTimerSeconds.asStateFlow()

    // Active Workout Mode State
    private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)
    val activeSession: StateFlow<ActiveWorkoutSession?> = _activeSession.asStateFlow()

    // Workout completed success toast or celebration banner
    private val _completedBannerMessage = MutableStateFlow<String?>(null)
    val completedBannerMessage: StateFlow<String?> = _completedBannerMessage.asStateFlow()

    fun selectDay(dayNumber: Int) {
        _selectedDayNumber.value = dayNumber
    }

    fun selectMuscle(muscle: MuscleGroup) {
        _selectedMuscle.value = muscle
    }

    fun inspectExercise(exercise: Exercise?) {
        _inspectedExercise.value = exercise
    }

    fun openRestTimer(seconds: Int = 60) {
        _restTimerSeconds.value = seconds
    }

    fun closeRestTimer() {
        _restTimerSeconds.value = null
    }

    fun dismissCompletedBanner() {
        _completedBannerMessage.value = null
    }

    fun openTodaySelectionDialog() {
        _showTodaySelectionDialog.value = true
    }

    fun closeTodaySelectionDialog() {
        _showTodaySelectionDialog.value = false
    }

    fun applyTodayMuscles(muscles: Set<MuscleGroup>) {
        val finalSet = if (muscles.isEmpty()) setOf(MuscleGroup.SINE, MuscleGroup.TRICEPS) else muscles
        _todayMuscles.value = finalSet
        todayPrefs.saveSelectedMuscles(finalSet)
        _recommendedPlan.value = WorkoutRecommendationEngine.generatePlan(finalSet)
        _showTodaySelectionDialog.value = false
    }

    fun startWorkout(day: WorkoutDay) {
        _activeSession.value = ActiveWorkoutSession(
            dayNumber = day.dayNumber,
            dayTitle = day.nameAz,
            isRunning = true,
            startedTimestamp = System.currentTimeMillis(),
            elapsedSeconds = 0,
            completedSets = emptyMap(),
            totalExercisesInSession = day.exercises.size
        )
    }

    fun startRecommendedWorkout(plan: RecommendedPlan) {
        _activeSession.value = ActiveWorkoutSession(
            dayNumber = 0, // 0 signifies custom / recommended plan
            dayTitle = plan.titleAz,
            isRunning = true,
            startedTimestamp = System.currentTimeMillis(),
            elapsedSeconds = 0,
            completedSets = emptyMap(),
            totalExercisesInSession = plan.exercises.size
        )
    }

    fun toggleSetCompletion(exerciseId: String, setIndex: Int, totalSets: Int) {
        val current = _activeSession.value ?: return
        val currentDone = current.completedSets[exerciseId] ?: 0
        val newDone = if (setIndex < currentDone) setIndex else setIndex + 1
        val updatedMap = current.completedSets.toMutableMap()
        updatedMap[exerciseId] = newDone.coerceIn(0, totalSets)
        _activeSession.value = current.copy(completedSets = updatedMap)

        // Automatically offer rest timer if a set was just marked complete!
        if (newDone > currentDone) {
            openRestTimer(60)
        }
    }

    fun finishWorkout(notes: String = "") {
        val session = _activeSession.value ?: return
        val totalExercises = session.totalExercisesInSession.coerceAtLeast(1)
        val completedCount = session.completedSets.values.count { it > 0 }

        viewModelScope.launch {
            historyDao.insertHistory(
                WorkoutHistoryEntity(
                    dayNumber = session.dayNumber,
                    dayTitle = session.dayTitle,
                    durationSeconds = session.elapsedSeconds.coerceAtLeast(60),
                    completedExercisesCount = completedCount,
                    totalExercisesCount = totalExercises,
                    notes = notes
                )
            )
            _completedBannerMessage.value = "${session.dayTitle} uğurla tamamlandı və yadda saxlanıldı!"
            _activeSession.value = null
        }
    }

    fun cancelActiveWorkout() {
        _activeSession.value = null
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            historyDao.deleteHistory(id)
        }
    }

    fun updateAvatarConfig(newConfig: UserAvatarConfig) {
        _avatarConfig.value = newConfig
        avatarPrefs.saveAvatarConfig(newConfig)
    }

    fun resetAvatarToDefault() {
        val default = avatarPrefs.resetToDefault()
        _avatarConfig.value = default
    }

    // --- Trainer & Student Operations ---
    fun selectStudent(studentId: Long?) {
        _selectedStudentId.value = studentId
    }

    fun selectStudentDay(dayNumber: Int) {
        _selectedStudentDay.value = dayNumber
    }

    fun addStudent(
        fullName: String,
        phone: String,
        currentWeightKg: Float,
        targetWeightKg: Float,
        heightCm: Float,
        goal: String,
        notes: String,
        assignSamplePlan: Boolean = true
    ) {
        viewModelScope.launch {
            val id = trainerRepo.addStudent(
                com.example.data.db.StudentEntity(
                    fullName = fullName.trim(),
                    phone = phone.trim(),
                    startWeightKg = currentWeightKg,
                    currentWeightKg = currentWeightKg,
                    targetWeightKg = targetWeightKg,
                    heightCm = heightCm,
                    goal = goal,
                    notes = notes
                )
            )
            if (assignSamplePlan) {
                trainerRepo.assignSamplePlanToStudent(id)
            }
            _selectedStudentId.value = id
        }
    }

    fun updateStudent(student: com.example.data.db.StudentEntity) {
        viewModelScope.launch {
            trainerRepo.updateStudent(student)
        }
    }

    fun deleteStudent(studentId: Long) {
        viewModelScope.launch {
            if (_selectedStudentId.value == studentId) {
                _selectedStudentId.value = null
            }
            trainerRepo.deleteStudent(studentId)
        }
    }

    fun addExerciseToStudent(
        studentId: Long,
        dayOfWeek: Int,
        dayTitle: String,
        exerciseId: String,
        exerciseName: String,
        muscleGroup: String,
        targetWeightKg: Float,
        targetSets: Int,
        targetReps: Int,
        trainerNotes: String
    ) {
        viewModelScope.launch {
            trainerRepo.addExercise(
                com.example.data.db.StudentWorkoutExerciseEntity(
                    studentId = studentId,
                    dayOfWeek = dayOfWeek,
                    dayTitle = dayTitle,
                    exerciseId = exerciseId,
                    exerciseName = exerciseName,
                    muscleGroup = muscleGroup,
                    targetWeightKg = targetWeightKg,
                    actualWeightKg = targetWeightKg,
                    targetSets = targetSets,
                    targetReps = targetReps,
                    trainerNotes = trainerNotes
                )
            )
        }
    }

    fun updateExerciseWeightAndStatus(
        exerciseId: Long,
        actualWeightKg: Float,
        isCompleted: Boolean,
        studentId: Long,
        exerciseName: String
    ) {
        viewModelScope.launch {
            trainerRepo.updateExerciseWeightAndStatus(
                id = exerciseId,
                actualWeightKg = actualWeightKg,
                isCompleted = isCompleted,
                studentId = studentId,
                exerciseName = exerciseName
            )
        }
    }

    fun deleteStudentExercise(exerciseId: Long) {
        viewModelScope.launch {
            trainerRepo.deleteExercise(exerciseId)
        }
    }

    fun assignSamplePlanToStudent(studentId: Long) {
        viewModelScope.launch {
            trainerRepo.assignSamplePlanToStudent(studentId)
        }
    }

    // --- Role & QR Sync Operations ---
    fun setUserRole(role: com.example.data.datasource.AppUserRole) {
        _userRole.value = role
        rolePrefs.setUserRole(role)
    }

    fun saveTrainerProfile(name: String, specialty: String, phone: String) {
        rolePrefs.saveTrainerProfile(name, specialty, phone)
        _trainerProfile.value = rolePrefs.getTrainerProfile()
    }

    fun saveStudentProfile(
        name: String,
        phone: String,
        currentWeightKg: Float,
        targetWeightKg: Float,
        heightCm: Float,
        goal: String
    ) {
        rolePrefs.saveStudentProfile(name, phone, currentWeightKg, targetWeightKg, heightCm, goal)
        _studentProfile.value = rolePrefs.getStudentProfile()
    }

    fun disconnectTrainer() {
        rolePrefs.disconnectTrainer()
        _connectedTrainer.value = null
        showToast("Məşqçi ilə əlaqə ləğv edildi")
    }

    fun showToast(msg: String) {
        _toastNotification.value = msg
    }

    fun clearToast() {
        _toastNotification.value = null
    }

    fun updateStudentExerciseProgress(
        dayOfWeek: Int,
        exerciseId: String,
        actualWeightKg: Float,
        isCompleted: Boolean
    ) {
        rolePrefs.updateStudentExerciseProgress(dayOfWeek, exerciseId, actualWeightKg, isCompleted)
        _studentAssignedDays.value = rolePrefs.getStudentAssignedWorkouts()
    }

    /**
     * Universal QR Scanner payload dispatcher:
     * Dispatches trainer profiles, student profiles, workout assignments, and student progress reports.
     */
    fun handleScannedQr(rawText: String) {
        val payload = com.example.util.QrCodeUtils.parsePayload(rawText)
        when (payload) {
            is com.example.util.QrPayload.TrainerProfile -> {
                // If scanned, connect this trainer!
                rolePrefs.setConnectedTrainer(payload)
                _connectedTrainer.value = rolePrefs.getConnectedTrainer()
                showToast("Məşqçi ilə uğurla əlaqələndirildi: ${payload.trainerName}")
            }
            is com.example.util.QrPayload.StudentProfile -> {
                // Trainer scanned student's QR code -> add student to database
                viewModelScope.launch {
                    val newId = trainerRepo.addStudent(
                        com.example.data.db.StudentEntity(
                            fullName = payload.fullName,
                            phone = payload.phone,
                            startWeightKg = payload.currentWeightKg,
                            currentWeightKg = payload.currentWeightKg,
                            targetWeightKg = payload.targetWeightKg,
                            heightCm = payload.heightCm,
                            goal = payload.goal,
                            notes = "QR Kod ilə əlavə edildi (${payload.studentId})"
                        )
                    )
                    trainerRepo.assignSamplePlanToStudent(newId)
                    _selectedStudentId.value = newId
                    showToast("Tələbə ${payload.fullName} uğurla qeydiyyata alındı və cədvəl yaradıldı!")
                }
            }
            is com.example.util.QrPayload.WorkoutAssignment -> {
                // Student scanned workout assignment from trainer
                rolePrefs.applyImportedAssignment(payload)
                _studentAssignedDays.value = rolePrefs.getStudentAssignedWorkouts()
                showToast("${payload.trainerName} tərəfindən '${payload.dayTitle}' üçün ${payload.exercises.size} məşq tapşırığı qəbul edildi!")
            }
            is com.example.util.QrPayload.StudentProgressReport -> {
                // Trainer scanned student's progress report QR
                showToast("Tələbə ${payload.studentName} tərəfindən '${payload.dayTitle}' üzrə ${payload.completedCount} məşq nəticəsi qəbul edildi!")
            }
            is com.example.util.QrPayload.Unknown -> {
                showToast("Naməlum QR Kod oxundu: ${payload.rawText.take(30)}")
            }
        }
    }

    // Helper to generate assignment QR payload for current student & day
    fun getAssignmentQrForCurrentStudent(dayOfWeek: Int): String {
        val student = selectedStudent.value ?: return ""
        val exercises = currentStudentDayExercises.value
        val dayTitle = com.example.data.datasource.FitnessData.threeDayProgram.firstOrNull { it.dayNumber == dayOfWeek }?.nameAz
            ?: "$dayOfWeek-ci Gün Məşqi"

        val items = exercises.map { ex ->
            com.example.util.AssignedExerciseItem(
                exerciseId = ex.exerciseId,
                exerciseName = ex.exerciseName,
                muscleGroup = ex.muscleGroup,
                targetWeightKg = ex.targetWeightKg,
                targetSets = ex.targetSets,
                targetReps = ex.targetReps,
                trainerNotes = ex.trainerNotes
            )
        }

        val assignment = com.example.util.QrPayload.WorkoutAssignment(
            assignmentId = "TASK-${student.id}-$dayOfWeek-${System.currentTimeMillis() % 10000}",
            trainerName = _trainerProfile.value.trainerName,
            studentName = student.fullName,
            dayOfWeek = dayOfWeek,
            dayTitle = dayTitle,
            exercises = items
        )
        return com.example.util.QrCodeUtils.encodeWorkoutAssignment(assignment)
    }

    // Helper to generate student progress report QR payload
    fun getStudentProgressQr(dayOfWeek: Int): String {
        val days = _studentAssignedDays.value
        val day = days.firstOrNull { it.dayOfWeek == dayOfWeek } ?: return ""
        val student = _studentProfile.value

        val results = day.exercises.map { ex ->
            com.example.util.ProgressExerciseItem(
                exerciseName = ex.exerciseName,
                actualWeightKg = ex.actualWeightKg,
                isCompleted = ex.isCompleted
            )
        }

        val report = com.example.util.QrPayload.StudentProgressReport(
            reportId = "PROG-${student.studentId}-$dayOfWeek-${System.currentTimeMillis() % 10000}",
            studentName = student.fullName,
            dayOfWeek = dayOfWeek,
            dayTitle = day.dayTitle,
            completedCount = results.count { it.isCompleted },
            results = results
        )
        return com.example.util.QrCodeUtils.encodeStudentProgress(report)
    }
}
