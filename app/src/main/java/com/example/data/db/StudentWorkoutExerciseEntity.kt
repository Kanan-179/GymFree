package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_workout_exercises")
data class StudentWorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val dayOfWeek: Int, // 1: Bazar ertəsi, 2: Çərşənbə axşamı, 3: Çərşənbə, 4: Cümə axşamı, 5: Cümə, 6: Şənbə, 7: Bazar
    val dayTitle: String,
    val exerciseId: String,
    val exerciseName: String,
    val muscleGroup: String = "Ümumi",
    val targetWeightKg: Float, // Müəllimin təyin etdiyi hədəf çəki
    val actualWeightKg: Float, // Tələbənin qaldırdığı çəki
    val targetSets: Int = 4,
    val targetReps: Int = 10,
    val isCompleted: Boolean = false,
    val trainerNotes: String = "",
    val orderIndex: Int = 0
)
