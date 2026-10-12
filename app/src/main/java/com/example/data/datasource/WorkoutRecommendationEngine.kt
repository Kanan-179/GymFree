package com.example.data.datasource

import com.example.data.model.Exercise
import com.example.data.model.MuscleGroup

data class RecommendedExerciseItem(
    val exercise: Exercise,
    val roleBadge: String, // "1. Əsas Baza", "2. Köməkçi Baza", "3. İzolyasiya", "4. Finisher / Yandırıcı"
    val recommendationReasonAz: String,
    val targetMuscle: MuscleGroup,
    val sets: Int,
    val reps: String,
    val restSeconds: Int
)

data class RecommendedPlan(
    val selectedMuscles: Set<MuscleGroup>,
    val titleAz: String,
    val subtitleAz: String,
    val totalEstimatedMinutes: Int,
    val exercises: List<RecommendedExerciseItem>
)

object WorkoutRecommendationEngine {

    // Preset options for quick 1-tap selection
    val quickPresets = listOf(
        WorkoutPreset(
            id = "push",
            nameAz = "Push (İtələmə)",
            icon = "🔥",
            muscles = setOf(MuscleGroup.SINE, MuscleGroup.CIYIN, MuscleGroup.TRICEPS),
            descriptionAz = "Sinə, çiyin və qolun arxası"
        ),
        WorkoutPreset(
            id = "pull",
            nameAz = "Pull (Çəkmə)",
            icon = "🦾",
            muscles = setOf(MuscleGroup.KUREK, MuscleGroup.BICEPS),
            descriptionAz = "Kürək qanadları və biceps"
        ),
        WorkoutPreset(
            id = "legs_core",
            nameAz = "Ayaq & Kor",
            icon = "🦵",
            muscles = setOf(MuscleGroup.AYAQ, MuscleGroup.ABS),
            descriptionAz = "Bud, baldır və 6-pack abs"
        ),
        WorkoutPreset(
            id = "arms",
            nameAz = "Qol Günü",
            icon = "💪",
            muscles = setOf(MuscleGroup.BICEPS, MuscleGroup.TRICEPS),
            descriptionAz = "Biceps və triceps superseti"
        ),
        WorkoutPreset(
            id = "chest_arms",
            nameAz = "Sinə & Qol",
            icon = "🏋️",
            muscles = setOf(MuscleGroup.SINE, MuscleGroup.BICEPS, MuscleGroup.TRICEPS),
            descriptionAz = "Geniş sinə və güclü qollar"
        ),
        WorkoutPreset(
            id = "back_shoulders",
            nameAz = "Kürək & Çiyin",
            icon = "🦅",
            muscles = setOf(MuscleGroup.KUREK, MuscleGroup.CIYIN),
            descriptionAz = "V-Taper və enli çiyinlər"
        )
    )

    fun generatePlan(selectedMuscles: Set<MuscleGroup>): RecommendedPlan {
        if (selectedMuscles.isEmpty()) {
            return generatePlan(setOf(MuscleGroup.SINE, MuscleGroup.TRICEPS))
        }

        val allExercises = FitnessData.allExercises

        // Priority order for multi-muscle day (Big compound muscle groups first, smaller arms & core last)
        val muscleOrderPriority = listOf(
            MuscleGroup.AYAQ,
            MuscleGroup.KUREK,
            MuscleGroup.SINE,
            MuscleGroup.CIYIN,
            MuscleGroup.TRICEPS,
            MuscleGroup.BICEPS,
            MuscleGroup.ABS
        )

        val sortedMuscles = selectedMuscles.sortedBy { muscle ->
            val idx = muscleOrderPriority.indexOf(muscle)
            if (idx == -1) 99 else idx
        }

        val items = mutableListOf<RecommendedExerciseItem>()

        val muscleNamesAz = sortedMuscles.joinToString(" + ") { it.nameAz }
        val isMultiMuscle = sortedMuscles.size > 1

        sortedMuscles.forEach { muscle ->
            val muscleExercises = allExercises.filter { it.primaryMuscle == muscle }
            val countToTake = when {
                sortedMuscles.size == 1 -> 4 // Single muscle day: 4 dedicated exercises
                sortedMuscles.size == 2 -> 3 // Two muscles: 3 exercises each (total 6)
                sortedMuscles.size == 3 -> 2 // Three muscles: 2 exercises each (total 6)
                else -> 1 // 4+ muscles (Full body style): 1-2 exercises each
            }

            muscleExercises.take(countToTake).forEachIndexed { index, exercise ->
                val roleBadge = when {
                    index == 0 && items.isEmpty() -> "1. Əsas Baza (Compound)"
                    index == 0 -> "Baza Hərəkəti"
                    index == 1 -> "Köməkçi Baza"
                    index == 2 -> "İzolyasiya & Relyef"
                    else -> "Finisher / Yandırıcı"
                }

                val reason = when (exercise.id) {
                    "bench_press" -> "Maksimum sinə gücü, kütlə və ümumi qalınlıq üçün nömrə bir təməl hərəkət."
                    "incline_db_press" -> "Sinənin yuxarı körpücük hissəsini dolduraraq estetik həcm verir."
                    "chest_fly" -> "Sinə liflərini tam gərərək daxili cizgi relyefini formalaşdırır."
                    "cable_crossover" -> "Daimi kabel gərginliyi ilə sinəni pik yığılma nöqtəsində yandırır."
                    "push_ups" -> "Bədən çəkisi ilə sinə, çiyin və kor əzələlərini sinxron gücləndirən qızıl standart."

                    "barbell_row" -> "Kürəyin qalınlığını və onurğa sütunu ətrafı əzələləri maksimum gücləndirir."
                    "lat_pulldown" -> "V-şəkilli geniş kürək qanadları üçün ən effektiv dartma hərəkəti."
                    "deadlift" -> "Bütün arxa zənciri və bədənin ümumi fiziki gücünü pikə çatdırır."
                    "pull_ups" -> "Bədən çəkisi ilə qanadları ən geniş açan və nisbi gücü artıran təbii hərəkət."
                    "seated_cable_row" -> "Kürəyin orta hissəsini və rombvari əzələləri relyefli edir."

                    "db_shoulder_press" -> "Çiyinin ön və yan başlıqlarını dolduraraq kürəvari forma verir."
                    "lateral_raise" -> "Çiyinləri vizual olaraq ən çox genişləndirən yan başlıq hərəkəti."
                    "cable_face_pull" -> "Arxa çiyin və rotasiya manjetini qoruyan, duruşu düzəldən hərəkət."
                    "arnold_press" -> "180 dərəcə fırlanma ilə deltoidin bütün başlıqlarını eyni anda yükləyir."
                    "front_raise" -> "Ön çiyin başlığını təcrid edərək qabaq hissəyə relyefli həcm qazandırır."

                    "barbell_curl" -> "Bicepsin pik hündürlüyü və ümumi həcmi üçün təməl baza."
                    "hammer_curl" -> "Braxialisi inkişaf etdirərək qola yandan qalın və güclü görünüş verir."
                    "cable_bicep_curl" -> "Daimi təzyiqlə bicepsin qısa və uzun başını nasoslayır (pump)."
                    "preacher_curl" -> "Qolları söykəyərək inersiyanı sıfırlayır və bicepsi təmiz təcrid edir."

                    "tricep_pushdown" -> "Tricepsin yan başını kəskinləşdirərək nalvari relyef yaradır."
                    "skull_crusher" -> "Tricepsin uzun başını dərindən dartaraq qolun arxa kütləsini artırır."
                    "parallel_dips" -> "Bədən çəkisi ilə tricepsin hər üç başını və alt sinəni partlayıcı şəkildə işlədir."
                    "overhead_triceps", "overhead_db_extension" -> "Qolu başın üzərindən açaraq tricepsdə dərin hipertrofiya yaradır."

                    "barbell_squat" -> "Bütün ayaq əzələlərini, dördbaşlı əzələni və metabolizmi ən güclü stimullaşdıran hərəkət."
                    "leg_press" -> "Onurğaya yük salmadan böyük çəkilərlə dörbaşlı bud əzələsini gücləndirir."
                    "bulgarian_split_squat" -> "Hər ayağı ayrı işlədərək asimmetriyanı yox edir və qalçanı formalaşdırır."
                    "lying_leg_curl" -> "Budun arxa iki-başlı əzələsini birbaşa izolə edərək diz oynağını möhkəmləndirir."
                    "romanian_deadlift" -> "Budun arxa hissəsini (hamstrings) və qalçanı gücləndirir."
                    "leg_extension" -> "Kvadriceps əzələsini birbaşa izolə edərək diz ətrafı formanı kəskinləşdirir."
                    "calf_raise" -> "Ayağın baldır relyefini və dözümlülüyünü artırır."

                    "hanging_leg_raise" -> "Alt qarın əzələlərini və çanaq bükücülərini dərindən işlədir."
                    "ab_crunch", "crunches" -> "Yuxarı qarın kubiklərini sıxaraq relyefi üzə çıxarır."
                    "bicycle_crunch" -> "Dönmə hərəkəti ilə kubikləri və yan qarın liflərini maksimal aktivləşdirir."
                    "russian_twist" -> "Oturaraq fırlanma ilə yan qarın əzələlərini və kor sabitliyini bərkidir."
                    "plank" -> "Bütün kor sabitliyini və onurğa dayaq gücünü möhkəmləndirir."

                    else -> "${exercise.primaryMuscle.nameAz} əzələsi üçün optimal inkişaf və güc təmin edir."
                }

                items.add(
                    RecommendedExerciseItem(
                        exercise = exercise,
                        roleBadge = roleBadge,
                        recommendationReasonAz = reason,
                        targetMuscle = exercise.primaryMuscle,
                        sets = exercise.recommendedSets,
                        reps = exercise.recommendedReps,
                        restSeconds = exercise.restSeconds
                    )
                )
            }
        }

        val estimatedMins = (items.size * 7.5).toInt().coerceIn(25, 75)

        val planTitle = if (isMultiMuscle) {
            "Bugünkü Hədəf: $muscleNamesAz Məşqi"
        } else {
            "Bugünkü Hədəf: ${sortedMuscles.first().nameAz} Günü"
        }

        val planSubtitle = if (isMultiMuscle) {
            "${sortedMuscles.size} əzələ qrupu üçün optimal elmi sıralama: Əvvəlcə baza güc hərəkətləri, ardınca izolyasiya və yandırıcılar."
        } else {
            "${sortedMuscles.first().nameAz} əzələsinin hərtərəfli böyüməsi üçün tam həcmli xüsusi proqram."
        }

        return RecommendedPlan(
            selectedMuscles = selectedMuscles,
            titleAz = planTitle,
            subtitleAz = planSubtitle,
            totalEstimatedMinutes = estimatedMins,
            exercises = items
        )
    }
}

data class WorkoutPreset(
    val id: String,
    val nameAz: String,
    val icon: String,
    val muscles: Set<MuscleGroup>,
    val descriptionAz: String
)
