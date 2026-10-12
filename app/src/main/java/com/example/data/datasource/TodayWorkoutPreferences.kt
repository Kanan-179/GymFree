package com.example.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.MuscleGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodayWorkoutPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("today_workout_prefs", Context.MODE_PRIVATE)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getTodayDateString(): String = dateFormat.format(Date())

    fun hasAnsweredToday(): Boolean {
        val lastDate = prefs.getString("last_answered_date", "") ?: ""
        return lastDate == getTodayDateString()
    }

    fun markAnsweredToday() {
        prefs.edit().putString("last_answered_date", getTodayDateString()).apply()
    }

    fun resetAnsweredToday() {
        prefs.edit().remove("last_answered_date").apply()
    }

    fun getSavedMuscles(): Set<MuscleGroup> {
        val rawSet = prefs.getStringSet("selected_muscles", null)
        if (rawSet.isNullOrEmpty()) {
            return setOf(MuscleGroup.SINE, MuscleGroup.TRICEPS)
        }
        return rawSet.mapNotNull { id ->
            MuscleGroup.entries.firstOrNull { it.id == id }
        }.toSet().ifEmpty { setOf(MuscleGroup.SINE, MuscleGroup.TRICEPS) }
    }

    fun saveSelectedMuscles(muscles: Set<MuscleGroup>) {
        val idSet = muscles.map { it.id }.toSet()
        prefs.edit()
            .putStringSet("selected_muscles", idSet)
            .putString("last_answered_date", getTodayDateString())
            .apply()
    }

    fun isAlwaysAskOnLaunch(): Boolean {
        return prefs.getBoolean("always_ask_on_launch", true)
    }

    fun setAlwaysAskOnLaunch(alwaysAsk: Boolean) {
        prefs.edit().putBoolean("always_ask_on_launch", alwaysAsk).apply()
    }
}
