package com.example.data.datasource

import com.example.data.model.ExerciseAnimationType

object ExerciseDbLibrary {

    data class ExerciseDbInfo(
        val exerciseId: String,
        val englishName: String,
        val gifUrl: String,
        val fallbackGifUrl: String,
        val source: String = "ExerciseDB",
        val targetMuscle: String,
        val equipment: String
    )

    // Fast jsDelivr CDN provides quick global responses and proper caching
    private const val CDN_BASE_URL = "https://cdn.jsdelivr.net/gh/omercotkd/exercises-gifs@main/assets"
    private const val RAW_BASE_URL = "https://raw.githubusercontent.com/omercotkd/exercises-gifs/main/assets"

    private fun makeInfo(
        id: String,
        englishName: String,
        targetMuscle: String,
        equipment: String
    ): ExerciseDbInfo {
        return ExerciseDbInfo(
            exerciseId = id,
            englishName = englishName,
            gifUrl = "$CDN_BASE_URL/$id.gif",
            fallbackGifUrl = "$RAW_BASE_URL/$id.gif",
            source = "ExerciseDB",
            targetMuscle = targetMuscle,
            equipment = equipment
        )
    }

    private val dbMap: Map<ExerciseAnimationType, ExerciseDbInfo> = mapOf(
        ExerciseAnimationType.BENCH_PRESS to makeInfo(
            id = "0025",
            englishName = "Barbell Bench Press",
            targetMuscle = "Pectorals (Sinə)",
            equipment = "Barbell & Bench (Ştanq)"
        ),
        ExerciseAnimationType.INCLINE_DUMBBELL_PRESS to makeInfo(
            id = "0314",
            englishName = "Dumbbell Incline Bench Press",
            targetMuscle = "Upper Pectorals (Yuxarı Sinə)",
            equipment = "Dumbbells & Incline Bench"
        ),
        ExerciseAnimationType.PUSH_UPS to makeInfo(
            id = "0662",
            englishName = "Push-up",
            targetMuscle = "Pectorals & Triceps",
            equipment = "Body Weight (Bədən Çəkisi)"
        ),
        ExerciseAnimationType.DIPS to makeInfo(
            id = "0251",
            englishName = "Chest Dip",
            targetMuscle = "Pectorals & Triceps",
            equipment = "Parallel Bars (Paralel Qollar)"
        ),
        ExerciseAnimationType.CHEST_FLY to makeInfo(
            id = "0308",
            englishName = "Dumbbell Fly",
            targetMuscle = "Pectorals (Sinə Açış)",
            equipment = "Dumbbells"
        ),
        ExerciseAnimationType.SHOULDER_PRESS to makeInfo(
            id = "0405",
            englishName = "Dumbbell Seated Shoulder Press",
            targetMuscle = "Deltoids (Çiyin Presi)",
            equipment = "Dumbbells & Bench"
        ),
        ExerciseAnimationType.ARNOLD_PRESS to makeInfo(
            id = "2137",
            englishName = "Dumbbell Arnold Press",
            targetMuscle = "Deltoids (Arnold Presi)",
            equipment = "Dumbbells"
        ),
        ExerciseAnimationType.LATERAL_RAISE to makeInfo(
            id = "0334",
            englishName = "Dumbbell Lateral Raise",
            targetMuscle = "Lateral Deltoid (Yan Çiyin)",
            equipment = "Dumbbells"
        ),
        ExerciseAnimationType.FRONT_RAISE to makeInfo(
            id = "0310",
            englishName = "Dumbbell Front Raise",
            targetMuscle = "Anterior Deltoid (Ön Çiyin)",
            equipment = "Dumbbells"
        ),
        ExerciseAnimationType.SQUAT to makeInfo(
            id = "0043",
            englishName = "Barbell Full Squat",
            targetMuscle = "Quadriceps & Glutes (Dördbashlı & Çanaq)",
            equipment = "Barbell & Rack"
        ),
        ExerciseAnimationType.BULGARIAN_SPLIT_SQUAT to makeInfo(
            id = "0410",
            englishName = "Dumbbell Single Leg Split Squat",
            targetMuscle = "Quadriceps & Glutes (Bolqar Skvatı)",
            equipment = "Dumbbells & Bench"
        ),
        ExerciseAnimationType.LEG_PRESS to makeInfo(
            id = "0739",
            englishName = "Sled 45° Leg Press",
            targetMuscle = "Quadriceps (Ayaq Presi)",
            equipment = "Sled Machine"
        ),
        ExerciseAnimationType.LEG_EXTENSION to makeInfo(
            id = "0585",
            englishName = "Lever Leg Extension",
            targetMuscle = "Quadriceps (Ayaq Açma)",
            equipment = "Lever Machine"
        ),
        ExerciseAnimationType.LEG_CURL to makeInfo(
            id = "0599",
            englishName = "Lever Seated Leg Curl",
            targetMuscle = "Hamstrings (Bud Arxası Oturaq)",
            equipment = "Lever Machine"
        ),
        ExerciseAnimationType.LYING_LEG_CURL to makeInfo(
            id = "0586",
            englishName = "Lever Lying Leg Curl",
            targetMuscle = "Hamstrings (Bud Arxası Uzanaraq)",
            equipment = "Lever Machine"
        ),
        ExerciseAnimationType.CALF_RAISE to makeInfo(
            id = "1372",
            englishName = "Barbell Standing Calf Raise",
            targetMuscle = "Calves (Baldır)",
            equipment = "Barbell / Platform"
        ),
        ExerciseAnimationType.DEADLIFT to makeInfo(
            id = "0032",
            englishName = "Barbell Deadlift",
            targetMuscle = "Hamstrings, Glutes & Lower Back (Dedlift)",
            equipment = "Barbell"
        ),
        ExerciseAnimationType.BENT_OVER_ROW to makeInfo(
            id = "0027",
            englishName = "Barbell Bent Over Row",
            targetMuscle = "Lats & Rhomboids (Ştanq Dartma)",
            equipment = "Barbell"
        ),
        ExerciseAnimationType.PULL_UPS to makeInfo(
            id = "0652",
            englishName = "Pull-up",
            targetMuscle = "Lats & Biceps (Turnikdə Dartınma)",
            equipment = "Pull-up Bar"
        ),
        ExerciseAnimationType.LAT_PULLDOWN to makeInfo(
            id = "0198",
            englishName = "Cable Pulldown",
            targetMuscle = "Latissimus Dorsi (Qanad Blok Dartma)",
            equipment = "Cable Pulldown Machine"
        ),
        ExerciseAnimationType.SEATED_CABLE_ROW to makeInfo(
            id = "0861",
            englishName = "Cable Seated Row",
            targetMuscle = "Middle Back & Lats (Kabel Blok)",
            equipment = "Cable Machine"
        ),
        ExerciseAnimationType.BICEP_CURL to makeInfo(
            id = "0031",
            englishName = "Barbell Curl",
            targetMuscle = "Biceps Brachii (Biseps)",
            equipment = "Barbell"
        ),
        ExerciseAnimationType.HAMMER_CURL to makeInfo(
            id = "0313",
            englishName = "Dumbbell Hammer Curl",
            targetMuscle = "Brachialis & Forearms (Çəkic Bükmə)",
            equipment = "Dumbbells"
        ),
        ExerciseAnimationType.PREACHER_CURL to makeInfo(
            id = "0070",
            englishName = "Barbell Preacher Curl",
            targetMuscle = "Biceps (Skott Skamyası)",
            equipment = "Barbell & Preacher Bench"
        ),
        ExerciseAnimationType.TRICEP_PUSHDOWN to makeInfo(
            id = "0201",
            englishName = "Cable Pushdown",
            targetMuscle = "Triceps Brachii (Blok Triseps)",
            equipment = "Cable Machine"
        ),
        ExerciseAnimationType.SKULL_CRUSHER to makeInfo(
            id = "0060",
            englishName = "Barbell Lying Triceps Extension Skull Crusher",
            targetMuscle = "Triceps (Fransız Presi)",
            equipment = "Barbell & Flat Bench"
        ),
        ExerciseAnimationType.OVERHEAD_TRICEPS to makeInfo(
            id = "0194",
            englishName = "Cable Overhead Triceps Extension",
            targetMuscle = "Triceps Long Head (Baş Üzərindən Triseps)",
            equipment = "Cable & Rope"
        ),
        ExerciseAnimationType.PLANK to makeInfo(
            id = "2135",
            englishName = "Front Plank",
            targetMuscle = "Core & Abs (Plank)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.ABS_CRUNCH to makeInfo(
            id = "0274",
            englishName = "Crunch Floor",
            targetMuscle = "Rectus Abdominis (Qarın Bükmə)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.BICYCLE_CRUNCH to makeInfo(
            id = "0003",
            englishName = "Air Bike / Bicycle Crunch",
            targetMuscle = "Obliques & Lower Abs (Velosiped)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.RUSSIAN_TWIST to makeInfo(
            id = "0687",
            englishName = "Russian Twist",
            targetMuscle = "Obliques (Rus Burulması)",
            equipment = "Body Weight / Weight Plate"
        ),
        ExerciseAnimationType.LEG_RAISE to makeInfo(
            id = "2963",
            englishName = "Captains Chair Straight Leg Raise",
            targetMuscle = "Lower Abs & Hip Flexors (Ayaq Qaldırma)",
            equipment = "Captains Chair / Mat"
        ),
        ExerciseAnimationType.CABLE_CROSSOVER to makeInfo(
            id = "0179",
            englishName = "Cable Standing Fly Crossover",
            targetMuscle = "Pectorals (Kabel Krossover)",
            equipment = "Cable Machine (Kabel Trenajoru)"
        ),
        ExerciseAnimationType.DECLINE_BENCH_PRESS to makeInfo(
            id = "0033",
            englishName = "Barbell Decline Bench Press",
            targetMuscle = "Lower Pectorals (Aşağı Sinə)",
            equipment = "Barbell & Decline Bench"
        ),
        ExerciseAnimationType.PEC_DECK_FLY to makeInfo(
            id = "0064",
            englishName = "Lever Seated Fly / Pec Deck",
            targetMuscle = "Pectorals (Kəpənək Trenajoru)",
            equipment = "Pec Deck Machine"
        ),
        ExerciseAnimationType.CLOSE_GRIP_BENCH_PRESS to makeInfo(
            id = "0030",
            englishName = "Barbell Close-Grip Bench Press",
            targetMuscle = "Triceps & Inner Chest (Dar Tutuş Ştanq)",
            equipment = "Barbell & Bench"
        ),
        ExerciseAnimationType.CONCENTRATION_CURL to makeInfo(
            id = "0297",
            englishName = "Dumbbell Concentration Curl",
            targetMuscle = "Biceps Brachii Peak (Konsentrasiya)",
            equipment = "Dumbbell & Bench"
        ),
        ExerciseAnimationType.INCLINE_DUMBBELL_CURL to makeInfo(
            id = "0315",
            englishName = "Dumbbell Incline Biceps Curl",
            targetMuscle = "Biceps Long Head (Meyilli Skamya)",
            equipment = "Dumbbells & Incline Bench"
        ),
        ExerciseAnimationType.T_BAR_ROW to makeInfo(
            id = "0837",
            englishName = "Lever T-bar Row",
            targetMuscle = "Middle Back & Lats (T-Qrif Dartma)",
            equipment = "T-Bar Machine / Landmine"
        ),
        ExerciseAnimationType.DUMBBELL_ROW to makeInfo(
            id = "0292",
            englishName = "Dumbbell One Arm Bent-over Row",
            targetMuscle = "Latissimus Dorsi (Tək Qol Mişar)",
            equipment = "Dumbbell & Flat Bench"
        ),
        ExerciseAnimationType.BARBELL_SHRUG to makeInfo(
            id = "0095",
            englishName = "Barbell Shrug",
            targetMuscle = "Trapezius (Trapesiya Şraq)",
            equipment = "Barbell"
        ),
        ExerciseAnimationType.FACE_PULL to makeInfo(
            id = "0174",
            englishName = "Cable Face Pull",
            targetMuscle = "Rear Deltoid & Rotator Cuff (Feys Pul)",
            equipment = "Cable Machine & Rope"
        ),
        ExerciseAnimationType.ROMANIAN_DEADLIFT to makeInfo(
            id = "0085",
            englishName = "Barbell Romanian Deadlift",
            targetMuscle = "Hamstrings & Glutes (Rumın Dedlifti)",
            equipment = "Barbell"
        ),
        ExerciseAnimationType.HACK_SQUAT to makeInfo(
            id = "0738",
            englishName = "Sled Hack Squat",
            targetMuscle = "Quadriceps (Qakk Skvat Trenajoru)",
            equipment = "Hack Squat Sled Machine"
        ),
        ExerciseAnimationType.HIP_THRUST to makeInfo(
            id = "1001",
            englishName = "Barbell Hip Thrust",
            targetMuscle = "Glutes & Hamstrings (Sağrı / Hip Trast)",
            equipment = "Barbell & Bench"
        ),
        ExerciseAnimationType.SEATED_CALF_RAISE to makeInfo(
            id = "0728",
            englishName = "Lever Seated Calf Raise",
            targetMuscle = "Soleus & Calves (Oturaq Baldır)",
            equipment = "Seated Calf Machine"
        ),
        ExerciseAnimationType.CABLE_LATERAL_RAISE to makeInfo(
            id = "0183",
            englishName = "Cable One Arm Lateral Raise",
            targetMuscle = "Lateral Deltoid (Kabel Yan Çiyin)",
            equipment = "Cable Machine"
        ),
        ExerciseAnimationType.UPRIGHT_ROW to makeInfo(
            id = "0438",
            englishName = "Dumbbell Upright Row",
            targetMuscle = "Deltoids & Trapezius (Çənəyə Qantel)",
            equipment = "Dumbbells"
        ),
        ExerciseAnimationType.HANGING_LEG_RAISE to makeInfo(
            id = "0472",
            englishName = "Hanging Leg Raise",
            targetMuscle = "Lower Abdominals (Turnikdə Ayaq)",
            equipment = "Pull-up Bar"
        ),
        ExerciseAnimationType.AB_WHEEL_ROLLOUT to makeInfo(
            id = "0867",
            englishName = "Wheel Rollout",
            targetMuscle = "Core & Rectus Abdominis (Qarın Roliki)",
            equipment = "Ab Roller Wheel"
        ),
        ExerciseAnimationType.CABLE_KNEELING_CRUNCH to makeInfo(
            id = "0175",
            englishName = "Cable Kneeling Crunch",
            targetMuscle = "Rectus Abdominis (Blokda Dizüstü Qarın)",
            equipment = "Cable Machine & Rope"
        ),
        ExerciseAnimationType.REVERSE_CRUNCH to makeInfo(
            id = "0872",
            englishName = "Reverse Crunch",
            targetMuscle = "Lower Rectus Abdominis (Əks Qarın Bükmə)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.DEAD_BUG to makeInfo(
            id = "0276",
            englishName = "Dead Bug",
            targetMuscle = "Transverse Abdominis & Core (Ölü Böcək)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.DUMBBELL_SIDE_BEND to makeInfo(
            id = "0407",
            englishName = "Dumbbell Side Bend",
            targetMuscle = "Obliques (Qantellə Yana Əyilmə)",
            equipment = "Dumbbell"
        ),
        ExerciseAnimationType.ALTERNATE_HEEL_TOUCHERS to makeInfo(
            id = "0006",
            englishName = "Alternate Heel Touchers",
            targetMuscle = "Obliques & Upper Abs (Pinqvin Daban Toxunuşu)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.DECLINE_CRUNCH to makeInfo(
            id = "0277",
            englishName = "Decline Crunch",
            targetMuscle = "Upper & Middle Abs (Meyilli Skamya Qarın)",
            equipment = "Decline Bench"
        ),
        ExerciseAnimationType.CABLE_WOODCHOP to makeInfo(
            id = "0243",
            englishName = "Cable Twist / Woodchop",
            targetMuscle = "Obliques & Core Rotators (Blok Ağackəsən)",
            equipment = "Cable Machine"
        ),
        ExerciseAnimationType.SIDE_PLANK to makeInfo(
            id = "0705",
            englishName = "Side Plank / Side Bridge",
            targetMuscle = "Lateral Core & Obliques (Yan Plank)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.JACKKNIFE_SIT_UP to makeInfo(
            id = "0507",
            englishName = "Jackknife Sit-up / V-Up",
            targetMuscle = "Full Abdominal Wall (Bıçaq Qarın V-Up)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.BENCH_LYING_LEG_RAISE to makeInfo(
            id = "0620",
            englishName = "Lying Leg Raise on Flat Bench",
            targetMuscle = "Lower Abdominals (Skamyada Ayaq Qaldırma)",
            equipment = "Flat Bench"
        ),
        ExerciseAnimationType.OBLIQUE_FLOOR_CRUNCH to makeInfo(
            id = "0635",
            englishName = "Oblique Floor Crunch",
            targetMuscle = "Internal & External Obliques (Yan Bükmə)",
            equipment = "Mat / Floor"
        ),
        ExerciseAnimationType.CABLE_STRAIGHT_ARM_PULLDOWN to makeInfo(
            id = "0238",
            englishName = "Cable Straight Arm Pulldown",
            targetMuscle = "Latissimus Dorsi & Core (Düz Qol Dartma)",
            equipment = "Cable Machine & Straight Bar"
        ),
        ExerciseAnimationType.TRICEPS_CABLE_KICKBACK to makeInfo(
            id = "0860",
            englishName = "Cable Triceps Kickback",
            targetMuscle = "Triceps Brachii Lateral Head (Blok Triseps Kickback)",
            equipment = "Cable Machine"
        )
    )

    fun getExerciseDbInfo(type: ExerciseAnimationType, customGifUrl: String? = null): ExerciseDbInfo {
        if (!customGifUrl.isNullOrBlank()) {
            return ExerciseDbInfo(
                exerciseId = "custom",
                englishName = "Fərdi Hərəkət",
                gifUrl = customGifUrl,
                fallbackGifUrl = customGifUrl,
                source = "Hərəkət Kataloqu",
                targetMuscle = "Hədəf Əzələlər",
                equipment = "Standart"
            )
        }
        return dbMap[type] ?: makeInfo(
            id = "0025",
            englishName = "Barbell Bench Press",
            targetMuscle = "Sinə",
            equipment = "Ştanq"
        )
    }

    fun hasExerciseDbItem(type: ExerciseAnimationType): Boolean = dbMap.containsKey(type)
}
