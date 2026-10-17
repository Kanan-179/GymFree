package com.example.util

import com.example.data.db.WorkoutHistoryEntity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * openGym inspired Data Portability and Backup Utility.
 * Export and import workout logs, volume history, and routines in JSON & CSV (FitNotes style).
 */
object WorkoutDataBackupUtils {

    fun exportHistoryToJson(historyList: List<WorkoutHistoryEntity>): String {
        val root = JSONObject()
        root.put("version", 1)
        root.put("exportedAt", System.currentTimeMillis())
        root.put("appName", "FitMəşq openGym Edition")
        root.put("totalWorkouts", historyList.size)

        val array = JSONArray()
        for (item in historyList) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("dayNumber", item.dayNumber)
            obj.put("dayTitle", item.dayTitle)
            obj.put("completedTimestamp", item.completedTimestamp)
            obj.put("durationSeconds", item.durationSeconds)
            obj.put("completedExercisesCount", item.completedExercisesCount)
            obj.put("totalExercisesCount", item.totalExercisesCount)
            obj.put("notes", item.notes)
            obj.put("totalVolumeKg", item.totalVolumeKg)
            obj.put("trainedMusclesJson", item.trainedMusclesJson)
            array.put(obj)
        }
        root.put("workouts", array)

        return root.toString(2)
    }

    fun exportHistoryToCsv(historyList: List<WorkoutHistoryEntity>): String {
        val sb = StringBuilder()
        // FitNotes / standard CSV header
        sb.append("Tarix,Məşq Günü,Müddət (dəq),Tamamlanan Hərəkətlər,Ümumi Hərəkətlər,Ümumi Tonaj (kq),Qeydlər\n")

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        for (item in historyList) {
            val dateStr = sdf.format(Date(item.completedTimestamp))
            val durationMin = item.durationSeconds / 60
            val escapedTitle = item.dayTitle.replace(",", " ")
            val escapedNotes = item.notes.replace(",", " ").replace("\n", " ")
            sb.append("$dateStr,$escapedTitle,$durationMin,${item.completedExercisesCount},${item.totalExercisesCount},${item.totalVolumeKg},$escapedNotes\n")
        }

        return sb.toString()
    }

    fun parseBackupJson(jsonString: String): List<WorkoutHistoryEntity> {
        val results = mutableListOf<WorkoutHistoryEntity>()
        try {
            val root = JSONObject(jsonString)
            val array = root.optJSONArray("workouts") ?: JSONArray()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                results.add(
                    WorkoutHistoryEntity(
                        id = 0, // auto-generate new ID on restore
                        dayNumber = obj.optInt("dayNumber", 1),
                        dayTitle = obj.optString("dayTitle", "Məşq"),
                        completedTimestamp = obj.optLong("completedTimestamp", System.currentTimeMillis()),
                        durationSeconds = obj.optInt("durationSeconds", 1800),
                        completedExercisesCount = obj.optInt("completedExercisesCount", 0),
                        totalExercisesCount = obj.optInt("totalExercisesCount", 6),
                        notes = obj.optString("notes", ""),
                        totalVolumeKg = obj.optDouble("totalVolumeKg", 0.0),
                        trainedMusclesJson = obj.optString("trainedMusclesJson", "")
                    )
                )
            }
        } catch (_: Exception) {
            // return empty or partial
        }
        return results
    }
}
