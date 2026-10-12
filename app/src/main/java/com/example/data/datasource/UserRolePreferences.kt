package com.example.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.util.AssignedExerciseItem
import com.example.util.QrPayload
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppUserRole {
    TRAINER,
    STUDENT
}

data class ConnectedTrainerInfo(
    val trainerId: String,
    val trainerName: String,
    val specialty: String,
    val connectedDate: String
)

data class StudentAssignedDay(
    val dayOfWeek: Int,
    val dayTitle: String,
    val trainerName: String,
    val exercises: List<StudentAssignedExercise>
)

data class StudentAssignedExercise(
    val id: String,
    val exerciseName: String,
    val muscleGroup: String,
    val targetWeightKg: Float,
    val actualWeightKg: Float,
    val targetSets: Int,
    val targetReps: Int,
    val trainerNotes: String,
    val isCompleted: Boolean = false
)

class UserRolePreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("fitmesq_role_prefs", Context.MODE_PRIVATE)

    fun getUserRole(): AppUserRole {
        val roleStr = prefs.getString("user_role", AppUserRole.TRAINER.name) ?: AppUserRole.TRAINER.name
        return try {
            AppUserRole.valueOf(roleStr)
        } catch (e: Exception) {
            AppUserRole.TRAINER
        }
    }

    fun setUserRole(role: AppUserRole) {
        prefs.edit().putString("user_role", role.name).apply()
    }

    // --- Trainer Identity (If user is a Trainer) ---
    fun getTrainerProfile(): QrPayload.TrainerProfile {
        val id = prefs.getString("my_trainer_id", "TR-7721") ?: "TR-7721"
        val name = prefs.getString("my_trainer_name", "Kənan Müəllim") ?: "Kənan Müəllim"
        val spec = prefs.getString("my_trainer_spec", "Baş Məşqçi & Bodibildinq") ?: "Baş Məşqçi & Bodibildinq"
        val phone = prefs.getString("my_trainer_phone", "+994 50 123 45 67") ?: "+994 50 123 45 67"
        return QrPayload.TrainerProfile(id, name, spec, phone)
    }

    fun saveTrainerProfile(name: String, specialty: String, phone: String) {
        prefs.edit()
            .putString("my_trainer_name", name.trim())
            .putString("my_trainer_spec", specialty.trim())
            .putString("my_trainer_phone", phone.trim())
            .apply()
    }

    // --- Student Identity (If user is a Student) ---
    fun getStudentProfile(): QrPayload.StudentProfile {
        val id = prefs.getString("my_student_id", "ST-${(System.currentTimeMillis() % 9000) + 1000}") ?: "ST-1001"
        val name = prefs.getString("my_student_name", "Tələbə İstifadəçi") ?: "Tələbə İstifadəçi"
        val phone = prefs.getString("my_student_phone", "") ?: ""
        val curW = prefs.getFloat("my_student_cur_w", 76f)
        val tarW = prefs.getFloat("my_student_tar_w", 82f)
        val h = prefs.getFloat("my_student_height", 180f)
        val goal = prefs.getString("my_student_goal", "Əzələ Kütləsi & Güc") ?: "Əzələ Kütləsi & Güc"
        val notes = prefs.getString("my_student_notes", "") ?: ""
        return QrPayload.StudentProfile(id, name, phone, curW, tarW, h, goal, notes)
    }

    fun saveStudentProfile(
        name: String,
        phone: String,
        currentWeightKg: Float,
        targetWeightKg: Float,
        heightCm: Float,
        goal: String
    ) {
        prefs.edit()
            .putString("my_student_name", name.trim())
            .putString("my_student_phone", phone.trim())
            .putFloat("my_student_cur_w", currentWeightKg)
            .putFloat("my_student_tar_w", targetWeightKg)
            .putFloat("my_student_height", heightCm)
            .putString("my_student_goal", goal.trim())
            .apply()
    }

    // --- Connected Trainer (When in Student mode) ---
    fun getConnectedTrainer(): ConnectedTrainerInfo? {
        val id = prefs.getString("conn_trainer_id", null) ?: return null
        val name = prefs.getString("conn_trainer_name", "Məşqçi") ?: "Məşqçi"
        val spec = prefs.getString("conn_trainer_spec", "Fitness") ?: "Fitness"
        val date = prefs.getString("conn_trainer_date", "Bu gün") ?: "Bu gün"
        return ConnectedTrainerInfo(id, name, spec, date)
    }

    fun setConnectedTrainer(trainer: QrPayload.TrainerProfile) {
        val dateStr = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        prefs.edit()
            .putString("conn_trainer_id", trainer.trainerId)
            .putString("conn_trainer_name", trainer.trainerName)
            .putString("conn_trainer_spec", trainer.specialty)
            .putString("conn_trainer_date", dateStr)
            .apply()
    }

    fun disconnectTrainer() {
        prefs.edit()
            .remove("conn_trainer_id")
            .remove("conn_trainer_name")
            .remove("conn_trainer_spec")
            .remove("conn_trainer_date")
            .apply()
    }

    // --- Student's Assigned Workouts from Trainer ---
    fun getStudentAssignedWorkouts(): List<StudentAssignedDay> {
        val raw = prefs.getString("student_assigned_workouts_json", null) ?: return getDefaultDemoAssignedWorkouts()
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<StudentAssignedDay>()
            for (i in 0 until arr.length()) {
                val dayObj = arr.getJSONObject(i)
                val exArr = dayObj.getJSONArray("exercises")
                val exList = mutableListOf<StudentAssignedExercise>()
                for (j in 0 until exArr.length()) {
                    val exObj = exArr.getJSONObject(j)
                    exList.add(
                        StudentAssignedExercise(
                            id = exObj.optString("id", "ex_$j"),
                            exerciseName = exObj.optString("name", "Hərəkət"),
                            muscleGroup = exObj.optString("muscle", "Sinə"),
                            targetWeightKg = exObj.optDouble("targetW", 50.0).toFloat(),
                            actualWeightKg = exObj.optDouble("actualW", 50.0).toFloat(),
                            targetSets = exObj.optInt("sets", 4),
                            targetReps = exObj.optInt("reps", 10),
                            trainerNotes = exObj.optString("notes", ""),
                            isCompleted = exObj.optBoolean("done", false)
                        )
                    )
                }
                list.add(
                    StudentAssignedDay(
                        dayOfWeek = dayObj.optInt("day", 1),
                        dayTitle = dayObj.optString("title", "Məşq Günü"),
                        trainerName = dayObj.optString("trainer", "Məşqçi"),
                        exercises = exList
                    )
                )
            }
            list
        } catch (e: Exception) {
            getDefaultDemoAssignedWorkouts()
        }
    }

    fun saveStudentAssignedWorkouts(days: List<StudentAssignedDay>) {
        val arr = JSONArray()
        days.forEach { day ->
            val dayObj = JSONObject().apply {
                put("day", day.dayOfWeek)
                put("title", day.dayTitle)
                put("trainer", day.trainerName)
                val exArr = JSONArray()
                day.exercises.forEach { ex ->
                    val exObj = JSONObject().apply {
                        put("id", ex.id)
                        put("name", ex.exerciseName)
                        put("muscle", ex.muscleGroup)
                        put("targetW", ex.targetWeightKg.toDouble())
                        put("actualW", ex.actualWeightKg.toDouble())
                        put("sets", ex.targetSets)
                        put("reps", ex.targetReps)
                        put("notes", ex.trainerNotes)
                        put("done", ex.isCompleted)
                    }
                    exArr.put(exObj)
                }
                put("exercises", exArr)
            }
            arr.put(dayObj)
        }
        prefs.edit().putString("student_assigned_workouts_json", arr.toString()).apply()
    }

    fun applyImportedAssignment(assignment: QrPayload.WorkoutAssignment): Boolean {
        val currentDays = getStudentAssignedWorkouts().toMutableList()
        val mappedExercises = assignment.exercises.map { item ->
            StudentAssignedExercise(
                id = item.exerciseId,
                exerciseName = item.exerciseName,
                muscleGroup = item.muscleGroup,
                targetWeightKg = item.targetWeightKg,
                actualWeightKg = item.targetWeightKg,
                targetSets = item.targetSets,
                targetReps = item.targetReps,
                trainerNotes = item.trainerNotes,
                isCompleted = false
            )
        }

        val existingIdx = currentDays.indexOfFirst { it.dayOfWeek == assignment.dayOfWeek }
        val newDay = StudentAssignedDay(
            dayOfWeek = assignment.dayOfWeek,
            dayTitle = assignment.dayTitle,
            trainerName = assignment.trainerName,
            exercises = mappedExercises
        )

        if (existingIdx >= 0) {
            currentDays[existingIdx] = newDay
        } else {
            currentDays.add(newDay)
            currentDays.sortBy { it.dayOfWeek }
        }

        saveStudentAssignedWorkouts(currentDays)
        return true
    }

    fun updateStudentExerciseProgress(
        dayOfWeek: Int,
        exerciseId: String,
        actualWeightKg: Float,
        isCompleted: Boolean
    ) {
        val currentDays = getStudentAssignedWorkouts().toMutableList()
        val dayIdx = currentDays.indexOfFirst { it.dayOfWeek == dayOfWeek }
        if (dayIdx >= 0) {
            val day = currentDays[dayIdx]
            val updatedExList = day.exercises.map { ex ->
                if (ex.id == exerciseId) {
                    ex.copy(actualWeightKg = actualWeightKg, isCompleted = isCompleted)
                } else ex
            }
            currentDays[dayIdx] = day.copy(exercises = updatedExList)
            saveStudentAssignedWorkouts(currentDays)
        }
    }

    private fun getDefaultDemoAssignedWorkouts(): List<StudentAssignedDay> {
        return listOf(
            StudentAssignedDay(
                dayOfWeek = 1,
                dayTitle = "1-ci Gün: Sinə & Triceps",
                trainerName = "Kənan Müəllim",
                exercises = listOf(
                    StudentAssignedExercise(
                        id = "bench_press",
                        exerciseName = "Barbell Bench Press",
                        muscleGroup = "Sinə",
                        targetWeightKg = 60f,
                        actualWeightKg = 60f,
                        targetSets = 4,
                        targetReps = 10,
                        trainerNotes = "Dirsəkləri bədənə 45 dərəcə bucaq altında saxla, amplitudan tam olsun.",
                        isCompleted = false
                    ),
                    StudentAssignedExercise(
                        id = "incline_dumbbell_press",
                        exerciseName = "Incline Dumbbell Press",
                        muscleGroup = "Sinə",
                        targetWeightKg = 22f,
                        actualWeightKg = 22f,
                        targetSets = 4,
                        targetReps = 10,
                        trainerNotes = "Yuxarı sinəni sıx, dambılları yuxarıda bir-birinə çırpma.",
                        isCompleted = false
                    ),
                    StudentAssignedExercise(
                        id = "triceps_pushdown",
                        exerciseName = "Triceps Cable Pushdown",
                        muscleGroup = "Triceps",
                        targetWeightKg = 25f,
                        actualWeightKg = 25f,
                        targetSets = 3,
                        targetReps = 12,
                        trainerNotes = "Qolları sabit saxla, yalnız dirsəklərdən hərəkət et.",
                        isCompleted = false
                    )
                )
            ),
            StudentAssignedDay(
                dayOfWeek = 2,
                dayTitle = "2-ci Gün: Kürək & Biceps",
                trainerName = "Kənan Müəllim",
                exercises = listOf(
                    StudentAssignedExercise(
                        id = "lat_pulldown",
                        exerciseName = "Lat Pulldown (Geniş Tutuş)",
                        muscleGroup = "Kürək",
                        targetWeightKg = 55f,
                        actualWeightKg = 55f,
                        targetSets = 4,
                        targetReps = 10,
                        trainerNotes = "Kürək əzələlərini tam yığ, çiyinləri aşağı sıx.",
                        isCompleted = false
                    ),
                    StudentAssignedExercise(
                        id = "barbell_curl",
                        exerciseName = "Barbell Biceps Curl",
                        muscleGroup = "Biceps",
                        targetWeightKg = 30f,
                        actualWeightKg = 30f,
                        targetSets = 3,
                        targetReps = 12,
                        trainerNotes = "İrəli-geri yellənmədən, təmiz texnika ilə qaldır.",
                        isCompleted = false
                    )
                )
            )
        )
    }
}
