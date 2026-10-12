package com.example.data.datasource

import com.example.data.model.Exercise
import com.example.data.model.ExerciseAnimationType
import com.example.data.model.MuscleGroup
import com.example.data.model.WorkoutDay

object FitnessData {

    val allExercises: List<Exercise> = listOf(
        // --- SİNƏ ---
        Exercise(
            id = "bench_press",
            nameAz = "Ştanqla Üfüqi Sinə Presi",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.BENCH_PRESS,
            lottieUrl = "https://assets6.lottiefiles.com/packages/lf20_2cjjmzun.json",
            equipment = "Ştanq & Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-10",
            restSeconds = 90,
            descriptionAz = "Sinə əzələlərinin ümumi kütləsini və gücünü artıran ən təməl baza hərəkətidir. Sinənin orta və alt hissəsini maksimal dərəcədə aktivləşdirir.",
            executionStepsAz = listOf(
                "Skamyaya uzanın, ayaqlarınızı döşəməyə möhkəm basın.",
                "Ştanqı çiyin genişliyindən bir az daha geniş tutuşla götürün.",
                "Nəfəs alaraq ştanqı yavaşca sinənizin orta hissəsinə qədər endirin (dirsəklər 45-75 dərəcə bucaqda).",
                "Nəfəsi verərək ştanqı başlanğıc vəziyyətinə güclü şəkildə qaldırın və sinəni sıxın."
            ),
            targetTipsAz = "Dirsəklərinizi 90 dərəcə çölə açmayın; 45 dərəcə bucaq çiyin oynaqlarını qoruyur və sinəyə daha çox təzyiq salır.",
            commonMistakesAz = listOf(
                "Ştanqı sinəyə çırpmaq",
                "Çanağı skamyadan qaldırmaq",
                "Dirsəkləri yuxarıda kilidləmək"
            )
        ),
        Exercise(
            id = "incline_db_press",
            nameAz = "Meyilli Skamyada Qantel Presi",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.CIYIN, MuscleGroup.TRICEPS),
            animationType = ExerciseAnimationType.INCLINE_DUMBBELL_PRESS,
            equipment = "Qantellər & Meyilli Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 75,
            descriptionAz = "Sinənin yuxarı körpücük hissəsini (klavikulyar baş) doldurur və sinəyə həcmli, bütöv forma verir.",
            executionStepsAz = listOf(
                "Skamyanı 30-45 dərəcə meyilliyə qoyun.",
                "Qantelləri dizlərinizdən təkanla yuxarı qaldıraraq çiyin səviyyəsinə gətirin.",
                "Nəfəs alaraq dirsəkləri aşağı və bir qədər bədənə yaxın endirin.",
                "Nəfəs verərək qantelləri yuxarıda qövsvari hərəkətlə qovuşdurun (bir-birinə vurmadan)."
            ),
            targetTipsAz = "Skamyanın bucağını 45 dərəcədən çox qaldırmayın, əks halda yük sinədən çiyinlərə keçəcək.",
            commonMistakesAz = listOf(
                "Bucağı 60 dərəcəyə qədər çox dik qoymaq",
                "Qantelləri yuxarıda bir-birinə vurmaq",
                "Nəzarətsiz sürətli endirmək"
            )
        ),
        Exercise(
            id = "chest_fly",
            nameAz = "Qantellə Sinə Açışı",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.CHEST_FLY,
            equipment = "Qantellər",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Sinə əzələ liflərini tam gərən və daxili sinə cizgisi relyefini kəskinləşdirən izolə hərəkətdir.",
            executionStepsAz = listOf(
                "Üfüqi skamyaya uzanın, qantelləri sinə üzərində dirsəkləri yüngül bükülmüş saxlayın.",
                "Qucaqlama pozasını saxlayaraq qolları asta-asta yana açın.",
                "Sinədə dərin dartılma hiss etdikdə nəfəs verərək qolları yenidən yuxarı yığın."
            ),
            targetTipsAz = "Hərəkət boyu dirsəkdəki bucağı sabit saxlayın; bu pres deyil, qucaqlama hərəkətidir.",
            commonMistakesAz = listOf(
                "Qantelləri çox dərindən endirib çiyini zədələmək",
                "Hərəkəti presə çevirərək dirsəkləri büküb-açmaq"
            )
        ),
        Exercise(
            id = "cable_crossover",
            nameAz = "Blokda Krossover Sinə Sıxma",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.CHEST_FLY,
            equipment = "Kabel / Blok Trenajoru",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Bütün hərəkət boyunca daimi gərginlik təmin edir, sinə əzələsinin daxili və alt hissəsini kəskinləşdirir.",
            executionStepsAz = listOf(
                "Hər iki kabel dəstəyini yuxarı blokdan tutun və bir addım irəli çıxın.",
                "Bədəni yüngülcə irəli əyin, dirsəkləri sabit saxlayaraq dəstəkləri qabaqda aşağıya doğru qovuşdurun.",
                "Pik nöqtədə 1 saniyə sinənizi maksimal bərkidib asta-asta geri qayıdın."
            ),
            targetTipsAz = "Hər təkrarın sonunda əlləri bir-birinin üstündən bir qədər keçirərək sinəni daha da bərk sıxa bilərsiniz.",
            commonMistakesAz = listOf(
                "Bədənlə yellənmək",
                "Həddindən artıq ağır çəki seçmək"
            )
        ),
        Exercise(
            id = "push_ups",
            nameAz = "Döşəmədən Jim (Push-Up)",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.CIYIN, MuscleGroup.ABS),
            animationType = ExerciseAnimationType.PUSH_UPS,
            lottieUrl = "https://assets9.lottiefiles.com/packages/lf20_q0hmtmhs.json",
            equipment = "Bədən Çəkisi / Mat",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "15-20",
            restSeconds = 60,
            descriptionAz = "Sinə, çiyin və triceps əzələlərini bədən çəkisi ilə dərindən gücləndirən, hər yerdə edilə bilən qızıl standart baza hərəkətidir.",
            executionStepsAz = listOf(
                "Əlləri döşəməyə çiyin enindən bir az geniş qoyun, barmaqlar irəli baxsın.",
                "Bədəni başdan ayağadək düz bir xətt halında saxlayın, qarnı və qalçanı sıxın.",
                "Nəfəs alaraq sinəniz döşəməyə toxunana qədər dirsəkləri 45 dərəcə bucaqla bükün.",
                "Nəfəs verərək yeri güclə itələyib başlanğıc vəziyyətinə qayıdın və sinəni sıxın."
            ),
            targetTipsAz = "Dirsəkləri çölə 90 dərəcə açmayın; 45 dərəcə bədənə yaxın bucaq çiyin oynaqlarını qoruyur və sinəni tam aktivləşdirir.",
            commonMistakesAz = listOf(
                "Belin aşağı sallanması",
                "Yarımçıq amplituda ilə hərəkət etmək",
                "Boyunu aşağı sallamaq"
            )
        ),

        // --- ÇİYİN ---
        Exercise(
            id = "db_shoulder_press",
            nameAz = "Oturaraq Qantellə Çiyin Presi",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.SINE),
            animationType = ExerciseAnimationType.SHOULDER_PRESS,
            equipment = "Qantellər & Dik Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-12",
            restSeconds = 75,
            descriptionAz = "Çiyinlərin ön və yan başlıqlarını gücləndirərək kürəvari və enli çiyin forması yaradır.",
            executionStepsAz = listOf(
                "Skamyanın söykənəcəyini 80-85 dərəcə qoyub oturun.",
                "Qantelləri qulaq səviyyəsində saxlayın, biləklər irəli baxsın.",
                "Nəfəs verərək qantelləri başınızın üzərində yuxarı qaldırın.",
                "Yuxarıda dirsəkləri kilidləmədən asta sürətlə aşağı endirin."
            ),
            targetTipsAz = "Qantelləri yuxarı qaldırarkən başınızın arxasına deyil, düz tavan istiqamətində itin.",
            commonMistakesAz = listOf(
                "Beli skamyadan həddən artıq qabartmaq",
                "Çəkiləri yellədərək qaldırmaq"
            )
        ),
        Exercise(
            id = "lateral_raise",
            nameAz = "Qantellə Yana Açış (Yan Çiyin)",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.LATERAL_RAISE,
            equipment = "Qantellər",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Çiyinin yan başını (lateral deltoid) izolə edərək çiyinləri vizual olaraq ən çox genişləndirən nömrə bir hərəkətdir.",
            executionStepsAz = listOf(
                "Ayaqlar çiyin enində, əllərdə yüngül-orta çəkidə qantellər.",
                "Dirsəkləri yüngülcə bükün və dirsəklərlə rəhbərlik edərək qolları yana çiyin hündürlüyünə qaldırın.",
                "Hərəkətin pik nöqtəsində çeçələ barmağınız baş barmaqdan bir az yuxarıda olsun (su tökmə effekti).",
                "Nəzarətlə və asta-asta aşağı endirin."
            ),
            targetTipsAz = "Ağır çəki götürməyin! Bu hərəkətdə əsas olan təkan deyil, yan çiyinin daimi yanmasıdır.",
            commonMistakesAz = listOf(
                "Bədənlə təkan verib yellənmək",
                "Trapezləri qulaqlara qədər dartmaq",
                "Qolları çiyin səviyyəsindən çox yuxarı qaldırmaq"
            )
        ),
        Exercise(
            id = "cable_face_pull",
            nameAz = "Kabeldə İplə Üzə Çəkmə (Face Pull)",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = listOf(MuscleGroup.KUREK),
            animationType = ExerciseAnimationType.SHOULDER_PRESS,
            equipment = "Kabel & İp",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "15-20",
            restSeconds = 60,
            descriptionAz = "Arxa çiyinləri (posterior deltoid) və rotasiya manjetini gücləndirərək duruşu düzəldir və çiyin zədələrinin qarşısını alır.",
            executionStepsAz = listOf(
                "Kabeli göz səviyyəsinə qoyun, ipi hər iki tərəfdən tutun.",
                "Bir addım geriyə çəkilin və dirsəkləri yuxarıda tutaraq ipi birbaşa alnınıza doğru dartın.",
                "Son nöqtədə əlləri arxaya açaraq arxa çiyinləri sıxın."
            ),
            targetTipsAz = "Dirsəklərinizin həmişə biləklərinizdən daha yuxarıda olmasına diqqət yetirin.",
            commonMistakesAz = listOf(
                "Çəkini sinəyə doğru çəkmək",
                "Arxaya tullanaraq bədənlə kömək etmək"
            )
        ),
        Exercise(
            id = "arnold_press",
            nameAz = "Arnold Çiyin Presi (Rotasiyalı)",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.SINE),
            animationType = ExerciseAnimationType.ARNOLD_PRESS,
            equipment = "Qantellər & Dik Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 75,
            descriptionAz = "Arnold Şvartseneqqer tərəfindən məşhurlaşdırılan, 180 dərəcə bilək rotasiyası ilə deltoidin hər 3 başını eyni anda yükləyən unikal çiyin hərəkəti.",
            executionStepsAz = listOf(
                "Skamyada dik oturun, qantelləri çənə səviyyəsində saxlayın, ovuclar üzünüzə baxsın.",
                "Qantelləri yuxarı itələdikcə biləklərinizi 180 dərəcə xaricə fırladın.",
                "Yuxarı pik nöqtədə ovuclar artıq irəli baxmalıdır.",
                "Eyni rotasiyanı tərs istiqamətdə təkrar edərək asta-asta çənə səviyyəsinə endirin."
            ),
            targetTipsAz = "Rotasiyanı hərəkətin ortasında deyil, qaldırma boyu axıcı və bərabər sürətlə tamamlayın.",
            commonMistakesAz = listOf(
                "Rotasiyanı qəfil və kəskin etmək",
                "Həddindən artıq ağır çəki götürüb formanı pozmaq"
            )
        ),
        Exercise(
            id = "front_raise",
            nameAz = "Qantellə Qabağa Qaldırma (Ön Çiyin)",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = listOf(MuscleGroup.SINE),
            animationType = ExerciseAnimationType.FRONT_RAISE,
            equipment = "Qantellər",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Ön deltoidi (anterior deltoid) birbaşa izolə edərək çiyinlərin ön hissəsinə yuvarlaq və qabarıq estetika qazandırır.",
            executionStepsAz = listOf(
                "Ayaqlar çiyin enində, qantelləri budların önündə saxlayın.",
                "Dirsəkləri yüngülcə dinc saxlayaraq qantelləri göz səviyyəsinə qədər qabağa qaldırın.",
                "Pik nöqtədə 1 saniyə fasilə verərək ön çiyinləri sıxın.",
                "Nəzarətlə və asta sürətlə aşağı salın."
            ),
            targetTipsAz = "Gövdə ilə yellənməyin; əgər inersiya ilə qaldırırsınızsa, çəkini azaldın.",
            commonMistakesAz = listOf(
                "Bədənlə yellənmək",
                "Qolları başdan yuxarı yelləmək"
            )
        ),

        // --- KÜRƏK ---
        Exercise(
            id = "lat_pulldown",
            nameAz = "Üst Blokdan Sinəyə Çəkmə",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.LAT_PULLDOWN,
            equipment = "Kabel / Blok Trenajoru",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 75,
            descriptionAz = "Kürək qanadlarını (latissimus dorsi) genişləndirən, V-şəkilli bədən quran ən məşhur kürək hərəkətidir.",
            executionStepsAz = listOf(
                "Trenajorda oturun, dizləri yastıqların altına möhkəm bərkidin.",
                "Dəstəyi geniş tutuşla götürün və bədəninizi yüngülcə (10-15 dərəcə) geriyə verin.",
                "Dirsəkləri yerə doğru çəkərək qanadları sıxın və dəstəyi sinənizin üst hissəsinə toxundurun.",
                "Asta sürətlə kürəyin dartılmasını hiss edərək qolları yuxarı açın."
            ),
            targetTipsAz = "Qollarınızla dartmayın; sanki dirsəklərinizlə arxadakı bir şeyi vurursunuz kimi hərəkət edin.",
            commonMistakesAz = listOf(
                "Çəkini başın arxasına çəkmək (boyun və çiyin üçün təhlükəlidir)",
                "Bədənlə çox arxaya əyilib yellənmək"
            )
        ),
        Exercise(
            id = "barbell_row",
            nameAz = "Ştanqla Əyilərək Çəkmə (Bent-Over Row)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.BENT_OVER_ROW,
            equipment = "Ştanq",
            difficulty = "Qabaqcıl",
            recommendedSets = 4,
            recommendedReps = "8-10",
            restSeconds = 90,
            descriptionAz = "Kürəyin qalınlığını, rombvari əzələləri və onurğa ətrafı təməl gücü artıran ən effektli baza hərəkətlərindəndir.",
            executionStepsAz = listOf(
                "Ayaqlar çiyin enində, ştanqı dizlərinizdən bir qədər aşağıda tutun.",
                "Dizləri bir az bükün, çanağı geriyə verin və onurğanı düz saxlayaraq bədəni 45 dərəcə əyin.",
                "Ştanqı qarına (göbək istiqamətinə) doğru çəkin və kürək pərlərini birləşdirin.",
                "Nəzarətlə ştanqı aşağı endirin."
            ),
            targetTipsAz = "Beli heç vaxt yumrulamayın! Sinə irəlidə, bel təbii daxili qövsdə olmalıdır.",
            commonMistakesAz = listOf(
                "Beli qozbelləşdirmək",
                "Bədəni dikəldib ştanqı yuxarı atmaq"
            )
        ),
        Exercise(
            id = "deadlift",
            nameAz = "Klassik Dedlift (Ştanq Qaldırma)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.AYAQ, MuscleGroup.ABS),
            animationType = ExerciseAnimationType.DEADLIFT,
            equipment = "Ştanq",
            difficulty = "Qabaqcıl",
            recommendedSets = 3,
            recommendedReps = "6-8",
            restSeconds = 120,
            descriptionAz = "Bütün bədənin arxa zəncirini (bel, kürək, qalça, hamstring) işlədən və testosteronu artıran kral hərəkət.",
            executionStepsAz = listOf(
                "Ayaqlar çiyin enində, qrif ayaq daraqlarının tam üzərində olsun.",
                "Dizləri bükün, çanağı arxaya atın, qrifdən iki əllə yapışın.",
                "Sinəni dikəldin, onurğanı düzləşdirin və dabanlarla yeri itələyərək ştanqı qaldırın.",
                "Yuxarıda dik durun, çanağı sıxın və ştanqı eyni düzlüklə aşağı endirin."
            ),
            targetTipsAz = "Ştanq bütün hərəkət boyunca ayaqlarınıza yaxın sürtünərək hərəkət etməlidir.",
            commonMistakesAz = listOf(
                "Beli yumru halda qaldırmaq",
                "Yuxarıda beli həddən artıq arxaya əymək"
            )
        ),
        Exercise(
            id = "pull_ups",
            nameAz = "Turnikdə Çəkinmə (Barfiks)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.PULL_UPS,
            equipment = "Turnik",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-12",
            restSeconds = 90,
            descriptionAz = "Kürək qanadlarını (lats) ən geniş açan, bədən çəkisi ilə güc və nisbi kütlə yaradan ən təbii və güclü hərəkət.",
            executionStepsAz = listOf(
                "Turnikdən çiyinlərdən bir qədər geniş tutuşla asılın, ayaqları dizdən yüngül çarpazlayın.",
                "Kürək pərlərini aşağı çəkərək qanadları aktivləşdirin.",
                "Sinənizi turnikə yaxınlaşdıraraq çənəniz qrifin üstünə çıxana qədər dartının.",
                "Qanadların tam dartıldığını hiss edərək nəzarətlə aşağı enin."
            ),
            targetTipsAz = "Turnikdə yellənməyin (kipping etməyin); təmiz qanad gücü ilə bədəni qaldırın.",
            commonMistakesAz = listOf(
                "Ayaqları təpikləyib inersiya ilə qalxmaq",
                "Çənəni turnikə çatdırmadan yarımçıq enmək"
            )
        ),
        Exercise(
            id = "seated_cable_row",
            nameAz = "Oturaraq Blokda Qarına Çəkmə",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.SEATED_CABLE_ROW,
            equipment = "Kabel & V-Dəstək",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 75,
            descriptionAz = "Kürəyin orta hissəsini, rombvari əzələləri və trapeziusun alt liflərini qalınlaşdıraraq relyefli arxa görünüş yaradır.",
            executionStepsAz = listOf(
                "Trenajorda oturun, dabanları dayaq lövhəsinə qoyun, dizləri yüngül bükülü saxlayın.",
                "V-dəstəyi tutun, belinizi dik saxlayaraq bir az irəli əyilib kürəyi gərin.",
                "Dəstəyi göbəyinizə doğru çəkərək dirsəkləri geriyə aparın və kürək pərlərini bərk sıxın.",
                "Asta sürətlə əlləri irəli uzadaraq qanadların dartılmasını hiss edin."
            ),
            targetTipsAz = "Çəkmə zamanı beli arxaya 45 dərəcə yelləməyin; gövdəni 80-90 dərəcədə stabil saxlayın.",
            commonMistakesAz = listOf(
                "Bədənlə güclü yellənmək",
                "Dəstəyi sinəyə çəkib çiyinləri qaldırmaq"
            )
        ),

        // --- BİCEPS ---
        Exercise(
            id = "barbell_curl",
            nameAz = "Ştanqla Biceps Qaldırma",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.BICEP_CURL,
            lottieUrl = "https://assets10.lottiefiles.com/packages/lf20_7fy2y3um.json",
            equipment = "Ştanq (Düz və ya EZ)",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "8-12",
            restSeconds = 60,
            descriptionAz = "Bicepsin ümumi həcmi və gücü üçün ən klassik baza hərəkətidir. Hər iki başı intensiv işlədir.",
            executionStepsAz = listOf(
                "Ayaqlar sabit, dirsəklər bədəninizin yanlarına sıxılmış vəziyyətdə durun.",
                "Ştanqı altdan tutun və nəfəs verərək çiyin istiqamətinə qaldırın.",
                "Yuxarıda bicepsi 1 saniyə bərk sıxın.",
                "Nəfəs alaraq ştanqı asta-asta (təxminən 2-3 saniyəyə) aşağı salın."
            ),
            targetTipsAz = "Dirsəklərinizi irəli-geri oynatmayın; dirsək oynağı fırlanma oxu kimi yerində donmalıdır.",
            commonMistakesAz = listOf(
                "Beli arxaya əyərək inersiya ilə qaldırmaq",
                "Ştanqı tez aşağı buraxmaq"
            )
        ),
        Exercise(
            id = "hammer_curl",
            nameAz = "Qantellə Çəkic Qaldırma (Hammer Curl)",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = listOf(MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.HAMMER_CURL,
            equipment = "Qantellər",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Qolun braxialis (brachialis) əzələsini və biləyi işlədərək qola yandan qalın və dolu görüntü verir.",
            executionStepsAz = listOf(
                "Qantelləri ovuc içləri bir-birinə baxacaq şəkildə neytral tutuşla götürün.",
                "Dirsəkləri bədənə yapışdıraraq qantelləri yuxarı bükün.",
                "Pik nöqtədə sıxın və nəzarətlə endirin."
            ),
            targetTipsAz = "Bu hərəkət bicepsi altından itələyərək onun daha hündür pik yaratmasına kömək edir.",
            commonMistakesAz = listOf(
                "Biləkləri burmaq",
                "Çiyinləri qabağa atmaq"
            )
        ),
        Exercise(
            id = "preacher_curl",
            nameAz = "Skot Skamyasında Biceps (Preacher Curl)",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.PREACHER_CURL,
            equipment = "EZ Ştanq & Skot Skamyası",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Qolları meyilli yastığa söykəyərək inersiyanı tam sıfırlayan və bicepsin qısa başını maksimal dərəcədə izolə edən hərəkət.",
            executionStepsAz = listOf(
                "Skot skamyasında oturun, qoltuqaltı nahiyəni yastığın yuxarı kənarına möhkəm sıxın.",
                "EZ ştanqı altdan tutuşla götürün.",
                "Bicepsləri bükərək ştanqı çənə istiqamətində yuxarı qaldırın və pik nöqtədə sıxın.",
                "Asta-asta, dirsəyi tam kilidləmədən qolu aşağı açın."
            ),
            targetTipsAz = "Aşağıda dirsəkləri qəfil və tam kilidləməyin, bu dirsək vətərlərini qoruyar.",
            commonMistakesAz = listOf(
                "Aşağı nöqtədə dirsəyi ifrat açmaq",
                "Oturacaqdan ayağa qalxaraq bədənlə kömək etmək"
            )
        ),

        // --- TRİCEPS ---
        Exercise(
            id = "tricep_pushdown",
            nameAz = "Kabeldə İplə Triceps Açma (Pushdown)",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.TRICEP_PUSHDOWN,
            equipment = "Kabel & İp / Düz Dəstək",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Tricepsin yan və medial başını kəskinləşdirərək nal formasını (horseshoe) qabardır.",
            executionStepsAz = listOf(
                "Trenajorun qarşısında durun, dirsəkləri 90 dərəcə bükərək bədəninizə sabitləyin.",
                "Nəfəs verərək ipi tam aşağıya doğru itin.",
                "Alt nöqtədə ipin uclarını yana açaraq tricepsi bərk sıxın.",
                "Asta-asta 90 dərəcəyə qədər geri buraxın."
            ),
            targetTipsAz = "Yalnız bilək və dirsək hərəkət etməlidir; çiyinləriniz və bədəniniz tamamilə hərəkətsiz qalmalıdır.",
            commonMistakesAz = listOf(
                "Dirsəkləri qabağa-arxaya yelləmək",
                "Çəkinin üzərinə bədənlə yıxılmaq"
            )
        ),
        Exercise(
            id = "skull_crusher",
            nameAz = "Skamyada Alına Açma (Skull Crusher)",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.SKULL_CRUSHER,
            equipment = "EZ Ştanq & Skamya",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 75,
            descriptionAz = "Tricepsin uzun başını (long head) dərindən gərərək qolun arxa kütləsini ən çox böyüdən hərəkətdir.",
            executionStepsAz = listOf(
                "Skamyaya uzanın, EZ ştanqı yuxarıda şaquli tutun.",
                "Dirsəkləri yerində sabit saxlayaraq ştanqı alnınıza və ya başınızın arxasına tərəf endirin.",
                "Tricepslərin gərildiyini hiss edib ştanqı dirsəklərdən tam açaraq yuxarı qaytarın."
            ),
            targetTipsAz = "Qolları şaquli deyil, bir az başa doğru (75 dərəcə) meyilli saxlasanız tricepsdə gərginlik heç vaxt itməz.",
            commonMistakesAz = listOf(
                "Dirsəkləri çox gen açmaq",
                "Ştanqı sürətlə alına endirmək"
            )
        ),
        Exercise(
            id = "parallel_dips",
            nameAz = "Paralel Qollarda Qalxma (Dips)",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = listOf(MuscleGroup.SINE, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.DIPS,
            equipment = "Paralel Qollar (Brus)",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-12",
            restSeconds = 90,
            descriptionAz = "Tricepsin 3 başını, alt sinəni və ön deltoidi bədən çəkisi ilə intensiv yükləyən ən effektiv itələmə bazalarından biridir.",
            executionStepsAz = listOf(
                "Paralel dəstəklərdən yapışın və qollarınızı düzləşdirərək havaya qalxın.",
                "Bədəni dik saxladıqda yük tricepsə, 15-20 dərəcə irəli əydikdə isə alt sinəyə düşür.",
                "Nəfəs alaraq dirsəklər 90 dərəcə bucaq əmələ gətirənədək bədəni nəzarətlə aşağı endirin.",
                "Nəfəs verərək tricepslərlə güclü təkan verib başlanğıc vəziyyətinə qayıdın."
            ),
            targetTipsAz = "Çiyinləri qulaqlara doğru sallamayın, boynu dik və çiyin pərlərini stabil saxlayın.",
            commonMistakesAz = listOf(
                "Çox dərindən enib çiyini travmaya salmaq",
                "Ayaqları qabağa-arxaya yelləmək"
            )
        ),
        Exercise(
            id = "overhead_triceps",
            nameAz = "Baş Arxasından Qantel Açma (Overhead)",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.OVERHEAD_TRICEPS,
            equipment = "Qantel & Skamya",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Qolu yuxarı qaldıraraq tricepsin uzun başını (long head) anatomik olaraq ən maksimal dartılma vəziyyətinə salan əsas hərəkət.",
            executionStepsAz = listOf(
                "Dik oturun və ya ayaq üstə durun, qanteli hər iki əllə baş barmaqların arasından yuxarıda tutun.",
                "Dirsəkləri başınızın yanlarına sıxaraq irəli yönəldin.",
                "Dirsəkləri bükərək qanteli boyunun arxasına doğru asta-asta endirin.",
                "Tricepsləri sıxaraq qanteli yenidən başın üzərinə qaldırın."
            ),
            targetTipsAz = "Dirsəklərin çölə gen açılmasına imkan verməyin; dirsəklər tavana və irəli baxmalıdır.",
            commonMistakesAz = listOf(
                "Dirsəkləri yana çox açmaq",
                "Beli arxaya həddən artıq əymək"
            )
        ),

        // --- AYAQ ---
        Exercise(
            id = "barbell_squat",
            nameAz = "Ştanqla Çömbəlmə (Barbell Squat)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = listOf(MuscleGroup.ABS, MuscleGroup.KUREK),
            animationType = ExerciseAnimationType.SQUAT,
            lottieUrl = "https://assets2.lottiefiles.com/packages/lf20_9x9qtxoa.json",
            equipment = "Ştanq & Çömbəlmə Stendi",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-10",
            restSeconds = 90,
            descriptionAz = "Ayaqların (kvadriceps, hamstring, qalça) və bütün bədən gücünün təməl dayağıdır. Metabolizmi ən çox alovlandıran hərəkətdir.",
            executionStepsAz = listOf(
                "Ştanqı kürəyin yuxarı trapezius əzələsi üzərinə qoyun.",
                "Ayaqları çiyin genişliyində qoyun, ayaq barmaqlarını yüngülcə (15-30 dərəcə) çölə yönəldin.",
                "Nəfəs alıb çanağı arxaya verərək sanki stula otururmuş kimi budlar yerə paralel olanadək enin.",
                "Dabanlardan təkan verərək və nəfəsi çıxararaq dikəlin."
            ),
            targetTipsAz = "Dizləriniz içəri batmamalıdır; həmişə ayaq barmaqlarınızın istiqamətində çölə açılmalıdır.",
            commonMistakesAz = listOf(
                "Dabanları yerdən qaldırmaq",
                "Beli qabağa həddindən artıq əymək",
                "Dizləri içəri qatlamaq"
            )
        ),
        Exercise(
            id = "romanian_deadlift",
            nameAz = "Rumın Dedlifti (Hamstring & Qalça)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = listOf(MuscleGroup.KUREK),
            animationType = ExerciseAnimationType.DEADLIFT,
            equipment = "Ştanq və ya Qantellər",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 75,
            descriptionAz = "Budun arxa əzələlərini (hamstrings) və qalçanı gücləndirən ən yaxşı dartılma hərəkətidir.",
            executionStepsAz = listOf(
                "Dik durun, ştanqı əllərinizdə saxlayın, dizləri bir az yumşaldın (tam bükməyin).",
                "Çanağınızı arxaya doğru itələyərək ştanqı dizlərinizdən aşağıya qədər asta-asta sürüşdürün.",
                "Ayaqların arxasında kəskin gərilmə hiss etdikdə qalçanı sıxaraq dikəlin."
            ),
            targetTipsAz = "Hərəkət dizlərin bükülməsi ilə deyil, çanağın arxaya getməsi (hip hinge) ilə baş verir.",
            commonMistakesAz = listOf(
                "Beli bükmək",
                "Dizləri skvat kimi çox qatlamaq"
            )
        ),
        Exercise(
            id = "leg_extension",
            nameAz = "Trenajorda Ayaq Açma (Leg Extension)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.LEG_EXTENSION,
            equipment = "Trenajor",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Kvadriceps əzələsini birbaşa izolə edərək diz ətrafı bağları və ön bud relyefini gücləndirir.",
            executionStepsAz = listOf(
                "Trenajorda oturun, arxalığı və ayaq yastığını topuqlara uyğun nizamlayın.",
                "Dizlərdən qüvvət alaraq ayaqları tam düz vəziyyətə qədər qaldırın.",
                "Yuxarıda 1 saniyə saxlayıb asta sürətlə aşağı qaytarın."
            ),
            targetTipsAz = "Aşağı endirərkən çəkilərin bir-birinə dəyməsinə imkan verməyin, gərginliyi saxlayın.",
            commonMistakesAz = listOf(
                "Təkanla ayaqları yuxarı atmaq",
                "Çanağı oturacaqdan qaldırmaq"
            )
        ),
        Exercise(
            id = "calf_raise",
            nameAz = "Qalxaraq Baldır Qaldırma (Calf Raise)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.CALF_RAISE,
            equipment = "Pillə / Trenajor",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "15-20",
            restSeconds = 45,
            descriptionAz = "Ayağın baldır (gastrocnemius və soleus) əzələlərini tam amplituda ilə inkişaf etdirir.",
            executionStepsAz = listOf(
                "Ayaq uclarını pillənin və ya blokun kənarına qoyun, dabanlar sərbəst qalsın.",
                "Dabanları aşağı salaraq baldırların dərin dartılmasını təmin edin.",
                "Barmaq uclarında maksimal yüksəkliyə qalxaraq baldırları sıxın."
            ),
            targetTipsAz = "Aşağıda 1 saniyə tam gərilmə, yuxarıda 1 saniyə tam sıxılma baldır inkişafının sirridir.",
            commonMistakesAz = listOf(
                "Yellənmək və yaylanmaq",
                "Kiçik amplituda ilə etmək"
            )
        ),
        Exercise(
            id = "leg_press",
            nameAz = "45° Meyilli Ayaq Presi (Leg Press)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.LEG_PRESS,
            equipment = "Ayaq Pres Trenajoru",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 90,
            descriptionAz = "Onurğaya ağırlıq salmadan böyük çəkilərlə kvadriceps və bud əzələlərini təhlükəsiz və intensiv inkişaf etdirməyə imkan verir.",
            executionStepsAz = listOf(
                "Trenajorun oturacağında möhkəm əyləşin, beli arxalığa yapışdırın.",
                "Pəncələri platformaya çiyin genişliyində yerləşdirin, təhlükəsizlik linglərini açın.",
                "Nəfəs alaraq platformanı dizlər 90 dərəcə bucaq əmələ gətirənədək asta-asta yaxınlaşdırın.",
                "Dabanlardan güc alaraq platformanı yuxarı itin (dirsəkləri və dizləri tam kilidləmədən)."
            ),
            targetTipsAz = "Yuxarıda dizləri heç vaxt tam 'şaqqıldadıb' kilidləməyin; 5 dərəcə bükük saxlayın ki, yük oynağa yox, əzələyə qalsın.",
            commonMistakesAz = listOf(
                "Çanağı oturacaqdan qaldırmaq (bel üçün təhlükəlidir)",
                "Dizləri tam kilidləmək"
            )
        ),
        Exercise(
            id = "bulgarian_split_squat",
            nameAz = "Bolqar Skvatı (Bulgarian Split Squat)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = listOf(MuscleGroup.ABS),
            animationType = ExerciseAnimationType.BULGARIAN_SPLIT_SQUAT,
            equipment = "Qantellər & Skamya",
            difficulty = "Qabaqcıl",
            recommendedSets = 3,
            recommendedReps = "8-10 (hər ayağa)",
            restSeconds = 75,
            descriptionAz = "Hər ayağı ayrı-ayrılıqda işlədərək əzələ asimmetriyasını aradan qaldırır, kvadriceps və qalça əzələlərini dərindən formalaşdırır.",
            executionStepsAz = listOf(
                "Skamyanın önündə durun, bir ayağın üstünü arxadakı skamyaya qoyun.",
                "Əllərdə qantellər olmaqla ön ayağın dabanına fokuslanaraq aşağı çömbəlin.",
                "Ön bud yerə paralel olana və arxa diz döşəməyə yaxınlaşana qədər enin.",
                "Ön dabanla yeri itələyərək başlanğıc vəziyyətinə dikəlin."
            ),
            targetTipsAz = "Ön ayağınızı skamyadan kifayət qədər irəli qoyun ki, enərkən diz ayaq barmaqlarını çox qabaqlamasın.",
            commonMistakesAz = listOf(
                "Müvazinəti itirmək üçün həddindən artıq ağır çəki seçmək",
                "Arxa ayağa çox yük salmaq"
            )
        ),
        Exercise(
            id = "lying_leg_curl",
            nameAz = "Qarnıüstə Ayaq Bükmə (Hamstring Curl)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.LYING_LEG_CURL,
            equipment = "Trenajor",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Budun arxa iki-başlı əzələsini (hamstrings) izolə edərək diz oynağını möhkəmləndirir və arxa ayaq relyefini kəskinləşdirir.",
            executionStepsAz = listOf(
                "Trenajora qarnıüstə uzanın, diyircəkli yastığı topuqların arxasına yerləşdirin.",
                "Dəstəklərdən iki əllə möhkəm yapışın, çanağı skamyaya sıxın.",
                "Ayaqları bükərək diyircəyi qalçaya doğru qaldırın və pik nöqtədə 1 saniyə saxlayın.",
                "Nəzarətlə və asta sürətlə aşağı qaytarın."
            ),
            targetTipsAz = "Ayaqları qaldırarkən çanağınızı skamyadan yuxarı qaldırmayın; çanaq daima skamyaya sıxılmalıdır.",
            commonMistakesAz = listOf(
                "Təkanla yelləyib qaldırmaq",
                "Çanağı yuxarı qaldırmaq"
            )
        ),

        // --- ABS / QARIN ---
        Exercise(
            id = "hanging_leg_raise",
            nameAz = "Turnikdə Ayaq Qaldırma",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.AYAQ),
            animationType = ExerciseAnimationType.LEG_RAISE,
            equipment = "Turnik",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-15",
            restSeconds = 60,
            descriptionAz = "Qarının aşağı hissəsini və dərin kor əzələlərini ən güclü stimullaşdıran hərəkətdir.",
            executionStepsAz = listOf(
                "Turnikdən sallanın, çiyinlərinizi sabit saxlayın.",
                "Yellənmədən, qarın əzələlərini bükərək dizləri və ya düz ayaqları 90 dərəcə və daha yuxarı qaldırın.",
                "Çanağı yüngülcə yuxarı büküb qarnı sıxın və asta-asta aşağı endirin."
            ),
            targetTipsAz = "Sadəcə ayaqları qaldırmayın; çanağınızı qabırğalarınıza doğru bükməyə çalışın.",
            commonMistakesAz = listOf(
                "Bədənlə yellənərək təkan almaq",
                "Ayaqları nəzarətsiz aşağı buraxmaq"
            )
        ),
        Exercise(
            id = "ab_crunch",
            nameAz = "Döşəmədə Klassik Qarın Bükmə (Crunch)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.ABS_CRUNCH,
            equipment = "Mat / Döşəmə",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "15-20",
            restSeconds = 45,
            descriptionAz = "Düz qarın əzələsinin (rectus abdominis) yuxarı və orta kubiklərini formalaşdırır.",
            executionStepsAz = listOf(
                "Arxası üstə uzanın, dizləri bükün, pəncələri yerə qoyun.",
                "Əlləri qulaqların arxasına qoyun (boyunu dartmayın).",
                "Nəfəs verərək kürək sümüklərini yerdən bir neçə santimetr qaldırın və qarnı sıxın.",
                "Nəfəs alaraq asta-asta geri qayıdın."
            ),
            targetTipsAz = "Boynunuzdan dartmayın; gözlərinizi tavandakı tək bir nöqtəyə dikin və çənə ilə sinə arasında tennis topu qədər məsafə saxlayın.",
            commonMistakesAz = listOf(
                "Əllərlə başı qabağa itələmək",
                "Beli yerdən tam qaldırmaq"
            )
        ),
        Exercise(
            id = "plank",
            nameAz = "Dirsəklərdə Statik Plank",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.CIYIN, MuscleGroup.KUREK),
            animationType = ExerciseAnimationType.PLANK,
            lottieUrl = "https://assets4.lottiefiles.com/packages/lf20_4kfl4d3m.json",
            equipment = "Mat",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "45-60 san",
            restSeconds = 45,
            descriptionAz = "Daxili köndələn qarın əzələsini, onurğa sabitliyini və bədənin ümumi dözümlülüyünü artırır.",
            executionStepsAz = listOf(
                "Dirsəklər çiyinlərin altında olmaqla qollar üzərində durun.",
                "Ayaq barmaqları üzərində durun, bədəni başdan dabanadək düz bir xətt kimi saxlayın.",
                "Qarnı içəri çəkin, qalçanı bərk sıxın və dərin, nizamlı nəfəs alın."
            ),
            targetTipsAz = "Beli aşağı sallamayın və ya çanağı göyə qaldırmayın; bədən taxta lövhə kimi dümdüz olmalıdır.",
            commonMistakesAz = listOf(
                "Belin sallanması (onurğaya ziyan vurur)",
                "Nəfəsi saxlamaq"
            )
        ),
        Exercise(
            id = "bicycle_crunch",
            nameAz = "Velosiped Qarın Hərəkəti (Bicycle Crunch)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.BICYCLE_CRUNCH,
            equipment = "Mat",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "20-25 (hər tərəfə)",
            restSeconds = 45,
            descriptionAz = "San Diego Dövlət Universitetinin tədqiqatına görə qarın və yan qarın (obliques) liflərini ən yüksək faizlə aktivləşdirən 1 nömrəli qarın hərəkəti.",
            executionStepsAz = listOf(
                "Arxası üstə uzanın, əlləri başın arxasına qoyun, dizləri 90 dərəcə qaldırın.",
                "Sağ dirsəyi irəli apararaq sol dizə doğru bükün, bu zaman sağ ayağı düz irəli uzadın.",
                "Dərhal axıcı şəkildə əks istiqamətə dəyişin: sol dirsək sağ dizə toxunur.",
                "Hərəkəti tələsmədən, velosiped pedalı fırladır kimi nizamlı nəfəslə icra edin."
            ),
            targetTipsAz = "Sadəcə dirsəkləri yelləməyin; bütün döş qəfəsini fırladaraq çiyini yerdən qaldırın.",
            commonMistakesAz = listOf(
                "Boyundan ikiəlli dartmaq",
                "Hərəkəti həddən artıq sürətlə və nəzarətsiz etmək"
            )
        ),
        Exercise(
            id = "russian_twist",
            nameAz = "Rus Qıvrılması (Russian Twist)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.RUSSIAN_TWIST,
            equipment = "Mat / Tibbi Top / Qantel",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "20 dövrə",
            restSeconds = 45,
            descriptionAz = "Yan qarın əzələlərini (obliques) və kor sabitliyini dinamik fırlanma gücü ilə formalaşdırır, bel ətrafını bərkidir.",
            executionStepsAz = listOf(
                "Döşəmədə oturun, dizləri bükün, ayaqları döşəmədən 10 sm yuxarı qaldırın (V-vəziyyəti).",
                "Gövdəni 45 dərəcə arxaya əyin və qarın əzələlərini gərgin saxlayın.",
                "Əlləri (və ya çəkini) sinə önündə saxlayaraq gövdəni ardıcıl sağa və sola fırladın.",
                "Hər fırlanmada döşəməyə yaxın toxunun və yan qarın əzələlərini sıxın."
            ),
            targetTipsAz = "Dizlərin sağa-sola həddən artıq yellənməsinə imkan verməyin; hərəkət beldən və qabırğalardan olmalıdır.",
            commonMistakesAz = listOf(
                "Beli qozbelləşdirmək",
                "Ayaqları döşəməyə salmaq"
            )
        ),
        // --- YENİ ƏLAVƏ EDİLMİŞ HƏRƏKƏTLƏR ---
        Exercise(
            id = "decline_bench_press",
            nameAz = "Aşağı Meyilli Ştanq Presi",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.DECLINE_BENCH_PRESS,
            equipment = "Ştanq & Aşağı Meyilli Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-12",
            restSeconds = 75,
            descriptionAz = "Sinənin alt xəttini və kənar kəsiyini dəqiq vurğulayır, çiyin oynağına düşən təzyiqi azaldaraq ağır çəkilərlə işləməyə imkan verir.",
            executionStepsAz = listOf(
                "Aşağı meyilli skamyada ayaqlarınızı kilidləyin və arxası üstə uzanın.",
                "Ştanqı çiyin enindən bir az geniş tutun.",
                "Nəfəs alaraq ştanqı sinənizin alt hissəsinə qədər endirin.",
                "Nəfəs verərək yuxarı qaldırın və sinənin alt liflərini sıxın."
            ),
            targetTipsAz = "Ştanqı qarına deyil, birbaşa alt döş xəttinə endirin.",
            commonMistakesAz = listOf("Ştanqı boyuna tərəf qaldırmaq", "Çanağı skamyadan ayırmaq")
        ),
        Exercise(
            id = "pec_deck_fly",
            nameAz = "Kəpənək Trenajoru (Pec Deck)",
            primaryMuscle = MuscleGroup.SINE,
            secondaryMuscles = listOf(MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.PEC_DECK_FLY,
            equipment = "Pec Deck Trenajoru",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Sinə əzələlərini təcrid olunmuş şəkildə yığır, daxili sinə cizgisini və maksimal sıxılmanı təmin edir.",
            executionStepsAz = listOf(
                "Oturacağın hündürlüyünü elə tənzimləyin ki, dirsəklər sinə səviyyəsində olsun.",
                "Qolları dirsəkdən azca bükülü saxlayaraq dəstəkləri önünüzdə qovuşdurun.",
                "Öndə 1 saniyə saxlayaraq sinəni bərk sıxın və nəzarətlə açın."
            ),
            targetTipsAz = "Çiyinləri qaldırmayın, kürəkləri oturacağa möhkəm sıxın.",
            commonMistakesAz = listOf("Dirsəkləri çox arxaya aparmaq", "Sinə əvəzinə çiyinlə itələmək")
        ),
        Exercise(
            id = "close_grip_bench",
            nameAz = "Dar Tutuşla Ştanq Presi",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = listOf(MuscleGroup.SINE, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.CLOSE_GRIP_BENCH_PRESS,
            equipment = "Ştanq & Düz Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-10",
            restSeconds = 75,
            descriptionAz = "Trisepsin kütlə və baza gücünü artıran əsas hərəkətlərdən biridir, qollara qalınlıq bəxş edir.",
            executionStepsAz = listOf(
                "Düz skamyaya uzanın, ştanqı təxminən çiyin genişliyində (20-30 sm) tutun.",
                "Dirsəkləri bədənə yaxın saxlayaraq ştanqı sinənin aşağı hissəsinə endirin.",
                "Trisepslərin gücü ilə ştanqı yuxarı itin və qolları tam açın."
            ),
            targetTipsAz = "Əlləri bir-birinə çox yaxın tutmayın (10 sm-dən az), bu biləkləri yükləyər.",
            commonMistakesAz = listOf("Dirsəkləri kənara açmaq", "Həddindən artıq dar tutmaq")
        ),
        Exercise(
            id = "concentration_curl",
            nameAz = "Konsentrasiya Biseps Bükməsi",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.CONCENTRATION_CURL,
            equipment = "Qantel & Skamya",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Bisepsin təpə nöqtəsini (peak) formalaşdıran və heç bir inersiyaya yol verməyən ən təmiz izolə hərəkətidir.",
            executionStepsAz = listOf(
                "Skamyada oturun, işçi qolun dirsəyini eyni tərəfdəki budun daxili hissəsinə söykəyin.",
                "Qanteli asta-asta yuxarı qaldıraraq bisepsi maksimal bükün.",
                "Pikdə 1 saniyə saxlayıb asta şəkildə başlanğıca endirin."
            ),
            targetTipsAz = "Bədənlə heç bir kömək etməyin, dirsək budda tərpənməz qalmalıdır.",
            commonMistakesAz = listOf("Dirsəyi yerdən qaldırmaq", "Qanteli aşağı buraxanda tez atmaq")
        ),
        Exercise(
            id = "incline_db_curl",
            nameAz = "Meyilli Skamyada Qantellə Biseps",
            primaryMuscle = MuscleGroup.BICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.INCLINE_DUMBBELL_CURL,
            equipment = "Qantellər & 45-60° Skamya",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Bisepsin uzun başını (long head) başlanğıc vəziyyətində dərindən dartır və qolun ön profilini dolğunlaşdırır.",
            executionStepsAz = listOf(
                "45-60 dərəcə meyilli skamyaya arxası üstə uzanın, qolları tam aşağı sallayın.",
                "Dirsəkləri geridə saxlayaraq qantelləri yuxarı bükün və biləkləri yüngülcə xaricə fırladın (supinasiya).",
                "Yavaşca əvvəlki gərilmə vəziyyətinə qayıdın."
            ),
            targetTipsAz = "Dirsəklərin önə gəlməsinə imkan verməyin, dartılma hissini itirməyin.",
            commonMistakesAz = listOf("Çiyinləri önə əymək", "Ağır çəki ilə yellənmək")
        ),
        Exercise(
            id = "t_bar_row",
            nameAz = "T-Qrif Kürək Dartması (T-Bar Row)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.BICEPS, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.T_BAR_ROW,
            equipment = "T-Bar Trenajoru / Ştanq",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "8-12",
            restSeconds = 75,
            descriptionAz = "Kürəyin orta hissəsini, romboidləri və qanadların qalınlığını artıran ən effektli güc hərəkətlərindəndir.",
            executionStepsAz = listOf(
                "Platformada durun, dizləri bükün və beli 45 dərəcə düz vəziyyətdə saxlayın.",
                "Dəstəyi neytral tutuşla götürün və nəfəs verərək qarnınıza doğru çəkin.",
                "Kürək sümüklərini arxada bir-birinə sıxın və yavaşca endirin."
            ),
            targetTipsAz = "Beli əsla qozbelləşdirməyin, ağırlığı kürəklə çəkin, qollarla yox.",
            commonMistakesAz = listOf("Beli əymək", "Ayaqlarla hoppanaraq təkan vermək")
        ),
        Exercise(
            id = "dumbbell_row",
            nameAz = "Tək Qolla Qantel Dartması (Mişar)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.BICEPS),
            animationType = ExerciseAnimationType.DUMBBELL_ROW,
            equipment = "Qantel & Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Hər iki tərəfi ayrı-ayrılıqda məşq etdirərək əzələ asimmetriyasını aradan qaldırır və geniş hərəkət amplitudası yaradır.",
            executionStepsAz = listOf(
                "Bir dizinizi və əlinizi skamyaya qoyun, gövdəniz döşəməyə paralel olsun.",
                "Digər əllə qanteli götürün və dirsəyi yuxarı-geriyə doğru çanağa tərəf çəkin.",
                "Yuxarıda qanad əzələsini sıxın və yavaş-yavaş aşağı endirin."
            ),
            targetTipsAz = "Qanteli sinəyə deyil, çanağa doğru çəkin ki, qanad əzələsi tam yığılsın.",
            commonMistakesAz = listOf("Gövdəni fırlatmaq", "Qanteli sadəcə qolla yuxarı qaldırmaq")
        ),
        Exercise(
            id = "barbell_shrug",
            nameAz = "Ştanqla Şraq (Trapesiya)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.BARBELL_SHRUG,
            equipment = "Ştanq",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Yuxarı trapesiya əzələlərini hədəf alır, boyun və çiyin nahiyəsinə güclü və qalın görünüş verir.",
            executionStepsAz = listOf(
                "Düz durun, ştanqı çiyin enində tutun, qolları tam sərbəst saxlayın.",
                "Qolları bükmədən, sadəcə çiyinlərinizi qulaqlarınıza doğru maksimal yuxarı qaldırın.",
                "Yuxarı nöqtədə 1 saniyə sıxıb asta şəkildə aşağı endirin."
            ),
            targetTipsAz = "Çiyinləri dairəvi fırlatmayın; bu oynaqlara ziyan vurur, yalnız şaquli yuxarı-aşağı hərəkət edin.",
            commonMistakesAz = listOf("Çiyinləri fırlatmaq", "Dirsəkləri bükmək")
        ),
        Exercise(
            id = "cable_lateral_raise",
            nameAz = "Kabel Blokda Yan Çiyin",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.CABLE_LATERAL_RAISE,
            equipment = "Kabel Trenajoru",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Aşağı blokdan dartma sayəsində hərəkətin lap başından sonuna qədər orta deltada fasiləsiz gərginlik saxlayır.",
            executionStepsAz = listOf(
                "Kabel blokunun yanında durun, aşağı dəstəyi uzaq əlinizlə tutun.",
                "Qolu dirsəkdən cüzi bükərək yana çiyin səviyyəsinə qədər qaldırın.",
                "Nəzarətlə və yavaş-yavaş başlanğıca endirin."
            ),
            targetTipsAz = "Çəkini qaldırarkən çeçələ barmağınızı bir qədər yuxarı yönəldin.",
            commonMistakesAz = listOf("Bədənlə yellənmək", "Qolu çiyin səviyyəsindən çox yuxarı qaldırmaq")
        ),
        Exercise(
            id = "upright_row",
            nameAz = "Qantellə Çənəyə Qaldırma",
            primaryMuscle = MuscleGroup.CIYIN,
            secondaryMuscles = listOf(MuscleGroup.KUREK, MuscleGroup.BICEPS),
            animationType = ExerciseAnimationType.UPRIGHT_ROW,
            equipment = "Qantellər",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "10-12",
            restSeconds = 60,
            descriptionAz = "Yan deltaları və yuxarı trapesiyanı birlikdə işlədərək çiyinlərə enli və hündür siluet verir.",
            executionStepsAz = listOf(
                "Düz dayanın, qantelləri budların önündə saxlayın.",
                "Dirsəkləri rəhbər tutaraq qantelləri sinə/çənə istiqamətinə doğru qaldırın (dirsəklər biləklərdən hündür olmalıdır).",
                "Nəzarətlə aşağı endirin."
            ),
            targetTipsAz = "Dirsəkləri həmişə biləklərdən yüksəkdə tutun.",
            commonMistakesAz = listOf("Həddindən artıq ağır çəki seçmək", "Dirsəkləri aşağı salmaq")
        ),
        Exercise(
            id = "hack_squat",
            nameAz = "Qakk Skvat Trenajoru (Hack Squat)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.HACK_SQUAT,
            equipment = "Hack Squat Sled Trenajoru",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 90,
            descriptionAz = "Kürəyi tam dəstəkləyərək belə düşən yükü minimuma endirir və bütün gərginliyi birbaşa budun ön dördbashlı əzələsinə yönəldir.",
            executionStepsAz = listOf(
                "Kürəyinizi və çiyinlərinizi trenajorun yastığına möhkəm sıxın.",
                "Ayaqları platformada çiyin enində yerləşdirin və təhlükəsizlik dəstəyini açın.",
                "Dizləri 90 dərəcə bucaq yaradanadək bükərək asta-asta oturun.",
                "Dabanlarla platformanı itələyərək başlanğıc vəziyyətinə qayıdın."
            ),
            targetTipsAz = "Hərəkətin yuxarı hissəsində dizlərinizi tam kilidləməyin, gərginliyi əzələdə saxlayın.",
            commonMistakesAz = listOf("Dabanları platformadan qaldırmaq", "Dizləri daxilə əymək")
        ),
        Exercise(
            id = "barbell_hip_thrust",
            nameAz = "Ştanqla Çanaq Qaldırma (Hip Thrust)",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.HIP_THRUST,
            equipment = "Ştanq, Yastıq & Skamya",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "10-12",
            restSeconds = 90,
            descriptionAz = "Böyük sağrı (gluteus maximus) əzələsinin həcmini və formasını artıran 1 nömrəli elmi sübut olunmuş hərəkətdir.",
            executionStepsAz = listOf(
                "Kürəyin yuxarı hissəsini skamyaya söykəyin, ştanqı çanaq üzərinə (yastıqla) qoyun.",
                "Dabanları yerə basaraq çanağı yuxarı qaldırın və gövdə ilə düz xətt yaradın.",
                "Yuxarıda çanaq əzələlərini 2 saniyə bərk sıxın və aşağı endirin."
            ),
            targetTipsAz = "Çənəni sinəyə yaxın saxlayın və irəliyə baxın, belinizi arxaya həddən artıq qövs etməyin.",
            commonMistakesAz = listOf("Beli arxaya qırmaq", "Ayaqları çox uzağa qoymaq")
        ),
        Exercise(
            id = "seated_calf_raise",
            nameAz = "Oturaq Baldır Trenajoru",
            primaryMuscle = MuscleGroup.AYAQ,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.SEATED_CALF_RAISE,
            equipment = "Seated Calf Trenajoru",
            difficulty = "Başlanğıc",
            recommendedSets = 4,
            recommendedReps = "15-20",
            restSeconds = 45,
            descriptionAz = "Oturaq vəziyyətdə dizlər bükülü olduğu üçün dərində yerləşən kambalaşəkilli (soleus) baldır əzələsini tam təcrid edir.",
            executionStepsAz = listOf(
                "Trenajorda oturun, barmaq uclarını platformanın kənarına qoyun və yastıqları dizlərinizin üstünə bərkidin.",
                "Dabanları maksimal aşağı endirərək baldırı dartın.",
                "Sonra barmaq uclarında maksimal yüksəkliyə qalxaraq baldırı sıxın."
            ),
            targetTipsAz = "Aşağıda 1 saniyə gərilmə, yuxarıda 1 saniyə sıxılma fasiləsi verin.",
            commonMistakesAz = listOf("İnersiya ilə yaylanmaq", "Hərəkətin amplitudasını kəsmək")
        ),
        Exercise(
            id = "ab_wheel_rollout",
            nameAz = "Roliklə Qarın Dartılması (Ab Wheel)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.KUREK, MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.AB_WHEEL_ROLLOUT,
            equipment = "Ab Roller / Qarın Roliki",
            difficulty = "Qabaqcıl",
            recommendedSets = 3,
            recommendedReps = "8-12",
            restSeconds = 60,
            descriptionAz = "Qarın divarını eksentrik uzanma fazasında maksimal gərginlikdə saxlayaraq inanılmaz dərəcədə möhkəm və relyefli edir.",
            executionStepsAz = listOf(
                "Diz üstə çökün, rolikin dəstəklərindən iki əllə tutun.",
                "Qarın əzələlərini sıxaraq asta-asta roliklə irəli diyirlənin.",
                "Bədəninizi döşəməyə yaxın uzadın, sonra qarın əzələlərinin gücü ilə başlanğıca qayıdın."
            ),
            targetTipsAz = "Beli əsla aşağı sallamayın (çökdürməyin); beli həmişə azca qozbel/gərgin saxlayın.",
            commonMistakesAz = listOf("Beli çökdürmək", "Qollarla deyil, qarınla dartınmamaq")
        ),
        // --- YENİ ƏLAVƏ EDİLMİŞ ABS / QARIN HƏRƏKƏTLƏRİ ---
        Exercise(
            id = "cable_kneeling_crunch",
            nameAz = "Blokda Dizüstü Qarın Bükmə (Prayer Crunch)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.CABLE_KNEELING_CRUNCH,
            equipment = "Kabel Trenajoru & Kəndir (Rope)",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Qarın əzələlərinə (rectus abdominis) əlavə çəki ilə yük vurmaq üçün ən effektiv hərəkətlərdən biridir. Belə artıq təzyiq salmadan kubiklərin qalınlaşmasını təmin edir.",
            executionStepsAz = listOf(
                "Yuxarı blokun qarşısında diz üstə çökün, kəndir dəstəyini başınızın hər iki tərəfində saxlayın.",
                "Çanağınızı sabit saxlayaraq nəfəs verin, dirsəkləri dizlərinizə doğru bükərək qarnınızı maksimum sıxın.",
                "Alt nöqtədə 1 saniyə fasilə verin və kubiklərin yığılmasını hiss edin.",
                "Nəfəs alaraq asta-asta, qarnın gərginliyini itirmədən başlanğıc vəziyyətinə qayıdın."
            ),
            targetTipsAz = "Hərəkəti ombanı aşağı-yuxarı yelləməklə etməyin; çanaq tam sabit qalmalı, sadəcə onurğa qarın əzələləri ilə bükülməlidir.",
            commonMistakesAz = listOf("Çanağı yelləmək", "Qolların gücü ilə kəndiri dartmaq", "Beli həddindən artıq düz saxlamaq")
        ),
        Exercise(
            id = "reverse_crunch",
            nameAz = "Əks Qarın Bükmə (Reverse Crunch)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.REVERSE_CRUNCH,
            equipment = "Mat / Döşəmə",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "15-20",
            restSeconds = 45,
            descriptionAz = "Aşağı qarın liflərini dərindən hədəf alır. Boyun və yuxarı onurğaya heç bir təzyiq salmadan aşağı qarın qatını relyefləşdirir.",
            executionStepsAz = listOf(
                "Mat üzərində arxası üstə uzanın, qollarınızı bədən boyunca yerə qoyun və ovucları döşəməyə basın.",
                "Dizlərinizi 90 dərəcə bükün və ayaqlarınızı yerdən qaldırın.",
                "Nəfəs verərək çanağınızı döşəmədən yuxarı və sinənizə doğru bükün, alt qarnınızı möhkəm sıxın.",
                "Nəfəs alaraq çanağı asta və nəzarətlə yerə endirin, ayaqları yerə dəymədən saxlayın."
            ),
            targetTipsAz = "Ayaqları inersiya ilə yelləməyin; bütün gücü çanağı yerdən 5-10 sm qaldıran alt qarın əzələsindən alın.",
            commonMistakesAz = listOf("Ayaqlarla yellənərək təkan almaq", "Beli döşəmədən qoparıb kəskin yerə çırpmaq")
        ),
        Exercise(
            id = "dead_bug",
            nameAz = "Ölü Böcək Qarın Hərəkəti (Dead Bug)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.KUREK),
            animationType = ExerciseAnimationType.DEAD_BUG,
            equipment = "Mat / Döşəmə",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "12-16 (hər tərəfə)",
            restSeconds = 45,
            descriptionAz = "Onurğanı qoruyan daxili köndələn qarın əzələsini (transverse abdominis) inkişaf etdirən və beli ağrılardan qoruyan qızıl standart kor məşqidir.",
            executionStepsAz = listOf(
                "Arxası üstə uzanın, qollarınızı tavana doğru düz qaldırın, dizləri 90 dərəcə bükərək qaldırın.",
                "Beli tamamilə döşəməyə yapışdırın (bel ilə döşəmə arasında heç bir boşluq qalmamalıdır).",
                "Nəfəs verərək eyni anda sağ qolu arxaya, sol ayağı isə irəli döşəməyə yaxın uzadın.",
                "Nəfəs alaraq mərkəzə qayıdın və dərhal əks tərəflə (sol qol + sağ ayaq) təkrar edin."
            ),
            targetTipsAz = "Bütün hərəkət boyu belinizin döşəmədən ayrılmasına qətiyyən icazə verməyin; qarnı içəri çəkilmiş saxlayın.",
            commonMistakesAz = listOf("Beli döşəmədən qaldırmaq", "Tələsik və nəzarətsiz hərəkət etmək")
        ),
        Exercise(
            id = "dumbbell_side_bend",
            nameAz = "Qantellə Yana Əyilmə (Dumbbell Side Bend)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.DUMBBELL_SIDE_BEND,
            equipment = "Tək Qantel",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "12-15 (hər tərəfə)",
            restSeconds = 45,
            descriptionAz = "Yan qarın əzələlərini (obliques) və bel kənarını bərkidir, yan tərəfdən bədənin estetik və güc balansını yaxşılaşdırır.",
            executionStepsAz = listOf(
                "Ayaqları çiyin bərabərliyində dik durun, bir əlinizdə qantel saxlayın, digər əli başın arxasına və ya belə qoyun.",
                "Bədəni önə və ya arxaya əymədən, sadəcə qantel olan tərəfə doğru yan müstəvidə asta-asta əyilin.",
                "Əks yan qarın əzələsində dartılma hiss etdikdə nəfəs verin və gövdəni dik vəziyyətə qaldıraraq yan əzələni sıxın.",
                "Təkrarları bitirdikdən sonra qanteli digər ələ keçirib əks tərəfi işlədin."
            ),
            targetTipsAz = "Heç vaxt eyni anda hər iki ələ qantel götürməyin; tək tərəfli ağırlıq əzələyə optimal gərginlik ötürür.",
            commonMistakesAz = listOf("Hər iki əldə qantel tutmaq", "Gövdəni qabağa əyərək fırlatmaq")
        ),
        Exercise(
            id = "alternate_heel_touchers",
            nameAz = "Növbəli Daban Toxunuşu (Heel Touchers / Pinqvin)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.ALTERNATE_HEEL_TOUCHERS,
            equipment = "Mat / Döşəmə",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "20-30 toxunuş",
            restSeconds = 40,
            descriptionAz = "Yan qarın və yuxarı qarın əzələlərini fasiləsiz gərginlikdə saxlayaraq yan relyefi kəskinləşdirən yan qıvrılma hərəkətidir.",
            executionStepsAz = listOf(
                "Arxası üstə uzanın, dizləri bükün, dabanları çanağa yaxın yerləşdirin.",
                "Kürək sümüklərini yerdən qaldıraraq qarnı yığın və qolları düz yanlara uzadın.",
                "Nəfəs verərək gövdəni sağa əyin və sağ əlin barmaqları ilə sağ dabana toxunun.",
                "Dərhal digər tərəfə keçərək sol əllə sol dabana toxunun."
            ),
            targetTipsAz = "Kürəyinizi döşəməyə salmayın; hərəkət boyu qarnınız yığılmış (izometrik bükülmüş) qalmalıdır.",
            commonMistakesAz = listOf("Boyundan dartmaq", "Kürəyi yerə qoyub tənbəllik etmək")
        ),
        Exercise(
            id = "decline_crunch",
            nameAz = "Meyilli Skamyada Qarın Bükmə (Decline Crunch)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.DECLINE_CRUNCH,
            equipment = "Meyilli Skamya (Decline Bench)",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 50,
            descriptionAz = "Aşağı meyilli bucaq sayəsində cazibə qüvvəsinə qarşı daha böyük müqavimət yaradır, qarın əzələlərinə döşəmədən daha güclü təzyiq vurur.",
            executionStepsAz = listOf(
                "Meyilli skamyada ayaqlarınızı yastıqların altına möhkəm bərkidin və uzanın.",
                "Əllərinizi sinənizdə çarpazlayın və ya qulaqların arxasına qoyun.",
                "Nəfəs verərək gövdənin yuxarı hissəsini qarın əzələləri ilə bükərək yuxarı qaldırın və kubikləri sıxın.",
                "Nəfəs alaraq asta-asta geri enin, bel skamyaya tam dəyməzdən əvvəl yeni təkrara başlayın."
            ),
            targetTipsAz = "Bütün beli dik qaldırmaq yerinə qabırğalarınızı çanağa yaxınlaşdıraraq qarnınızı yuvarlaq bükməyə fokuslanın.",
            commonMistakesAz = listOf("Beli taxta kimi düz qaldırmaq", "Aşağıda sürətlə yıxılmaq")
        ),
        Exercise(
            id = "cable_woodchop",
            nameAz = "Blokda Ağackəsən Fırlanma (Cable Woodchopper)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.CIYIN, MuscleGroup.KUREK),
            animationType = ExerciseAnimationType.CABLE_WOODCHOP,
            equipment = "Kabel / Blok Trenajoru",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "12-15 (hər tərəfə)",
            restSeconds = 60,
            descriptionAz = "İdmançılarda zərbə gücü və gövdənin fırlanma (rotational) gücünü quran ən funksional kor məşqidir. Bel ətrafını gücləndirir.",
            executionStepsAz = listOf(
                "Kabel dəstəyini yuxarı və ya sinə səviyyəsində qoyun, trenajora yan tərəfdən durun və iki əllə tutun.",
                "Ayaqları çiyindən geniş açın, dizləri azca bükün.",
                "Qolları düz saxlayaraq gövdəni diaqonal istiqamətdə aşağıya və əks tərəfə güclü şəkildə fırladın.",
                "Nəzarətlə başlanğıc vəziyyətinə qayıdın və eyni təkrar sayını digər istiqamətə də edin."
            ),
            targetTipsAz = "Fırlanmanı qollarınızla deyil, qarın və çanaqdan gələn burulma enerjisi ilə təmin edin.",
            commonMistakesAz = listOf("Dirsəkləri büküb qollarla çəkmək", "Bədəni tərpətmədən sadəcə qolları yelləmək")
        ),
        Exercise(
            id = "side_plank",
            nameAz = "Yan Dirsək Planki (Side Plank)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = listOf(MuscleGroup.CIYIN),
            animationType = ExerciseAnimationType.SIDE_PLANK,
            equipment = "Mat / Döşəmə",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "30-45 san (hər tərəfə)",
            restSeconds = 45,
            descriptionAz = "Onurğanın yan sütununu, kvadrat bel əzələsini və yan qarnı gücləndirərək zədələrdən qoruyan statik dözümlülük məşqidir.",
            executionStepsAz = listOf(
                "Böyür üstə uzanın, dirsəyinizi tam çiyininizin altına qoyun.",
                "Ayaqları bir-birinin üstünə qoyun və ya bir az qabağa açın.",
                "Çanağınızı yerdən qaldıraraq başdan dabana qədər düz xətt yaradın.",
                "Yan qarnı və qalçanı sıxaraq göstərilən vaxt ərzində tərpənmədən durun."
            ),
            targetTipsAz = "Çanağın yerə doğru sallanmasına və ya bədənin önə əyilməsinə imkan verməyin.",
            commonMistakesAz = listOf("Çanağı aşağı salmaq", "Dirsəyi çiyindən çox uzaqda saxlamaq")
        ),
        Exercise(
            id = "jackknife_sit_up",
            nameAz = "Bıçaq Qarın Hərəkəti (Jackknife V-Up)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.JACKKNIFE_SIT_UP,
            equipment = "Mat / Döşəmə",
            difficulty = "Qabaqcıl",
            recommendedSets = 3,
            recommendedReps = "10-15",
            restSeconds = 60,
            descriptionAz = "Eyni anda yuxarı və aşağı qarnı maksimum amplituda ilə qovuşduran 'V' formalı güclü qarın hərəkətidir.",
            executionStepsAz = listOf(
                "Arxası üstə düz uzanın, qollarınızı başınızın arxasına, ayaqlarınızı düz uzadın.",
                "Eyni anda qollarınızı və düz ayaqlarınızı yuxarı qaldıraraq bədəninizlə 'V' hərfi forması yaradın.",
                "Əllərinizi ayaq barmaqlarınıza çatdırın və qarnınızı 1 saniyə dərindən sıxın.",
                "Asta-asta nəzarətlə başlanğıc vəziyyətinə enin və yerə tam toxunmadan təkrar edin."
            ),
            targetTipsAz = "Dizləri bacardıqca düz saxlayın və hərəkətin yuxarı nöqtəsində dərindən nəfəs verin.",
            commonMistakesAz = listOf("Dizləri tam bükmək", "Ayaqları yerə çırpmaq")
        ),
        Exercise(
            id = "bench_lying_leg_raise",
            nameAz = "Skamyada Uzanaraq Ayaq Qaldırma (Flat Bench Leg Raise)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.BENCH_LYING_LEG_RAISE,
            equipment = "Üfüqi Skamya (Flat Bench)",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "12-15",
            restSeconds = 50,
            descriptionAz = "Skamyadan kənara sallanan ayaqlar sayəsində alt qarın əzələsinə daha böyük dartılma və hərəkət dərinliyi bəxş edir.",
            executionStepsAz = listOf(
                "Üfüqi skamyaya uzanın, başınızın arxasındakı kənarlardan möhkəm tutun.",
                "Ayaqlarınızı düz birləşdirərək skamya səviyyəsindən yuxarı qaldırın.",
                "Alt qarın əzələlərini bükərək ayaqları 90 dərəcəyə qədər qaldırın, çanağı yüngülcə skamyadan qaldırın.",
                "Asta və nəzarətlə skamya səviyyəsinə qədər endirin."
            ),
            targetTipsAz = "Ayaqları endirərkən belinizi skamyadan ayırmayın; alt qarnınız daim gərgin olsun.",
            commonMistakesAz = listOf("Ayaqları sürətlə aşağı buraxmaq", "Çanağı heç qaldırmamaq")
        ),
        Exercise(
            id = "oblique_floor_crunch",
            nameAz = "Yan Qarın Bükməsi (Oblique Floor Crunch)",
            primaryMuscle = MuscleGroup.ABS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.OBLIQUE_FLOOR_CRUNCH,
            equipment = "Mat / Döşəmə",
            difficulty = "Başlanğıc",
            recommendedSets = 3,
            recommendedReps = "15-20 (hər tərəfə)",
            restSeconds = 45,
            descriptionAz = "Döşəmədə böyür üstə uzanaraq daxili və xarici yan qarın əzələlərini tam təcrid olunmuş şəkildə yığır.",
            executionStepsAz = listOf(
                "Arxası üstə uzanın, dizləri bükərək bir tərəfə (sağa) döşəməyə aşırdın, gövdəniz isə tavana baxsın.",
                "Əlləri başın arxasına qoyun.",
                "Nəfəs verərək sol çiyini və qabırğaları yan tərəfdən qaldıraraq sol yan qarnı sıxın.",
                "Alt nöqtəyə endikdən sonra təkrarları tamamlayıb dizləri digər tərəfə aşırdın."
            ),
            targetTipsAz = "Hərəkət qısa amma çox sıxıcı olmalıdır; məqsəd başı qaldırmaq deyil, qabırğanı ombaya sıxmaqdır.",
            commonMistakesAz = listOf("Boyunu dartmaq", "Dizləri yelləmək")
        ),
        // --- ƏLAVƏ YENİ KÜRƏK VƏ TRİSEPS HƏRƏKƏTLƏRİ ---
        Exercise(
            id = "cable_straight_arm_pulldown",
            nameAz = "Blokda Qolları Bükmədən Dartma (Straight-Arm Pulldown)",
            primaryMuscle = MuscleGroup.KUREK,
            secondaryMuscles = listOf(MuscleGroup.ABS, MuscleGroup.TRICEPS),
            animationType = ExerciseAnimationType.CABLE_STRAIGHT_ARM_PULLDOWN,
            equipment = "Kabel Trenajoru & Düz Qrif",
            difficulty = "Orta",
            recommendedSets = 4,
            recommendedReps = "12-15",
            restSeconds = 60,
            descriptionAz = "Bisepsləri tamamilə oyundan çıxararaq qanad əzələlərini (latissimus dorsi) və kor divarını təcrid olunmuş şəkildə yandırır.",
            executionStepsAz = listOf(
                "Yuxarı blokun qarşısında durun, düz qrifdən çiyin enində tutun, bir addım geri çəkilin.",
                "Dizləri yüngül bükün, gövdəni 30 dərəcə irəli əyin, qolları düz saxlayın.",
                "Dirsəkləri bükmədən, qanad əzələlərini sıxaraq qriflə ombanıza doğru qövsvari hərəkətlə aşağı dartın.",
                "Aşağıda 1 saniyə kürəyi sıxın, sonra asta-asta göz səviyyəsinə qaytarın."
            ),
            targetTipsAz = "Dirsəkləri bükməyin; bu hərəkət triseps açışı deyil, qanad dartışıdır.",
            commonMistakesAz = listOf("Dirsəkləri büküb-açmaq", "Bədənlə yellənmək")
        ),
        Exercise(
            id = "triceps_cable_kickback",
            nameAz = "Blokda Triseps Geri Açma (Cable Kickback)",
            primaryMuscle = MuscleGroup.TRICEPS,
            secondaryMuscles = emptyList(),
            animationType = ExerciseAnimationType.TRICEPS_CABLE_KICKBACK,
            equipment = "Kabel Trenajoru",
            difficulty = "Orta",
            recommendedSets = 3,
            recommendedReps = "12-15 (hər qola)",
            restSeconds = 45,
            descriptionAz = "Kabelin yaratdığı daimi müqavimət sayəsində triceps əzələsinin lateral və uzun başını pik qısalma nöqtəsində kəskin relyefləşdirir.",
            executionStepsAz = listOf(
                "Aşağı blokun yanında dayanın, gövdəni döşəməyə paralelə yaxın əyin.",
                "İşləyən qolun dirsəyini qabırğanıza bərkidin (90 dərəcə bucaqda).",
                "Dirsəyi tərpətmədən, nəfəs verərək qolu tamamilə arxaya düz açın və tricepsi 1 saniyə sıxın.",
                "Nəzarətlə 90 dərəcə vəziyyətinə qayıdın."
            ),
            targetTipsAz = "Dirsəyinizin aşağı düşməsinə və ya qabağa getməsinə imkan verməyin; dirsək havada kilidli qalmalıdır.",
            commonMistakesAz = listOf("Dirsəyi yelləmək", "Qolu arxada tam açmamaq")
        )
    )

    // Dedicated 3-Day Workout Program
    val threeDayProgram: List<WorkoutDay> = listOf(
        WorkoutDay(
            dayNumber = 1,
            nameAz = "Gün 1: İtələmə (Push)",
            targetMusclesSummaryAz = "Sinə, Çiyin və Triceps",
            targetMuscles = listOf(MuscleGroup.SINE, MuscleGroup.CIYIN, MuscleGroup.TRICEPS),
            estimatedMinutes = 55,
            exercises = listOf(
                allExercises.first { it.id == "bench_press" },
                allExercises.first { it.id == "incline_db_press" },
                allExercises.first { it.id == "db_shoulder_press" },
                allExercises.first { it.id == "lateral_raise" },
                allExercises.first { it.id == "tricep_pushdown" },
                allExercises.first { it.id == "skull_crusher" }
            )
        ),
        WorkoutDay(
            dayNumber = 2,
            nameAz = "Gün 2: Çəkmə (Pull)",
            targetMusclesSummaryAz = "Kürək, Biceps və Arxa Çiyin",
            targetMuscles = listOf(MuscleGroup.KUREK, MuscleGroup.BICEPS, MuscleGroup.CIYIN),
            estimatedMinutes = 55,
            exercises = listOf(
                allExercises.first { it.id == "lat_pulldown" },
                allExercises.first { it.id == "barbell_row" },
                allExercises.first { it.id == "deadlift" },
                allExercises.first { it.id == "cable_face_pull" },
                allExercises.first { it.id == "barbell_curl" },
                allExercises.first { it.id == "hammer_curl" }
            )
        ),
        WorkoutDay(
            dayNumber = 3,
            nameAz = "Gün 3: Ayaq & Qarın (Legs & Abs)",
            targetMusclesSummaryAz = "Ayaq (Bud, Baldır) və Abs (Qarın)",
            targetMuscles = listOf(MuscleGroup.AYAQ, MuscleGroup.ABS),
            estimatedMinutes = 50,
            exercises = listOf(
                allExercises.first { it.id == "barbell_squat" },
                allExercises.first { it.id == "romanian_deadlift" },
                allExercises.first { it.id == "leg_extension" },
                allExercises.first { it.id == "calf_raise" },
                allExercises.first { it.id == "hanging_leg_raise" },
                allExercises.first { it.id == "cable_kneeling_crunch" },
                allExercises.first { it.id == "ab_crunch" },
                allExercises.first { it.id == "reverse_crunch" },
                allExercises.first { it.id == "plank" }
            )
        )
    )

    fun getExercisesForMuscle(muscle: MuscleGroup): List<Exercise> {
        return allExercises.filter { it.primaryMuscle == muscle || it.secondaryMuscles.contains(muscle) }
    }
}
