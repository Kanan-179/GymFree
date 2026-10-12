package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerDao {

    // Students Management
    @Query("SELECT * FROM students ORDER BY registrationTimestamp DESC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    fun getStudentById(id: Long): Flow<StudentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("DELETE FROM students WHERE id = :studentId")
    suspend fun deleteStudentById(studentId: Long)

    // Student Exercises & Days
    @Query("SELECT * FROM student_workout_exercises WHERE studentId = :studentId AND dayOfWeek = :dayOfWeek ORDER BY orderIndex ASC, id ASC")
    fun getExercisesForStudentAndDay(studentId: Long, dayOfWeek: Int): Flow<List<StudentWorkoutExerciseEntity>>

    @Query("SELECT * FROM student_workout_exercises WHERE studentId = :studentId ORDER BY dayOfWeek ASC, orderIndex ASC")
    fun getAllExercisesForStudent(studentId: Long): Flow<List<StudentWorkoutExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentExercise(exercise: StudentWorkoutExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentExercises(exercises: List<StudentWorkoutExerciseEntity>)

    @Update
    suspend fun updateStudentExercise(exercise: StudentWorkoutExerciseEntity)

    @Query("DELETE FROM student_workout_exercises WHERE id = :id")
    suspend fun deleteStudentExercise(id: Long)

    @Query("DELETE FROM student_workout_exercises WHERE studentId = :studentId")
    suspend fun deleteAllExercisesForStudent(studentId: Long)

    @Query("UPDATE student_workout_exercises SET actualWeightKg = :actualWeightKg, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateExerciseWeightAndStatus(id: Long, actualWeightKg: Float, isCompleted: Boolean)

    // Student Weight History Logs
    @Query("SELECT * FROM student_weight_logs WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getWeightLogsForStudent(studentId: Long): Flow<List<StudentWeightLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(log: StudentWeightLogEntity): Long
}
