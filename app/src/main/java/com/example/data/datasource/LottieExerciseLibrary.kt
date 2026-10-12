package com.example.data.datasource

import com.example.data.model.ExerciseAnimationType

object LottieExerciseLibrary {

    data class LottieExerciseInfo(
        val lottieUrl: String,
        val authorOrSource: String,
        val descriptionAz: String
    )

    // Popular free community Lottie animations for workouts hosted on LottieFiles public CDN
    private val lottieMap: Map<ExerciseAnimationType, LottieExerciseInfo> = mapOf(
        ExerciseAnimationType.SQUAT to LottieExerciseInfo(
            lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_9x9qtxoa.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Düzgün skvat hərəkəti: Dabanlar döşəmədə, bel düz və dizlər paralel vəziyyətdə çömbəlmə."
        ),
        ExerciseAnimationType.PUSH_UPS to LottieExerciseInfo(
            lottieUrl = "https://assets9.lottiefiles.com/packages/lf20_q0hmtmhs.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Döşdən jim (Push-up): Bədən düz xətt şəklində plank vəziyyətində sinə yerə toxunana qədər eniş."
        ),
        ExerciseAnimationType.BICEP_CURL to LottieExerciseInfo(
            lottieUrl = "https://assets10.lottiefiles.com/packages/lf20_7fy2y3um.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Biseps qantel qaldırma: Dirsəklər gövdəyə sabit, təcrid olunmuş biseps bükməsi."
        ),
        ExerciseAnimationType.BENCH_PRESS to LottieExerciseInfo(
            lottieUrl = "https://assets6.lottiefiles.com/packages/lf20_2cjjmzun.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Üfüqi sinə presi: Ştanqın sinə səviyyəsinə nəzarətli endirilməsi və yuxarı sıxılması."
        ),
        ExerciseAnimationType.PLANK to LottieExerciseInfo(
            lottieUrl = "https://assets4.lottiefiles.com/packages/lf20_4kfl4d3m.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Klassik plank: Bədən mərkəz (kor) əzələlərinin izometrik gərginlikdə saxlanması."
        ),
        ExerciseAnimationType.ABS_CRUNCH to LottieExerciseInfo(
            lottieUrl = "https://assets3.lottiefiles.com/packages/lf20_jcikwtux.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Abdominal crunch: Kürəyin yuxarı hissəsinin qaldırılaraq qarın əzələsinin yığılması."
        ),
        ExerciseAnimationType.BICYCLE_CRUNCH to LottieExerciseInfo(
            lottieUrl = "https://assets1.lottiefiles.com/packages/lf20_dw7j2n6x.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Velosiped qarın hərəkəti: Çarpaz dirsək və dizlərin dinamik birləşməsi."
        ),
        ExerciseAnimationType.DEADLIFT to LottieExerciseInfo(
            lottieUrl = "https://assets5.lottiefiles.com/packages/lf20_z9ed2y6q.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Dedlift: Omba və budların arxası ilə ştanqın yerdən düz onurğa ilə qaldırılması."
        ),
        ExerciseAnimationType.SHOULDER_PRESS to LottieExerciseInfo(
            lottieUrl = "https://assets7.lottiefiles.com/packages/lf20_5njp3vnm.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Çiyin presi: Baş üzərinə ağırlığın itələnməsi və deltoid əzələlərinin işləməsi."
        ),
        ExerciseAnimationType.PULL_UPS to LottieExerciseInfo(
            lottieUrl = "https://assets8.lottiefiles.com/packages/lf20_3rwasyjy.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Turnikdə dartınma: Çənənin mil üzərinə qaldırılması və qanad əzələlərinin yığılması."
        ),
        ExerciseAnimationType.DIPS to LottieExerciseInfo(
            lottieUrl = "https://assets9.lottiefiles.com/packages/lf20_w51pcehl.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Paralel qollar (Dips): Dirsəklərin 90 dərəcə bükülərək bədənin endirilməsi və qolların açılması."
        ),
        ExerciseAnimationType.LATERAL_RAISE to LottieExerciseInfo(
            lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_v1yudlrx.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Qantellə yana açış: Yan deltoidlərin aktivləşdirilməsi və çiyin eninin genişləndirilməsi."
        ),
        ExerciseAnimationType.LEG_PRESS to LottieExerciseInfo(
            lottieUrl = "https://assets6.lottiefiles.com/packages/lf20_m6cu9m9a.json",
            authorOrSource = "LottieFiles Community",
            descriptionAz = "Ayaq presi trenajoru: Dabanlarla platformanın itələnməsi və dörbaşlı budun gücləndirilməsi."
        )
    )

    fun getLottieForExercise(type: ExerciseAnimationType, customUrl: String? = null): LottieExerciseInfo {
        if (!customUrl.isNullOrBlank()) {
            return LottieExerciseInfo(
                lottieUrl = customUrl,
                authorOrSource = "LottieFiles (Xüsusi Link)",
                descriptionAz = "Xüsusi Lottie animasiyası"
            )
        }
        return lottieMap[type] ?: LottieExerciseInfo(
            lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_9x9qtxoa.json", // Fallback fitness animation
            authorOrSource = "LottieFiles Fitness",
            descriptionAz = "LottieFiles vektor fitnes animasiyası"
        )
    }

    fun hasDedicatedLottie(type: ExerciseAnimationType): Boolean = lottieMap.containsKey(type)
}
