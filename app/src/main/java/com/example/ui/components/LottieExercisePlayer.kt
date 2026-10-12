package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.ui.theme.*

@Composable
fun LottieExercisePlayer(
    lottieUrl: String,
    isPlaying: Boolean,
    speedMultiplier: Float,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(lottieUrl)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        speed = speedMultiplier,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = false
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .testTag("lottie_player_box"),
        contentAlignment = Alignment.Center
    ) {
        if (composition == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = CyanPrimary,
                    modifier = Modifier.size(36.dp),
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Animasiya Yüklənir...",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        } else {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("lottie_animation_view")
            )
        }
    }
}
