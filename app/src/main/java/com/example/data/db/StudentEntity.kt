package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phone: String = "",
    val startWeightKg: Float = 75f,
    val currentWeightKg: Float = 75f,
    val targetWeightKg: Float = 80f,
    val heightCm: Float = 178f,
    val goal: String = "Əzələ Kütləsi",
    val notes: String = "",
    val registrationTimestamp: Long = System.currentTimeMillis()
)
