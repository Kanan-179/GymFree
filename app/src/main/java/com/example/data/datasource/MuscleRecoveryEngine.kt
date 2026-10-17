package com.example.data.datasource

import com.example.data.db.WorkoutHistoryEntity
import com.example.data.model.BodyRecoveryAnalysis
import com.example.data.model.MuscleGroup
import com.example.data.model.MuscleRecoveryState
import com.example.data.model.MuscleRecoveryStatus
import org.json.JSONArray

/**
 * openGym inspired Anatomical Muscle Recovery and Tissue Fatigue Engine.
 * Analyzes previous workout history to calculate live recovery percentage,
 * tissue readiness, and optimal muscle workout scheduling in Azerbaijani.
 */
object MuscleRecoveryEngine {

    fun analyzeRecovery(
        historyList: List<WorkoutHistoryEntity>,
        currentTimeMs: Long = System.currentTimeMillis()
    ): BodyRecoveryAnalysis {
        val allMuscles = MuscleGroup.entries

        val muscleStates = allMuscles.map { muscle ->
            calculateMuscleState(muscle, historyList, currentTimeMs)
        }

        // Calculate overall body readiness score (0-100)
        val overallScore = if (muscleStates.isNotEmpty()) {
            val sum = muscleStates.sumOf { it.recoveryPercent }
            (sum / muscleStates.size).coerceIn(10, 100)
        } else {
            100
        }

        val recommendedMuscles = muscleStates
            .filter { it.status == MuscleRecoveryStatus.OPTIMAL || it.status == MuscleRecoveryStatus.DETRAINED }
            .map { it.muscle }

        val musclesNeedingRest = muscleStates
            .filter { it.status == MuscleRecoveryStatus.FATIGUED || it.status == MuscleRecoveryStatus.RECOVERING }
            .map { it.muscle }

        return BodyRecoveryAnalysis(
            overallReadinessScore = overallScore,
            muscleStates = muscleStates,
            recommendedMuscles = recommendedMuscles,
            musclesNeedingRest = musclesNeedingRest
        )
    }

    private fun calculateMuscleState(
        muscle: MuscleGroup,
        historyList: List<WorkoutHistoryEntity>,
        currentTimeMs: Long
    ): MuscleRecoveryState {
        // Find latest workout that trained this muscle
        val lastWorkout = historyList
            .filter { workoutEngagedMuscle(it, muscle) }
            .maxByOrNull { it.completedTimestamp }

        if (lastWorkout == null) {
            return MuscleRecoveryState(
                muscle = muscle,
                recoveryPercent = 100,
                status = MuscleRecoveryStatus.DETRAINED,
                lastTrainedHoursAgo = null,
                lastTrainedTimestamp = null,
                lastTrainedDateStr = "Məşq qeydi yoxdur",
                recommendationAz = "${muscle.nameAz} əzələsi üçün hələ məşq qeydə alınmayıb. Bu gün məşqə daxil etmək tamamilə təhlükəsiz və faydalıdır."
            )
        }

        val elapsedMs = (currentTimeMs - lastWorkout.completedTimestamp).coerceAtLeast(0L)
        val elapsedHours = elapsedMs / (1000 * 60 * 60)
        val elapsedDays = elapsedHours / 24

        val dateStr = when {
            elapsedHours < 1 -> "İndicə məşq olundu"
            elapsedHours < 24 -> "$elapsedHours saat əvvəl"
            elapsedDays == 1L -> "Dünən (~$elapsedHours saat əvvəl)"
            elapsedDays < 7 -> "$elapsedDays gün əvvəl"
            else -> "${elapsedDays / 7} həftə əvvəl"
        }

        return when {
            // First 24 hours: Heavy fatigue & muscle protein breakdown/synthesis peak
            elapsedHours < 24 -> {
                val percent = (25 + (elapsedHours * 1.0f)).toInt().coerceIn(25, 48)
                MuscleRecoveryState(
                    muscle = muscle,
                    recoveryPercent = percent,
                    status = MuscleRecoveryStatus.FATIGUED,
                    lastTrainedHoursAgo = elapsedHours,
                    lastTrainedTimestamp = lastWorkout.completedTimestamp,
                    lastTrainedDateStr = dateStr,
                    recommendationAz = "${muscle.nameAz} intensiv bərpa fazasındadır. Mikrozədələrin sağalması üçün bu gün bu əzələyə tam istirahət verin."
                )
            }
            // 24 to 48 hours: Glycogen restoring, entering supercompensation
            elapsedHours < 48 -> {
                val progressFrom24 = (elapsedHours - 24).toFloat() / 24f
                val percent = (50 + (progressFrom24 * 35)).toInt().coerceIn(50, 85)
                MuscleRecoveryState(
                    muscle = muscle,
                    recoveryPercent = percent,
                    status = MuscleRecoveryStatus.RECOVERING,
                    lastTrainedHoursAgo = elapsedHours,
                    lastTrainedTimestamp = lastWorkout.completedTimestamp,
                    lastTrainedDateStr = dateStr,
                    recommendationAz = "${muscle.nameAz} əzələsində bərpa yekunlaşmaq üzrədir (${percent}%). Sabah tam güclə məşqə hazır olacaq."
                )
            }
            // 48 to 144 hours (2 - 6 days): Optimal supercompensation window
            elapsedHours <= 144 -> {
                MuscleRecoveryState(
                    muscle = muscle,
                    recoveryPercent = 100,
                    status = MuscleRecoveryStatus.OPTIMAL,
                    lastTrainedHoursAgo = elapsedHours,
                    lastTrainedTimestamp = lastWorkout.completedTimestamp,
                    lastTrainedDateStr = dateStr,
                    recommendationAz = "${muscle.nameAz} 100% bərpa olunub və super-kompensasiya pikindədir! Bu gün məşq etmək üçün ən ideal vaxtdır."
                )
            }
            // More than 6 days (detrained / unworked stimulus needed)
            else -> {
                MuscleRecoveryState(
                    muscle = muscle,
                    recoveryPercent = 100,
                    status = MuscleRecoveryStatus.DETRAINED,
                    lastTrainedHoursAgo = elapsedHours,
                    lastTrainedTimestamp = lastWorkout.completedTimestamp,
                    lastTrainedDateStr = dateStr,
                    recommendationAz = "${muscle.nameAz} son dəfə $dateStr məşq olunub. Əzələ tonusunu və adaptasiyasını qorumaq üçün yeni stimul tövsiyə edilir."
                )
            }
        }
    }

    private fun workoutEngagedMuscle(workout: WorkoutHistoryEntity, muscle: MuscleGroup): Boolean {
        // If workout explicitly stored trained muscles json
        if (workout.trainedMusclesJson.isNotBlank()) {
            try {
                val array = JSONArray(workout.trainedMusclesJson)
                for (i in 0 until array.length()) {
                    if (array.getString(i).equals(muscle.id, ignoreCase = true)) {
                        return true
                    }
                }
            } catch (_: Exception) {
                // fallback to standard mapping
            }
        }

        // Standard 3-Day Program mapping
        return when (workout.dayNumber) {
            1 -> muscle == MuscleGroup.SINE || muscle == MuscleGroup.CIYIN || muscle == MuscleGroup.TRICEPS
            2 -> muscle == MuscleGroup.KUREK || muscle == MuscleGroup.BICEPS || muscle == MuscleGroup.CIYIN
            3 -> muscle == MuscleGroup.AYAQ || muscle == MuscleGroup.ABS
            else -> {
                // Custom / Recommended plan: check keywords in title
                val title = workout.dayTitle.lowercase()
                when (muscle) {
                    MuscleGroup.SINE -> title.contains("sinə") || title.contains("chest")
                    MuscleGroup.CIYIN -> title.contains("çiyin") || title.contains("shoulder")
                    MuscleGroup.KUREK -> title.contains("kürək") || title.contains("back")
                    MuscleGroup.BICEPS -> title.contains("biceps") || title.contains("qol")
                    MuscleGroup.TRICEPS -> title.contains("triceps")
                    MuscleGroup.ABS -> title.contains("abs") || title.contains("qarın")
                    MuscleGroup.AYAQ -> title.contains("ayaq") || title.contains("leg")
                }
            }
        }
    }
}
