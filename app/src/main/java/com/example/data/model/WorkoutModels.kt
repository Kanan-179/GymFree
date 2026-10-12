package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class MuscleGroup(
    val id: String,
    val nameAz: String,
    val anatomicalName: String,
    val color: Color,
    val descriptionAz: String,
    val growthTipsAz: String
) {
    SINE(
        id = "sine",
        nameAz = "Sinə",
        anatomicalName = "Pectoralis Major & Minor",
        color = MuscleChest,
        descriptionAz = "Bədənin ön üst hissəsini təşkil edən, itələmə və qucaqlama hərəkətlərinə cavabdeh əsas güc əzələsi.",
        growthTipsAz = "Sinə əzələlərini tam gərmək üçün hərəkətin alt nöqtəsində 1 saniyə fasilə verin və yuxarı qaldırarkən sinənizi sıxın."
    ),
    CIYIN(
        id = "ciyin",
        nameAz = "Çiyin",
        anatomicalName = "Deltoideus (Ön, Yan, Arxa)",
        color = MuscleShoulder,
        descriptionAz = "Qolları hər istiqamətə hərəkət etdirən və estetik V-şəkilli bədən quran 3 başlı deltoid əzələsi.",
        growthTipsAz = "Çiyinin yan başı üçün lateral açışları təmiz texnika ilə, həddindən artıq ağır çəki götürmədən, dirsəkləri yüngül bükərək edin."
    ),
    KUREK(
        id = "kurek",
        nameAz = "Kürək",
        anatomicalName = "Latissimus Dorsi, Trapezius, Rhomboids",
        color = MuscleBack,
        descriptionAz = "Geniş V-taper forması və sağlam onurğa duruşu üçün əsas çəkmə əzələləri qrupu.",
        growthTipsAz = "Çəkmə zamanı qollarınızla deyil, dirsəklərinizi geriyə apararaq kürək qanadlarını bir-birinə yaxınlaşdırmağa fokuslanın."
    ),
    BICEPS(
        id = "biceps",
        nameAz = "Biceps",
        anatomicalName = "Biceps Brachii (Uzun və Qısa Baş)",
        color = MuscleBiceps,
        descriptionAz = "Qolun ön hissəsində dirsəyi bükmə və biləyi çevirmə funksiyasını yerinə yetirən əzələ.",
        growthTipsAz = "Hərəkətin yuxarı nöqtəsində biləkləri çölə doğru yüngül çevirin (supinasiya) və bicepsi 1 saniyə maksimum sıxın."
    ),
    TRICEPS(
        id = "triceps",
        nameAz = "Triceps",
        anatomicalName = "Triceps Brachii (Yan, Medial, Uzun Baş)",
        color = MuscleTriceps,
        descriptionAz = "Qolun həcminin 65%-ni təşkil edən və qolu dirsəkdən tam açmağa cavabdeh olan güclü əzələ.",
        growthTipsAz = "Dirsəklərinizi bədəninizə sabit saxlayın, qolu tam açdıqda tricepsi pik nöqtədə sıxın."
    ),
    ABS(
        id = "abs",
        nameAz = "Abs / Qarın",
        anatomicalName = "Rectus Abdominis & Obliques",
        color = MuscleAbs,
        descriptionAz = "Onurğanı sabitləyən, bədənin mərkəz gücünü (kor) və 6-pack relyefini formalaşdıran əzələlər.",
        growthTipsAz = "Təkrarları tələsmədən, nəfəsi verərək qarnı içəri çəkib sıxaraq edin. Boyundan yox, qarın əzələlərindən dartının."
    ),
    AYAQ(
        id = "ayaq",
        nameAz = "Ayaq",
        anatomicalName = "Quadriceps, Hamstrings, Gluteus, Calves",
        color = MuscleLegs,
        descriptionAz = "Bədənin ən böyük əzələ kütləsi; testosteron və metabolizmi ən güclü stimullaşdıran əsas təməl sütunlar.",
        growthTipsAz = "Skvat və ayaq preslərində dabanlardan təkan verin, dizləri barmaq ucları istiqamətində açın və tam hərəkət amplitudundan istifadə edin."
    );

    companion object {
        fun fromId(id: String): MuscleGroup = entries.firstOrNull { it.id == id } ?: SINE
    }
}

enum class ExerciseAnimationType {
    BENCH_PRESS,
    INCLINE_DUMBBELL_PRESS,
    CHEST_FLY,
    SHOULDER_PRESS,
    LATERAL_RAISE,
    LAT_PULLDOWN,
    BENT_OVER_ROW,
    DEADLIFT,
    BICEP_CURL,
    HAMMER_CURL,
    TRICEP_PUSHDOWN,
    SKULL_CRUSHER,
    SQUAT,
    LEG_EXTENSION,
    LEG_CURL,
    CALF_RAISE,
    ABS_CRUNCH,
    PLANK,
    LEG_RAISE,
    DIPS,
    PUSH_UPS,
    PULL_UPS,
    SEATED_CABLE_ROW,
    ARNOLD_PRESS,
    FRONT_RAISE,
    PREACHER_CURL,
    OVERHEAD_TRICEPS,
    BULGARIAN_SPLIT_SQUAT,
    LEG_PRESS,
    LYING_LEG_CURL,
    RUSSIAN_TWIST,
    BICYCLE_CRUNCH,
    CABLE_CROSSOVER,
    DECLINE_BENCH_PRESS,
    PEC_DECK_FLY,
    CLOSE_GRIP_BENCH_PRESS,
    CONCENTRATION_CURL,
    INCLINE_DUMBBELL_CURL,
    T_BAR_ROW,
    DUMBBELL_ROW,
    BARBELL_SHRUG,
    FACE_PULL,
    ROMANIAN_DEADLIFT,
    HACK_SQUAT,
    HIP_THRUST,
    SEATED_CALF_RAISE,
    CABLE_LATERAL_RAISE,
    UPRIGHT_ROW,
    HANGING_LEG_RAISE,
    AB_WHEEL_ROLLOUT,
    CABLE_KNEELING_CRUNCH,
    REVERSE_CRUNCH,
    DEAD_BUG,
    DUMBBELL_SIDE_BEND,
    ALTERNATE_HEEL_TOUCHERS,
    DECLINE_CRUNCH,
    CABLE_WOODCHOP,
    SIDE_PLANK,
    JACKKNIFE_SIT_UP,
    BENCH_LYING_LEG_RAISE,
    OBLIQUE_FLOOR_CRUNCH,
    CABLE_STRAIGHT_ARM_PULLDOWN,
    TRICEPS_CABLE_KICKBACK
}

data class Exercise(
    val id: String,
    val nameAz: String,
    val primaryMuscle: MuscleGroup,
    val secondaryMuscles: List<MuscleGroup>,
    val animationType: ExerciseAnimationType,
    val equipment: String, // Ştanq, Qantel, Blok / Kabel, Trenajor, Bədən çəkisi
    val difficulty: String, // Başlanğıc, Orta, Qabaqcıl
    val recommendedSets: Int = 4,
    val recommendedReps: String = "8-12",
    val restSeconds: Int = 60,
    val descriptionAz: String,
    val executionStepsAz: List<String>,
    val targetTipsAz: String,
    val commonMistakesAz: List<String>,
    val lottieUrl: String? = null,
    val exerciseDbGifUrl: String? = null
)

data class WorkoutDay(
    val dayNumber: Int,
    val nameAz: String,
    val targetMusclesSummaryAz: String,
    val targetMuscles: List<MuscleGroup>,
    val estimatedMinutes: Int = 50,
    val exercises: List<Exercise>
)

data class WorkoutSessionState(
    val dayNumber: Int,
    val isRunning: Boolean = false,
    val currentExerciseIndex: Int = 0,
    val completedSets: Map<String, Int> = emptyMap(), // exerciseId -> completed sets count
    val elapsedSeconds: Int = 0
)
