package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.AvatarPose
import com.example.ui.components.AvatarPreviewStage
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarStudioScreen(
    currentConfig: UserAvatarConfig,
    onSaveConfig: (UserAvatarConfig) -> Unit,
    onResetConfig: () -> Unit
) {
    var workingConfig by remember(currentConfig) { mutableStateOf(currentConfig) }
    var selectedPose by remember { mutableStateOf(AvatarPose.FRONT_FLEX) }
    var selectedSection by remember { mutableIntStateOf(0) }
    var isEditingName by remember { mutableStateOf(false) }
    var showSavedMessage by remember { mutableStateOf(false) }

    val colorPalette = listOf(
        0xFF06B6D4 to "Sian Mavi",
        0xFFF97316 to "Narıncı",
        0xFF84CC16 to "Yaşıl Laym",
        0xFFEF4444 to "Qırmızı",
        0xFFEC4899 to "Çəhrayı",
        0xFFA855F7 to "Bənövşəyi",
        0xFFEAB308 to "Qızılı",
        0xFF1E293B to "Tünd Boz",
        0xFF0F172A to "Qara",
        0xFFE2E8F0 to "Gümüşü Ağ"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
            .testTag("avatar_studio_screen")
    ) {
        // Success Banner when saved
        if (showSavedMessage) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = LimeSuccess.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, LimeSuccess),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LimeSuccess)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Avatar uğurla yadda saxlanıldı və animasiyalara tətbiq edildi!",
                            color = LimeSuccess,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { showSavedMessage = false }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Bağla", tint = TextSecondary)
                    }
                }
            }
        }

        // Section Title & Presets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Şəxsi İdmançı Avatarı",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Məşq animasiyalarında sizin xarakteriniz çıxış edir",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            IconButton(
                onClick = {
                    onResetConfig()
                    workingConfig = UserAvatarConfig.DEFAULT
                    showSavedMessage = true
                },
                modifier = Modifier.testTag("reset_avatar_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Sıfırla", tint = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Preset Chips
        Text(
            text = "HAZIR ŞABLONLAR",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CyanPrimary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(UserAvatarConfig.PRESETS) { preset ->
                val isSelected = workingConfig.name == preset.config.name
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) CyanPrimary.copy(alpha = 0.25f) else DarkSurfaceVariant,
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary) else null,
                    modifier = Modifier.clickable {
                        workingConfig = preset.config
                        onSaveConfig(preset.config)
                        showSavedMessage = true
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(preset.config.skinTone.baseColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = preset.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) CyanPrimary else TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // LIVE AVATAR PREVIEW STAGE
        AvatarPreviewStage(
            config = workingConfig,
            pose = selectedPose,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .testTag("avatar_preview_canvas")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Pose Switcher Bar & Name Field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pose chips
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                AvatarPose.entries.forEach { pose ->
                    val isSelected = selectedPose == pose
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) OrangeAccent.copy(alpha = 0.2f) else DarkSurfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, OrangeAccent) else null,
                        modifier = Modifier.clickable { selectedPose = pose }
                    ) {
                        Text(
                            text = pose.titleAz,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) OrangeAccent else TextMuted,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Athlete Name Pill
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DarkSurfaceVariant,
                modifier = Modifier.clickable { isEditingName = !isEditingName }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = workingConfig.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }

        if (isEditingName) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = workingConfig.name,
                onValueChange = { workingConfig = workingConfig.copy(name = it) },
                label = { Text("İdmançının Adı", color = TextMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = CyanPrimary,
                    unfocusedBorderColor = DarkBorder
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CATEGORY TABS
        val categoryTitles = listOf("Bədən & Cins", "Dəri & Saç", "İdman Geyimi", "Aksesuarlar")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            categoryTitles.forEachIndexed { index, title ->
                val isSelected = selectedSection == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CyanPrimary else Color.Transparent)
                        .clickable { selectedSection = index }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) CyanOnPrimary else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION CONTENT
        when (selectedSection) {
            0 -> {
                // Section 0: Physique & Gender
                Text("CİNS & MODEL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    AvatarGender.entries.forEach { g ->
                        val isSel = workingConfig.gender == g
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { workingConfig = workingConfig.copy(gender = g) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) CyanPrimary.copy(alpha = 0.2f) else DarkSurfaceVariant
                            ),
                            border = if (isSel) androidx.compose.foundation.BorderStroke(1.5.dp, CyanPrimary) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (g == AvatarGender.MALE) Icons.Default.Male else Icons.Default.Female,
                                    contentDescription = null,
                                    tint = if (isSel) CyanPrimary else TextMuted
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = g.displayNameAz,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) CyanPrimary else TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("BƏDƏN QURULUŞU VƏ ƏZƏLƏ HƏCMİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                AvatarPhysique.entries.forEach { p ->
                    val isSel = workingConfig.physique == p
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { workingConfig = workingConfig.copy(physique = p) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSel) OrangeAccent.copy(alpha = 0.2f) else DarkSurfaceVariant
                        ),
                        border = if (isSel) androidx.compose.foundation.BorderStroke(1.5.dp, OrangeAccent) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = p.displayNameAz,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) OrangeAccent else TextPrimary,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = when (p) {
                                        AvatarPhysique.SLIM -> "Estetik, yüngül çəkili və elastik atlet"
                                        AvatarPhysique.ATHLETIC -> "Standart güclü və relyefli idmançı bədəni"
                                        AvatarPhysique.BODYBUILDER -> "Ağır çəkili, nəhəng baiseps və geniş kürək"
                                    },
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            if (isSel) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = OrangeAccent)
                            }
                        }
                    }
                }
            }

            1 -> {
                // Section 1: Skin & Hair
                Text("DƏRİ RƏNGİ (MELANİN VƏ YA KİBER)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AvatarSkinTone.entries.forEach { tone ->
                        val isSel = workingConfig.skinTone == tone
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { workingConfig = workingConfig.copy(skinTone = tone) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(tone.baseColor)
                                    .border(
                                        width = if (isSel) 3.dp else 1.dp,
                                        color = if (isSel) CyanPrimary else DarkBorder,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tone.displayNameAz,
                                fontSize = 10.sp,
                                color = if (isSel) CyanPrimary else TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("SAÇ DÜZÜMÜ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AvatarHairStyle.entries.forEach { style ->
                        val isSel = workingConfig.hairStyle == style
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) CyanPrimary.copy(alpha = 0.25f) else DarkSurfaceVariant,
                            border = if (isSel) androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary) else null,
                            modifier = Modifier.clickable { workingConfig = workingConfig.copy(hairStyle = style) }
                        ) {
                            Text(
                                text = style.displayNameAz,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) CyanPrimary else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("SAÇ RƏNGİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AvatarHairColor.entries.forEach { hc ->
                        val isSel = workingConfig.hairColor == hc
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { workingConfig = workingConfig.copy(hairColor = hc) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(hc.color)
                                    .border(
                                        width = if (isSel) 3.dp else 1.dp,
                                        color = if (isSel) CyanPrimary else DarkBorder,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hc.displayNameAz,
                                fontSize = 10.sp,
                                color = if (isSel) CyanPrimary else TextMuted
                            )
                        }
                    }
                }
            }

            2 -> {
                // Section 2: Gym Clothing
                Text("ÜST İDMAN GEYİMİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                AvatarTopClothing.entries.forEach { top ->
                    val isSel = workingConfig.topClothing == top
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { workingConfig = workingConfig.copy(topClothing = top) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSel) CyanPrimary.copy(alpha = 0.2f) else DarkSurfaceVariant
                        ),
                        border = if (isSel) androidx.compose.foundation.BorderStroke(1.5.dp, CyanPrimary) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = top.displayNameAz,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) CyanPrimary else TextPrimary,
                                fontSize = 13.sp
                            )
                            if (isSel) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = CyanPrimary)
                            }
                        }
                    }
                }

                if (workingConfig.topClothing != AvatarTopClothing.SHIRTLESS) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("MAYKA / KÖYNƏK RƏNGİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorPalette.forEach { (hex, name) ->
                            val isSel = workingConfig.topColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(
                                        width = if (isSel) 3.dp else 1.dp,
                                        color = if (isSel) Color.White else DarkBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { workingConfig = workingConfig.copy(topColorHex = hex) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("ALT İDMAN GEYİMİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    AvatarBottomClothing.entries.forEach { bot ->
                        val isSel = workingConfig.bottomClothing == bot
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) OrangeAccent.copy(alpha = 0.25f) else DarkSurfaceVariant,
                            border = if (isSel) androidx.compose.foundation.BorderStroke(1.dp, OrangeAccent) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { workingConfig = workingConfig.copy(bottomClothing = bot) }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            ) {
                                Text(
                                    text = bot.displayNameAz,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) OrangeAccent else TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("ALT GEYİM RƏNGİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colorPalette.forEach { (hex, _) ->
                        val isSel = workingConfig.bottomColorHex == hex
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(hex))
                                .border(
                                    width = if (isSel) 3.dp else 1.dp,
                                    color = if (isSel) Color.White else DarkBorder,
                                    shape = CircleShape
                                )
                                .clickable { workingConfig = workingConfig.copy(bottomColorHex = hex) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("KROSSOVKA RƏNGİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        0xFFE2E8F0 to "Ağ",
                        0xFF0F172A to "Qara",
                        0xFF84CC16 to "Neon Yaşıl",
                        0xFF06B6D4 to "Sian Mavi",
                        0xFFEF4444 to "Qırmızı"
                    ).forEach { (hex, _) ->
                        val isSel = workingConfig.shoesColorHex == hex
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(hex))
                                .border(
                                    width = if (isSel) 3.dp else 1.dp,
                                    color = if (isSel) Color.White else DarkBorder,
                                    shape = CircleShape
                                )
                                .clickable { workingConfig = workingConfig.copy(shoesColorHex = hex) }
                        )
                    }
                }
            }

            3 -> {
                // Section 3: Accessories & Wraps
                Text("İDMAN TƏCHİZATI VƏ AKSESUARLARI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                Spacer(modifier = Modifier.height(10.dp))

                // Lifting belt
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Ağır Atletika Kəməri (Gym Belt)", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                            Text("Squat, deadlift və ştanq zamanı beli qoruyur", fontSize = 11.sp, color = TextMuted)
                        }
                        Switch(
                            checked = workingConfig.liftingBelt,
                            onCheckedChange = { workingConfig = workingConfig.copy(liftingBelt = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CyanPrimary,
                                checkedTrackColor = CyanPrimary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Wrist Wraps
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("İdman Biləklikləri (Wrist Wraps)", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                            Text("Bench press və baiseps zamanı biləklərə dəstək", fontSize = 11.sp, color = TextMuted)
                        }
                        Switch(
                            checked = workingConfig.wristWraps,
                            onCheckedChange = { workingConfig = workingConfig.copy(wristWraps = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = OrangeAccent,
                                checkedTrackColor = OrangeAccent.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                if (workingConfig.wristWraps) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("BİLƏKLİK RƏNGİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0xFFF97316, 0xFF06B6D4, 0xFFEF4444, 0xFF84CC16, 0xFFEC4899, 0xFF0F172A).forEach { hex ->
                            val isSel = workingConfig.wristWrapsColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(
                                        width = if (isSel) 3.dp else 1.dp,
                                        color = if (isSel) Color.White else DarkBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { workingConfig = workingConfig.copy(wristWrapsColorHex = hex) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Headband
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Baş Tər Bəndi (Sweatband)", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                            Text("İdmançı alnında tər saxlayan xüsusi lent", fontSize = 11.sp, color = TextMuted)
                        }
                        Switch(
                            checked = workingConfig.headband,
                            onCheckedChange = { workingConfig = workingConfig.copy(headband = it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CyanPrimary,
                                checkedTrackColor = CyanPrimary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }

                if (workingConfig.headband) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("BAŞ BƏNDİ RƏNGİ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0xFF06B6D4, 0xFFF97316, 0xFFEF4444, 0xFF84CC16, 0xFFE2E8F0, 0xFF0F172A).forEach { hex ->
                            val isSel = workingConfig.headbandColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(hex))
                                    .border(
                                        width = if (isSel) 3.dp else 1.dp,
                                        color = if (isSel) Color.White else DarkBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { workingConfig = workingConfig.copy(headbandColorHex = hex) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SAVE BUTTON
        Button(
            onClick = {
                onSaveConfig(workingConfig)
                showSavedMessage = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_avatar_btn"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = CyanOnPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Yadda Saxla və Məşqlərə Tətbiq Et",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CyanOnPrimary
            )
        }
    }
}
