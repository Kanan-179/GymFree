package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class AvatarGender(val displayNameAz: String) {
    MALE("Kişi"),
    FEMALE("Qadın")
}

enum class AvatarPhysique(
    val displayNameAz: String,
    val chestScale: Float,
    val armScale: Float,
    val legScale: Float
) {
    SLIM("İncə / Fit", 0.9f, 0.88f, 0.9f),
    ATHLETIC("Atletik / Relyefli", 1.0f, 1.0f, 1.0f),
    BODYBUILDER("Əzələli / Bodibilder", 1.25f, 1.3f, 1.22f)
}

enum class AvatarSkinTone(
    val displayNameAz: String,
    val baseHex: Long,
    val highlightHex: Long,
    val shadowHex: Long
) {
    CYBER_MANNEQUIN("Kiber Maniken", 0xFF334155, 0xFF475569, 0xFF1E293B),
    LIGHT("Təbii Açıq", 0xFFE0BB9B, 0xFFF5D6BD, 0xFFB88E6D),
    WARM_TAN("Buğdayı", 0xFFC68656, 0xFFDE9E6E, 0xFF9E5C32),
    BRONZE("Bürünc Qaraşın", 0xFF8D5537, 0xFFA66946, 0xFF65371F),
    DARK("Tünd Şabalıdı", 0xFF513123, 0xFF6B4332, 0xFF331C12),
    GOLDEN("Qızılı Titan", 0xFFCA8A04, 0xFFFACC15, 0xFF854D0E),
    NEON_CYAN("Neon Kiber", 0xFF0284C7, 0xFF38BDF8, 0xFF0369A1);

    val baseColor: Color get() = Color(baseHex)
    val highlightColor: Color get() = Color(highlightHex)
    val shadowColor: Color get() = Color(shadowHex)
}

enum class AvatarHairStyle(val displayNameAz: String) {
    BALD("Keçəl / Təmiz"),
    BUZZ_CUT("Qısa İdmançı"),
    FADE("Müasir Fade"),
    CURLY("Qıvrım Saç"),
    PONYTAIL("At Quyruğu / Topuz"),
    UNDERCUT("Undercut / Stil")
}

enum class AvatarHairColor(val displayNameAz: String, val hex: Long) {
    BLACK("Qara", 0xFF18181B),
    DARK_BROWN("Tünd Qonur", 0xFF3E2723),
    BLONDE("Sarı", 0xFFEAB308),
    AUBURN("Kürən", 0xFFB91C1C),
    SILVER("Gümüşü", 0xFFE2E8F0),
    NEON_BLUE("Neon Mavi", 0xFF06B6D4);

    val color: Color get() = Color(hex)
}

enum class AvatarTopClothing(val displayNameAz: String) {
    SHIRTLESS("Çılpaq (Bütün Əzələlər)"),
    TANK_TOP("İdman Maykası"),
    COMPRESSION_TEE("Sıx İdman Köynəyi")
}

enum class AvatarBottomClothing(val displayNameAz: String) {
    GYM_SHORTS("İdman Şortiki"),
    COMPRESSION_TIGHTS("İdman Taytı"),
    BRIEFS("Klassik Bodibilder Trus")
}

data class UserAvatarConfig(
    val name: String = "Mənim İdmançım",
    val gender: AvatarGender = AvatarGender.MALE,
    val physique: AvatarPhysique = AvatarPhysique.BODYBUILDER,
    val skinTone: AvatarSkinTone = AvatarSkinTone.CYBER_MANNEQUIN,
    val hairStyle: AvatarHairStyle = AvatarHairStyle.BUZZ_CUT,
    val hairColor: AvatarHairColor = AvatarHairColor.BLACK,
    val topClothing: AvatarTopClothing = AvatarTopClothing.SHIRTLESS,
    val topColorHex: Long = 0xFF06B6D4, // CyanPrimary
    val bottomClothing: AvatarBottomClothing = AvatarBottomClothing.GYM_SHORTS,
    val bottomColorHex: Long = 0xFF1E293B, // Dark Slate
    val shoesColorHex: Long = 0xFFE2E8F0, // Chrome
    val wristWraps: Boolean = true,
    val wristWrapsColorHex: Long = 0xFFF97316, // OrangeAccent
    val liftingBelt: Boolean = true,
    val liftingBeltColorHex: Long = 0xFF0F172A,
    val headband: Boolean = false,
    val headbandColorHex: Long = 0xFF06B6D4
) {
    val topColor: Color get() = Color(topColorHex)
    val bottomColor: Color get() = Color(bottomColorHex)
    val shoesColor: Color get() = Color(shoesColorHex)
    val wristWrapsColor: Color get() = Color(wristWrapsColorHex)
    val liftingBeltColor: Color get() = Color(liftingBeltColorHex)
    val headbandColor: Color get() = Color(headbandColorHex)

    companion object {
        val DEFAULT = UserAvatarConfig()

        val PRESETS = listOf(
            Preset("Titan Bodibilder", UserAvatarConfig(
                name = "Titan Bodibilder",
                gender = AvatarGender.MALE,
                physique = AvatarPhysique.BODYBUILDER,
                skinTone = AvatarSkinTone.WARM_TAN,
                hairStyle = AvatarHairStyle.BUZZ_CUT,
                hairColor = AvatarHairColor.BLACK,
                topClothing = AvatarTopClothing.SHIRTLESS,
                bottomClothing = AvatarBottomClothing.GYM_SHORTS,
                bottomColorHex = 0xFF1E293B,
                wristWraps = true,
                wristWrapsColorHex = 0xFFF97316,
                liftingBelt = true
            )),
            Preset("Kiber Maniken", UserAvatarConfig(
                name = "Kiber Maniken",
                gender = AvatarGender.MALE,
                physique = AvatarPhysique.BODYBUILDER,
                skinTone = AvatarSkinTone.CYBER_MANNEQUIN,
                hairStyle = AvatarHairStyle.BALD,
                topClothing = AvatarTopClothing.SHIRTLESS,
                bottomClothing = AvatarBottomClothing.GYM_SHORTS,
                wristWraps = true,
                liftingBelt = true
            )),
            Preset("Krossfit Atlet", UserAvatarConfig(
                name = "Krossfit Atlet",
                gender = AvatarGender.MALE,
                physique = AvatarPhysique.ATHLETIC,
                skinTone = AvatarSkinTone.LIGHT,
                hairStyle = AvatarHairStyle.FADE,
                hairColor = AvatarHairColor.DARK_BROWN,
                topClothing = AvatarTopClothing.TANK_TOP,
                topColorHex = 0xFF06B6D4,
                bottomClothing = AvatarBottomClothing.GYM_SHORTS,
                bottomColorHex = 0xFF0F172A,
                headband = true,
                headbandColorHex = 0xFF06B6D4,
                wristWraps = true,
                liftingBelt = false
            )),
            Preset("Fit Xanım Atlet", UserAvatarConfig(
                name = "Fit Xanım Atlet",
                gender = AvatarGender.FEMALE,
                physique = AvatarPhysique.ATHLETIC,
                skinTone = AvatarSkinTone.WARM_TAN,
                hairStyle = AvatarHairStyle.PONYTAIL,
                hairColor = AvatarHairColor.BLACK,
                topClothing = AvatarTopClothing.TANK_TOP,
                topColorHex = 0xFFEC4899, // Pink
                bottomClothing = AvatarBottomClothing.COMPRESSION_TIGHTS,
                bottomColorHex = 0xFF1E293B,
                wristWraps = true,
                wristWrapsColorHex = 0xFFEC4899,
                liftingBelt = false
            )),
            Preset("Qızıl Era Çempionu", UserAvatarConfig(
                name = "Qızıl Era Çempionu",
                gender = AvatarGender.MALE,
                physique = AvatarPhysique.BODYBUILDER,
                skinTone = AvatarSkinTone.BRONZE,
                hairStyle = AvatarHairStyle.CURLY,
                hairColor = AvatarHairColor.DARK_BROWN,
                topClothing = AvatarTopClothing.SHIRTLESS,
                bottomClothing = AvatarBottomClothing.BRIEFS,
                bottomColorHex = 0xFFCA8A04,
                wristWraps = false,
                liftingBelt = true,
                liftingBeltColorHex = 0xFF78350F
            ))
        )
    }

    data class Preset(val title: String, val config: UserAvatarConfig)
}
