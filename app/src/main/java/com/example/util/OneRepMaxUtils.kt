package com.example.util

import com.example.data.model.OneRepMaxEstimate
import com.example.data.model.OneRepMaxPercentRow
import kotlin.math.roundToInt

/**
 * openGym inspired 1RM (One Rep Max) Strength Calculator.
 * Calculates estimated maximum 1-rep load using Epley & Brzycki formulas
 * and produces load breakdowns for strength, hypertrophy, and endurance.
 */
object OneRepMaxUtils {

    fun calculate1Rm(weightKg: Float, reps: Int): OneRepMaxEstimate {
        val safeWeight = weightKg.coerceAtLeast(1f)
        val safeReps = reps.coerceIn(1, 30)

        // Epley Formula: 1RM = W * (1 + R / 30)
        val epley = safeWeight * (1f + safeReps.toFloat() / 30f)

        // Brzycki Formula: 1RM = W * (36 / (37 - R))
        val brzycki = if (safeReps < 37) {
            safeWeight * (36f / (37f - safeReps.toFloat()))
        } else {
            epley
        }

        // Blended accurate 1RM
        val final1Rm = if (safeReps == 1) safeWeight else ((epley + brzycki) / 2f)

        val percentSpecs = listOf(
            Triple(100, "1 təkrar", "Maksimal Tək Təkrar Gücü"),
            Triple(95, "2 təkrar", "Pik Güc Testi"),
            Triple(90, "3-4 təkrar", "Maksimal Qüvvə (Power)"),
            Triple(85, "5-6 təkrar", "Əsas Güc Artımı (5x5)"),
            Triple(80, "7-8 təkrar", "Hipertrofiya (Əzələ Həcmi)"),
            Triple(75, "9-10 təkrar", "Kütlə və Relyef"),
            Triple(70, "11-12 təkrar", "Əzələ Dözümlülüyü"),
            Triple(65, "15 təkrar", "Kondisionlaşma & Təkrarlar"),
            Triple(60, "18-20 təkrar", "Damarlanma & Yanma")
        )

        val breakdown = percentSpecs.map { (pct, targetReps, goal) ->
            val load = (final1Rm * (pct / 100f) * 10f).roundToInt() / 10f
            OneRepMaxPercentRow(
                percent = pct,
                weightKg = load,
                repsTarget = targetReps,
                goalTitleAz = goal
            )
        }

        return OneRepMaxEstimate(
            inputWeightKg = safeWeight,
            inputReps = safeReps,
            estimated1RmEpley = (epley * 10f).roundToInt() / 10f,
            estimated1RmBrzycki = (brzycki * 10f).roundToInt() / 10f,
            finalEstimated1Rm = (final1Rm * 10f).roundToInt() / 10f,
            percentBreakdown = breakdown
        )
    }
}
