package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class AvatarPose(val titleAz: String) {
    FRONT_FLEX("Ön İkiqat Baiseps"),
    RELAXED_STAND("Klassik Duruş"),
    CHEST_FLEX("Sinə Sıxma")
}

@Composable
fun AvatarPreviewStage(
    config: UserAvatarConfig,
    modifier: Modifier = Modifier,
    pose: AvatarPose = AvatarPose.FRONT_FLEX
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_breathe")
    val breatheProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    Box(
        modifier = modifier
            .background(DarkBackground, RoundedCornerShape(20.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Gym studio subtle background spotlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        config.topColor.copy(alpha = 0.15f),
                        Color(0x08FFFFFF),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy - 20f),
                    radius = w * 0.55f
                ),
                radius = w * 0.55f,
                center = Offset(cx, cy - 20f)
            )

            // Floor shadow / studio ring
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x55000000), Color.Transparent),
                    center = Offset(cx, cy + h * 0.42f),
                    radius = 90f
                ),
                topLeft = Offset(cx - 85f, cy + h * 0.38f),
                size = Size(170f, 26f)
            )

            // Draw full customizable avatar athlete
            drawCustomAthlete(
                cx = cx,
                cy = cy - 10f,
                config = config,
                pose = pose,
                breathe = breatheProgress
            )
        }
    }
}

/**
 * Renders a full customized athlete with user's selected skin, physique, hair, clothing, and gear.
 */
fun DrawScope.drawCustomAthlete(
    cx: Float,
    cy: Float,
    config: UserAvatarConfig,
    pose: AvatarPose,
    breathe: Float
) {
    val skin = config.skinTone
    val baseColor = skin.baseColor
    val highlightColor = skin.highlightColor
    val shadowColor = skin.shadowColor

    val p = config.physique
    val chestW = 62f * p.chestScale + (3f * breathe)
    val torsoH = 76f
    val headRadius = 14f

    val shoulderY = cy - 36f
    val hipY = cy + 42f

    // 1. LEGS (Thighs, Knees, Calves, Shorts/Tights, Shoes)
    val legSpread = 26f
    val hipLeft = Offset(cx - 14f, hipY)
    val hipRight = Offset(cx + 14f, hipY)

    val kneeY = hipY + 44f
    val ankleY = hipY + 84f

    val kneeLeft = Offset(cx - legSpread, kneeY)
    val kneeRight = Offset(cx + legSpread, kneeY)

    val ankleLeft = Offset(cx - legSpread - 4f, ankleY)
    val ankleRight = Offset(cx + legSpread + 4f, ankleY)

    val quadThickness = 21f * p.legScale

    // Left Thigh
    drawLine(baseColor, hipLeft, kneeLeft, strokeWidth = quadThickness, cap = StrokeCap.Round)
    drawOval(highlightColor, topLeft = Offset(kneeLeft.x - 7f, kneeLeft.y - 20f), size = Size(14f, 24f))

    // Right Thigh
    drawLine(baseColor, hipRight, kneeRight, strokeWidth = quadThickness, cap = StrokeCap.Round)
    drawOval(highlightColor, topLeft = Offset(kneeRight.x - 7f, kneeRight.y - 20f), size = Size(14f, 24f))

    // Calves & Shins
    drawLine(baseColor, kneeLeft, ankleLeft, strokeWidth = 14f, cap = StrokeCap.Round)
    drawOval(highlightColor, topLeft = Offset(ankleLeft.x - 4f, ankleLeft.y - 22f), size = Size(12f, 18f))

    drawLine(baseColor, kneeRight, ankleRight, strokeWidth = 14f, cap = StrokeCap.Round)
    drawOval(highlightColor, topLeft = Offset(ankleRight.x - 4f, ankleRight.y - 22f), size = Size(12f, 18f))

    // Knees
    drawCircle(Color(0xFF64748B), radius = 5.5f, center = kneeLeft)
    drawCircle(Color(0xFF64748B), radius = 5.5f, center = kneeRight)

    // Shoes
    val shoeColor = config.shoesColor
    drawRoundRect(shoeColor, topLeft = Offset(ankleLeft.x - 12f, ankleLeft.y), size = Size(20f, 10f), cornerRadius = CornerRadius(3f, 3f))
    drawRoundRect(shoeColor, topLeft = Offset(ankleRight.x - 8f, ankleRight.y), size = Size(20f, 10f), cornerRadius = CornerRadius(3f, 3f))

    // Bottom Clothing (Shorts, Tights, Briefs)
    when (config.bottomClothing) {
        AvatarBottomClothing.GYM_SHORTS -> {
            val shortsPath = Path().apply {
                moveTo(cx - 24f, hipY - 6f)
                lineTo(cx + 24f, hipY - 6f)
                lineTo(cx + legSpread + 8f, hipY + 24f)
                lineTo(cx + 4f, hipY + 22f)
                lineTo(cx - 4f, hipY + 22f)
                lineTo(cx - legSpread - 8f, hipY + 24f)
                close()
            }
            drawPath(shortsPath, color = config.bottomColor)
            // Accent trim on shorts
            drawLine(config.topColor.copy(alpha = 0.8f), Offset(cx - legSpread - 8f, hipY + 23f), Offset(cx - 4f, hipY + 21f), strokeWidth = 2f)
            drawLine(config.topColor.copy(alpha = 0.8f), Offset(cx + 4f, hipY + 21f), Offset(cx + legSpread + 8f, hipY + 23f), strokeWidth = 2f)
        }
        AvatarBottomClothing.COMPRESSION_TIGHTS -> {
            drawLine(config.bottomColor, hipLeft, kneeLeft, strokeWidth = quadThickness + 1.5f, cap = StrokeCap.Round)
            drawLine(config.bottomColor, hipRight, kneeRight, strokeWidth = quadThickness + 1.5f, cap = StrokeCap.Round)
            drawLine(config.bottomColor, kneeLeft, Offset(ankleLeft.x, ankleLeft.y - 6f), strokeWidth = 15f, cap = StrokeCap.Round)
            drawLine(config.bottomColor, kneeRight, Offset(ankleRight.x, ankleRight.y - 6f), strokeWidth = 15f, cap = StrokeCap.Round)
        }
        AvatarBottomClothing.BRIEFS -> {
            val briefsPath = Path().apply {
                moveTo(cx - 22f, hipY - 6f)
                lineTo(cx + 22f, hipY - 6f)
                lineTo(cx + 10f, hipY + 16f)
                lineTo(cx - 10f, hipY + 16f)
                close()
            }
            drawPath(briefsPath, color = config.bottomColor)
        }
    }

    // 2. TORSO BASE (V-Taper Latissimus, Pecs, 6-Pack Abs, Obliques)
    val torsoPath = Path().apply {
        moveTo(cx - chestW / 2f, shoulderY)
        lineTo(cx + chestW / 2f, shoulderY)
        lineTo(cx + 17f, hipY)
        lineTo(cx - 17f, hipY)
        close()
    }
    drawPath(torsoPath, color = baseColor)

    // Latissimus Dorsi (Wings flare)
    drawOval(highlightColor, topLeft = Offset(cx - chestW * 0.58f, shoulderY + 14f), size = Size(16f, 34f))
    drawOval(highlightColor, topLeft = Offset(cx + chestW * 0.58f - 16f, shoulderY + 14f), size = Size(16f, 34f))

    // Pectoralis Major (Sinə Əzələləri)
    val pecH = 26f
    val pecW = chestW * 0.44f
    val leftPec = Path().apply {
        moveTo(cx - 2f, shoulderY + 4f)
        lineTo(cx - pecW, shoulderY + 3f)
        lineTo(cx - pecW, shoulderY + pecH)
        lineTo(cx - 2f, shoulderY + pecH + 2f)
        close()
    }
    drawPath(leftPec, color = highlightColor)

    val rightPec = Path().apply {
        moveTo(cx + 2f, shoulderY + 4f)
        lineTo(cx + pecW, shoulderY + 3f)
        lineTo(cx + pecW, shoulderY + pecH)
        lineTo(cx + 2f, shoulderY + pecH + 2f)
        close()
    }
    drawPath(rightPec, color = highlightColor)

    // Sternum separation line
    drawLine(shadowColor, Offset(cx, shoulderY + 2f), Offset(cx, shoulderY + pecH + 3f), strokeWidth = 2.5f)

    // 6-Pack Abs
    val absTop = shoulderY + pecH + 4f
    val packW = 10f
    val packH = 8f
    for (row in 0..2) {
        val y = absTop + row * (packH + 2.5f)
        drawRoundRect(highlightColor, topLeft = Offset(cx - packW - 1.5f, y), size = Size(packW, packH), cornerRadius = CornerRadius(2.5f, 2.5f))
        drawRoundRect(highlightColor, topLeft = Offset(cx + 1.5f, y), size = Size(packW, packH), cornerRadius = CornerRadius(2.5f, 2.5f))
    }
    drawLine(shadowColor, Offset(cx, absTop - 1f), Offset(cx, absTop + 3 * (packH + 2.5f)), strokeWidth = 2f)

    // Top Clothing (Tank Top or Compression Tee)
    when (config.topClothing) {
        AvatarTopClothing.SHIRTLESS -> {
            // Natural chest & abs display
        }
        AvatarTopClothing.TANK_TOP -> {
            // Racerback athletic tank top
            val tankPath = Path().apply {
                moveTo(cx - chestW * 0.32f, shoulderY)
                lineTo(cx - chestW * 0.16f, shoulderY)
                lineTo(cx - 6f, shoulderY + 16f)
                lineTo(cx + 6f, shoulderY + 16f)
                lineTo(cx + chestW * 0.16f, shoulderY)
                lineTo(cx + chestW * 0.32f, shoulderY)
                lineTo(cx + chestW * 0.44f, shoulderY + 24f)
                lineTo(cx + 16f, hipY - 2f)
                lineTo(cx - 16f, hipY - 2f)
                lineTo(cx - chestW * 0.44f, shoulderY + 24f)
                close()
            }
            drawPath(tankPath, color = config.topColor)
        }
        AvatarTopClothing.COMPRESSION_TEE -> {
            val teePath = Path().apply {
                moveTo(cx - chestW * 0.52f, shoulderY)
                lineTo(cx + chestW * 0.52f, shoulderY)
                lineTo(cx + 16f, hipY - 2f)
                lineTo(cx - 16f, hipY - 2f)
                close()
            }
            drawPath(teePath, color = config.topColor)
        }
    }

    // Weightlifting Belt (Ağır Atletika Kəməri)
    if (config.liftingBelt) {
        val beltY = hipY - 14f
        drawRoundRect(
            color = config.liftingBeltColor,
            topLeft = Offset(cx - 21f, beltY),
            size = Size(42f, 13f),
            cornerRadius = CornerRadius(3f, 3f)
        )
        // Chrome steel buckle
        drawRoundRect(
            color = Color(0xFFE2E8F0),
            topLeft = Offset(cx - 6f, beltY + 1.5f),
            size = Size(12f, 10f),
            cornerRadius = CornerRadius(1.5f, 1.5f),
            style = Stroke(width = 2f)
        )
        // Prong pin
        drawLine(Color(0xFFE2E8F0), Offset(cx, beltY + 2.5f), Offset(cx, beltY + 9.5f), strokeWidth = 2f)
    }

    // 3. SHOULDERS (Deltoids) & ARMS
    val shoulderL = Offset(cx - chestW / 2f, shoulderY)
    val shoulderR = Offset(cx + chestW / 2f, shoulderY)
    val deltRadius = 13f * p.armScale

    // Draw Deltoid Caps
    drawCircle(highlightColor, radius = deltRadius, center = shoulderL)
    drawCircle(highlightColor, radius = deltRadius, center = shoulderR)

    val armBulk = 15f * p.armScale
    val bicepPeak = 9f * p.armScale

    when (pose) {
        AvatarPose.FRONT_FLEX -> {
            // Front Double Biceps pose (Elbows up, biceps peaked, wrists curled)
            val elbowL = Offset(cx - chestW * 0.72f - 6f, shoulderY - 8f)
            val elbowR = Offset(cx + chestW * 0.72f + 6f, shoulderY - 8f)

            val wristL = Offset(cx - chestW * 0.52f, shoulderY - 34f)
            val wristR = Offset(cx + chestW * 0.52f, shoulderY - 34f)

            // Upper arms
            drawLine(baseColor, shoulderL, elbowL, strokeWidth = armBulk, cap = StrokeCap.Round)
            drawLine(baseColor, shoulderR, elbowR, strokeWidth = armBulk, cap = StrokeCap.Round)

            // BICEP PEAKS
            val bicepMidL = Offset((shoulderL.x + elbowL.x) / 2f, (shoulderL.y + elbowL.y) / 2f - 4f)
            val bicepMidR = Offset((shoulderR.x + elbowR.x) / 2f, (shoulderR.y + elbowR.y) / 2f - 4f)

            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(config.topColor.copy(alpha = 0.5f), highlightColor, baseColor),
                    center = bicepMidL,
                    radius = bicepPeak * 1.5f
                ),
                topLeft = Offset(bicepMidL.x - bicepPeak, bicepMidL.y - bicepPeak),
                size = Size(bicepPeak * 2f, bicepPeak * 1.8f)
            )
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(config.topColor.copy(alpha = 0.5f), highlightColor, baseColor),
                    center = bicepMidR,
                    radius = bicepPeak * 1.5f
                ),
                topLeft = Offset(bicepMidR.x - bicepPeak, bicepMidR.y - bicepPeak),
                size = Size(bicepPeak * 2f, bicepPeak * 1.8f)
            )

            // Forearms
            drawLine(highlightColor, elbowL, wristL, strokeWidth = armBulk * 0.85f, cap = StrokeCap.Round)
            drawLine(highlightColor, elbowR, wristR, strokeWidth = armBulk * 0.85f, cap = StrokeCap.Round)

            // Wrist Wraps
            if (config.wristWraps) {
                drawCircle(config.wristWrapsColor, radius = 7.5f, center = wristL)
                drawCircle(config.wristWrapsColor, radius = 7.5f, center = wristR)
            }

            // Clenched fists
            drawCircle(highlightColor, radius = 6.5f, center = Offset(wristL.x + 2f, wristL.y - 4f))
            drawCircle(highlightColor, radius = 6.5f, center = Offset(wristR.x - 2f, wristR.y - 4f))
        }

        AvatarPose.RELAXED_STAND -> {
            // Relaxed athletic stand with arms down
            val elbowL = Offset(cx - chestW * 0.65f, cy + 4f)
            val elbowR = Offset(cx + chestW * 0.65f, cy + 4f)
            val wristL = Offset(cx - chestW * 0.6f, cy + 38f)
            val wristR = Offset(cx + chestW * 0.6f, cy + 38f)

            drawLine(baseColor, shoulderL, elbowL, strokeWidth = armBulk, cap = StrokeCap.Round)
            drawLine(baseColor, shoulderR, elbowR, strokeWidth = armBulk, cap = StrokeCap.Round)

            drawLine(highlightColor, elbowL, wristL, strokeWidth = armBulk * 0.85f, cap = StrokeCap.Round)
            drawLine(highlightColor, elbowR, wristR, strokeWidth = armBulk * 0.85f, cap = StrokeCap.Round)

            if (config.wristWraps) {
                drawCircle(config.wristWrapsColor, radius = 7f, center = wristL)
                drawCircle(config.wristWrapsColor, radius = 7f, center = wristR)
            }
            drawCircle(highlightColor, radius = 6f, center = wristL)
            drawCircle(highlightColor, radius = 6f, center = wristR)
        }

        AvatarPose.CHEST_FLEX -> {
            // Most Muscular / Hands clasping together in front
            val elbowL = Offset(cx - chestW * 0.68f, cy - 6f)
            val elbowR = Offset(cx + chestW * 0.68f, cy - 6f)
            val wristCenter = Offset(cx, cy + 18f)

            drawLine(baseColor, shoulderL, elbowL, strokeWidth = armBulk, cap = StrokeCap.Round)
            drawLine(baseColor, shoulderR, elbowR, strokeWidth = armBulk, cap = StrokeCap.Round)

            drawLine(highlightColor, elbowL, wristCenter, strokeWidth = armBulk * 0.85f, cap = StrokeCap.Round)
            drawLine(highlightColor, elbowR, wristCenter, strokeWidth = armBulk * 0.85f, cap = StrokeCap.Round)

            if (config.wristWraps) {
                drawCircle(config.wristWrapsColor, radius = 7.5f, center = Offset(cx - 5f, cy + 18f))
                drawCircle(config.wristWrapsColor, radius = 7.5f, center = Offset(cx + 5f, cy + 18f))
            }
            drawCircle(highlightColor, radius = 7f, center = wristCenter)
        }
    }

    // 4. HEAD, HAIR & ACCESSORIES
    val headCenter = Offset(cx, shoulderY - 20f)

    // Neck
    drawRoundRect(
        color = highlightColor,
        topLeft = Offset(cx - 6f, shoulderY - 10f),
        size = Size(12f, 14f),
        cornerRadius = CornerRadius(3f, 3f)
    )

    // Cranium
    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(highlightColor, baseColor, shadowColor),
            start = Offset(headCenter.x - headRadius, headCenter.y - headRadius),
            end = Offset(headCenter.x + headRadius, headCenter.y + headRadius)
        ),
        topLeft = Offset(headCenter.x - headRadius * 0.85f, headCenter.y - headRadius * 1.1f),
        size = Size(headRadius * 1.7f, headRadius * 2.2f)
    )

    // Jawline
    val jawPath = Path().apply {
        moveTo(headCenter.x - headRadius * 0.65f, headCenter.y + 2f)
        lineTo(headCenter.x - headRadius * 0.35f, headCenter.y + headRadius * 0.95f)
        lineTo(headCenter.x + headRadius * 0.35f, headCenter.y + headRadius * 0.95f)
        lineTo(headCenter.x + headRadius * 0.65f, headCenter.y + 2f)
    }
    drawPath(jawPath, color = highlightColor, style = Stroke(width = 1.5f, cap = StrokeCap.Round))

    // Eyes / athletic visor line
    drawLine(
        color = Color(0x33FFFFFF),
        start = Offset(headCenter.x - headRadius * 0.55f, headCenter.y - 1f),
        end = Offset(headCenter.x + headRadius * 0.55f, headCenter.y - 1f),
        strokeWidth = 2f,
        cap = StrokeCap.Round
    )

    // HAIR RENDERING
    val hColor = config.hairColor.color
    when (config.hairStyle) {
        AvatarHairStyle.BALD -> {
            // Clean shaved head
        }
        AvatarHairStyle.BUZZ_CUT -> {
            // Tight athletic buzz cut
            val hairPath = Path().apply {
                moveTo(headCenter.x - headRadius * 0.85f, headCenter.y - 2f)
                lineTo(headCenter.x - headRadius * 0.85f, headCenter.y - headRadius * 1.1f)
                lineTo(headCenter.x + headRadius * 0.85f, headCenter.y - headRadius * 1.1f)
                lineTo(headCenter.x + headRadius * 0.85f, headCenter.y - 2f)
                close()
            }
            drawPath(hairPath, color = hColor)
        }
        AvatarHairStyle.FADE -> {
            // Modern fade cut
            drawRoundRect(
                color = hColor,
                topLeft = Offset(headCenter.x - headRadius * 0.82f, headCenter.y - headRadius * 1.25f),
                size = Size(headRadius * 1.64f, headRadius * 0.9f),
                cornerRadius = CornerRadius(5f, 5f)
            )
            // Fade sides
            drawLine(hColor.copy(alpha = 0.5f), Offset(headCenter.x - headRadius * 0.8f, headCenter.y - 4f), Offset(headCenter.x - headRadius * 0.8f, headCenter.y + 2f), strokeWidth = 3f)
            drawLine(hColor.copy(alpha = 0.5f), Offset(headCenter.x + headRadius * 0.8f, headCenter.y - 4f), Offset(headCenter.x + headRadius * 0.8f, headCenter.y + 2f), strokeWidth = 3f)
        }
        AvatarHairStyle.CURLY -> {
            // Textured curly hair curls
            for (i in -3..3) {
                drawCircle(hColor, radius = 4f, center = Offset(headCenter.x + i * 3.5f, headCenter.y - headRadius * 1.15f))
            }
        }
        AvatarHairStyle.PONYTAIL -> {
            // Hair pulled into high ponytail
            drawRoundRect(
                color = hColor,
                topLeft = Offset(headCenter.x - headRadius * 0.8f, headCenter.y - headRadius * 1.15f),
                size = Size(headRadius * 1.6f, headRadius * 0.85f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Ponytail flowing back
            val ponyPath = Path().apply {
                moveTo(headCenter.x + headRadius * 0.6f, headCenter.y - headRadius * 0.8f)
                lineTo(headCenter.x + headRadius * 1.5f, headCenter.y - headRadius * 0.3f)
                lineTo(headCenter.x + headRadius * 1.3f, headCenter.y + headRadius * 0.6f)
                lineTo(headCenter.x + headRadius * 0.7f, headCenter.y + 2f)
                close()
            }
            drawPath(ponyPath, color = hColor)
            // Hair tie band
            drawCircle(config.topColor, radius = 2.5f, center = Offset(headCenter.x + headRadius * 0.65f, headCenter.y - headRadius * 0.6f))
        }
        AvatarHairStyle.UNDERCUT -> {
            val hairPath = Path().apply {
                moveTo(headCenter.x - headRadius * 0.8f, headCenter.y - headRadius * 1.3f)
                lineTo(headCenter.x + headRadius * 0.9f, headCenter.y - headRadius * 1.2f)
                lineTo(headCenter.x + headRadius * 0.7f, headCenter.y - 2f)
                lineTo(headCenter.x - headRadius * 0.7f, headCenter.y - 2f)
                close()
            }
            drawPath(hairPath, color = hColor)
        }
    }

    // Headband (Baş Tər Bəndi)
    if (config.headband) {
        val bandY = headCenter.y - headRadius * 0.45f
        drawRoundRect(
            color = config.headbandColor,
            topLeft = Offset(headCenter.x - headRadius * 0.85f, bandY),
            size = Size(headRadius * 1.7f, 4.5f),
            cornerRadius = CornerRadius(1.5f, 1.5f)
        )
    }
}
