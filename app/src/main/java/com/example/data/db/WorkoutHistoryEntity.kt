package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayNumber: Int,
    val dayTitle: String,
    val completedTimestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val completedExercisesCount: Int,
    val totalExercisesCount: Int,
    val notes: String = ""
)
