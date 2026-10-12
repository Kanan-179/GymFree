package com.example.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.*

class UserAvatarPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_avatar_prefs", Context.MODE_PRIVATE)

    fun loadAvatarConfig(): UserAvatarConfig {
        return UserAvatarConfig(
            name = prefs.getString("name", "Mənim İdmançım") ?: "Mənim İdmançım",
            gender = AvatarGender.entries.getOrElse(
                prefs.getInt("gender", AvatarGender.MALE.ordinal)
            ) { AvatarGender.MALE },
            physique = AvatarPhysique.entries.getOrElse(
                prefs.getInt("physique", AvatarPhysique.BODYBUILDER.ordinal)
            ) { AvatarPhysique.BODYBUILDER },
            skinTone = AvatarSkinTone.entries.getOrElse(
                prefs.getInt("skinTone", AvatarSkinTone.CYBER_MANNEQUIN.ordinal)
            ) { AvatarSkinTone.CYBER_MANNEQUIN },
            hairStyle = AvatarHairStyle.entries.getOrElse(
                prefs.getInt("hairStyle", AvatarHairStyle.BUZZ_CUT.ordinal)
            ) { AvatarHairStyle.BUZZ_CUT },
            hairColor = AvatarHairColor.entries.getOrElse(
                prefs.getInt("hairColor", AvatarHairColor.BLACK.ordinal)
            ) { AvatarHairColor.BLACK },
            topClothing = AvatarTopClothing.entries.getOrElse(
                prefs.getInt("topClothing", AvatarTopClothing.SHIRTLESS.ordinal)
            ) { AvatarTopClothing.SHIRTLESS },
            topColorHex = prefs.getLong("topColorHex", 0xFF06B6D4),
            bottomClothing = AvatarBottomClothing.entries.getOrElse(
                prefs.getInt("bottomClothing", AvatarBottomClothing.GYM_SHORTS.ordinal)
            ) { AvatarBottomClothing.GYM_SHORTS },
            bottomColorHex = prefs.getLong("bottomColorHex", 0xFF1E293B),
            shoesColorHex = prefs.getLong("shoesColorHex", 0xFFE2E8F0),
            wristWraps = prefs.getBoolean("wristWraps", true),
            wristWrapsColorHex = prefs.getLong("wristWrapsColorHex", 0xFFF97316),
            liftingBelt = prefs.getBoolean("liftingBelt", true),
            liftingBeltColorHex = prefs.getLong("liftingBeltColorHex", 0xFF0F172A),
            headband = prefs.getBoolean("headband", false),
            headbandColorHex = prefs.getLong("headbandColorHex", 0xFF06B6D4)
        )
    }

    fun saveAvatarConfig(config: UserAvatarConfig) {
        prefs.edit().apply {
            putString("name", config.name)
            putInt("gender", config.gender.ordinal)
            putInt("physique", config.physique.ordinal)
            putInt("skinTone", config.skinTone.ordinal)
            putInt("hairStyle", config.hairStyle.ordinal)
            putInt("hairColor", config.hairColor.ordinal)
            putInt("topClothing", config.topClothing.ordinal)
            putLong("topColorHex", config.topColorHex)
            putInt("bottomClothing", config.bottomClothing.ordinal)
            putLong("bottomColorHex", config.bottomColorHex)
            putLong("shoesColorHex", config.shoesColorHex)
            putBoolean("wristWraps", config.wristWraps)
            putLong("wristWrapsColorHex", config.wristWrapsColorHex)
            putBoolean("liftingBelt", config.liftingBelt)
            putLong("liftingBeltColorHex", config.liftingBeltColorHex)
            putBoolean("headband", config.headband)
            putLong("headbandColorHex", config.headbandColorHex)
            apply()
        }
    }

    fun resetToDefault(): UserAvatarConfig {
        prefs.edit().clear().apply()
        return UserAvatarConfig.DEFAULT
    }
}
