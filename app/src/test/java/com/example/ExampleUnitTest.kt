package com.example

import com.example.data.datasource.WorkoutRecommendationEngine
import com.example.data.model.MuscleGroup
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testWorkoutRecommendation_pushMuscles() {
    val selected = setOf(MuscleGroup.SINE, MuscleGroup.CIYIN, MuscleGroup.TRICEPS)
    val plan = WorkoutRecommendationEngine.generatePlan(selected)

    assertNotNull(plan)
    assertTrue(plan.exercises.isNotEmpty())
    assertEquals(selected, plan.selectedMuscles)
    // SINE should be present
    assertTrue(plan.exercises.any { it.targetMuscle == MuscleGroup.SINE })
    // CIYIN should be present
    assertTrue(plan.exercises.any { it.targetMuscle == MuscleGroup.CIYIN })
    // TRICEPS should be present
    assertTrue(plan.exercises.any { it.targetMuscle == MuscleGroup.TRICEPS })
    assertTrue(plan.totalEstimatedMinutes > 0)
  }

  @Test
  fun testWorkoutRecommendation_singleMuscleBiceps() {
    val selected = setOf(MuscleGroup.BICEPS)
    val plan = WorkoutRecommendationEngine.generatePlan(selected)

    assertNotNull(plan)
    assertTrue(plan.exercises.isNotEmpty())
    assertTrue(plan.exercises.all { it.targetMuscle == MuscleGroup.BICEPS })
  }

  @Test
  fun testWorkoutRecommendation_emptySelectionFallback() {
    val plan = WorkoutRecommendationEngine.generatePlan(emptySet())
    assertNotNull(plan)
    assertTrue(plan.exercises.isNotEmpty())
  }

  @Test
  fun testExerciseDbLibrary_mapping() {
    val benchInfo = com.example.data.datasource.ExerciseDbLibrary.getExerciseDbInfo(com.example.data.model.ExerciseAnimationType.BENCH_PRESS)
    assertEquals("0025", benchInfo.exerciseId)
    assertTrue(benchInfo.gifUrl.endsWith("0025.gif"))
    assertTrue(benchInfo.gifUrl.startsWith("https://cdn.jsdelivr.net/gh/omercotkd/exercises-gifs@main/assets/"))
    assertEquals("Barbell Bench Press", benchInfo.englishName)

    val squatInfo = com.example.data.datasource.ExerciseDbLibrary.getExerciseDbInfo(com.example.data.model.ExerciseAnimationType.SQUAT)
    assertEquals("0043", squatInfo.exerciseId)
    assertTrue(squatInfo.gifUrl.endsWith("0043.gif"))

    com.example.data.model.ExerciseAnimationType.values().forEach { type ->
      val info = com.example.data.datasource.ExerciseDbLibrary.getExerciseDbInfo(type)
      assertNotNull(info)
      assertTrue(info.exerciseId.isNotBlank())
      assertTrue(info.gifUrl.isNotBlank())
      assertTrue(info.fallbackGifUrl.isNotBlank())
    }
  }
}
