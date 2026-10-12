package com.example.data.repository

import com.example.data.db.StudentEntity
import com.example.data.db.StudentWeightLogEntity
import com.example.data.db.StudentWorkoutExerciseEntity
import com.example.data.db.TrainerDao
import kotlinx.coroutines.flow.Flow

class TrainerRepository(private val trainerDao: TrainerDao) {

    val allStudents: Flow<List<StudentEntity>> = trainerDao.getAllStudents()

    fun getStudentById(studentId: Long): Flow<StudentEntity?> {
        return trainerDao.getStudentById(studentId)
    }

    suspend fun addStudent(student: StudentEntity): Long {
        return trainerDao.insertStudent(student)
    }

    suspend fun updateStudent(student: StudentEntity) {
        trainerDao.updateStudent(student)
    }

    suspend fun deleteStudent(studentId: Long) {
        trainerDao.deleteAllExercisesForStudent(studentId)
        trainerDao.deleteStudentById(studentId)
    }

    fun getExercisesForStudentAndDay(studentId: Long, dayOfWeek: Int): Flow<List<StudentWorkoutExerciseEntity>> {
        return trainerDao.getExercisesForStudentAndDay(studentId, dayOfWeek)
    }

    fun getAllExercisesForStudent(studentId: Long): Flow<List<StudentWorkoutExerciseEntity>> {
        return trainerDao.getAllExercisesForStudent(studentId)
    }

    suspend fun addExercise(exercise: StudentWorkoutExerciseEntity): Long {
        return trainerDao.insertStudentExercise(exercise)
    }

    suspend fun updateExercise(exercise: StudentWorkoutExerciseEntity) {
        trainerDao.updateStudentExercise(exercise)
    }

    suspend fun deleteExercise(exerciseId: Long) {
        trainerDao.deleteStudentExercise(exerciseId)
    }

    suspend fun updateExerciseWeightAndStatus(
        id: Long,
        actualWeightKg: Float,
        isCompleted: Boolean,
        studentId: Long,
        exerciseName: String
    ) {
        trainerDao.updateExerciseWeightAndStatus(id, actualWeightKg, isCompleted)
        if (isCompleted && actualWeightKg > 0) {
            trainerDao.insertWeightLog(
                StudentWeightLogEntity(
                    studentId = studentId,
                    exerciseName = exerciseName,
                    weightKg = actualWeightKg,
                    reps = 10,
                    note = "Məşq qeydi"
                )
            )
        }
    }

    fun getWeightLogs(studentId: Long): Flow<List<StudentWeightLogEntity>> {
        return trainerDao.getWeightLogsForStudent(studentId)
    }

    suspend fun assignSamplePlanToStudent(studentId: Long) {
        val sampleList = listOf(
            // 1-ci Gün: Bazar ertəsi - Sinə & Triseps
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 1,
                dayTitle = "1-ci Gün: Sinə & Triseps",
                exerciseId = "bench_press",
                exerciseName = "Ştanqla Uzanaraq Sinə Presi",
                muscleGroup = "Sinə",
                targetWeightKg = 60f,
                actualWeightKg = 60f,
                targetSets = 4,
                targetReps = 10,
                trainerNotes = "Dirsəkləri 45 dərəcə saxla, döş qəfəsinə toxundur"
            ),
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 1,
                dayTitle = "1-ci Gün: Sinə & Triseps",
                exerciseId = "incline_dumbbell_press",
                exerciseName = "Maili Skamyada Qantel Presi",
                muscleGroup = "Sinə",
                targetWeightKg = 20f,
                actualWeightKg = 20f,
                targetSets = 3,
                targetReps = 12,
                trainerNotes = "Yuxarı sinəni sıx"
            ),
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 1,
                dayTitle = "1-ci Gün: Sinə & Triseps",
                exerciseId = "tricep_rope_pushdown",
                exerciseName = "Kəndirlə Triseps Açma",
                muscleGroup = "Triseps",
                targetWeightKg = 25f,
                actualWeightKg = 25f,
                targetSets = 3,
                targetReps = 12,
                trainerNotes = "Aşağıda kəndiri yanlara aç"
            ),

            // 2-ci Gün: Çərşənbə - Arxa & Biseps
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 3,
                dayTitle = "2-ci Gün: Arxa & Biseps",
                exerciseId = "lat_pulldown",
                exerciseName = "Blokda Sinəyə Dartma (Lat Pulldown)",
                muscleGroup = "Kürək",
                targetWeightKg = 50f,
                actualWeightKg = 50f,
                targetSets = 4,
                targetReps = 10,
                trainerNotes = "Kürək əzələlərini sıx, geriyə çox əyilmə"
            ),
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 3,
                dayTitle = "2-ci Gün: Arxa & Biseps",
                exerciseId = "bicep_barbell_curl",
                exerciseName = "Ştanqla Ayaqüstə Biseps Bükmə",
                muscleGroup = "Biseps",
                targetWeightKg = 30f,
                actualWeightKg = 30f,
                targetSets = 3,
                targetReps = 10,
                trainerNotes = "Bədəni yelləmə, dirsəkləri sabit saxla"
            ),

            // 3-cü Gün: Cümə - Ayaq & Çiyin
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 5,
                dayTitle = "3-cü Gün: Ayaq & Çiyin",
                exerciseId = "squat",
                exerciseName = "Ştanqla Çöməlmə (Squat)",
                muscleGroup = "Ayaq",
                targetWeightKg = 70f,
                actualWeightKg = 70f,
                targetSets = 4,
                targetReps = 8,
                trainerNotes = "Bel düz, dizlər barmaq ucunu keçməsin"
            ),
            StudentWorkoutExerciseEntity(
                studentId = studentId,
                dayOfWeek = 5,
                dayTitle = "3-cü Gün: Ayaq & Çiyin",
                exerciseId = "dumbbell_shoulder_press",
                exerciseName = "Qantellə Oturaraq Çiyin Presi",
                muscleGroup = "Çiyin",
                targetWeightKg = 18f,
                actualWeightKg = 18f,
                targetSets = 4,
                targetReps = 10,
                trainerNotes = "Nəzarətli endir, yuxarıda bir saniyə saxla"
            )
        )
        trainerDao.insertStudentExercises(sampleList)
    }
}
