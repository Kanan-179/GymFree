package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_weight_logs")
data class StudentWeightLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val exerciseName: String,
    val weightKg: Float,
    val reps: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)
