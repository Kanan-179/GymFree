package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import com.example.util.QrCodeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class QrDisplayType {
    TRAINER_PROFILE,
    STUDENT_PROFILE,
    ASSIGNMENT,
    PROGRESS_REPORT
}

@Composable
fun QrCodeDisplayDialog(
    type: QrDisplayType,
    title: String,
    subtitle: String,
    payloadText: String,
    detailsSummary: List<Pair<String, String>> = emptyList(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var qrBitmap by remember(payloadText) { mutableStateOf<Bitmap?>(null) }
    var isGenerating by remember { mutableStateOf(true) }

    LaunchedEffect(payloadText) {
        isGenerating = true
        withContext(Dispatchers.Default) {
            try {
                qrBitmap = QrCodeUtils.generateQrBitmap(payloadText, 512)
            } catch (e: Exception) {
                qrBitmap = null
            }
        }
        isGenerating = false
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, DarkSurfaceVariant, RoundedCornerShape(24.dp))
                .testTag("qr_code_display_dialog"),
            color = DarkSurface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    when (type) {
                                        QrDisplayType.TRAINER_PROFILE -> CyanPrimary.copy(alpha = 0.2f)
                                        QrDisplayType.STUDENT_PROFILE -> OrangeAccent.copy(alpha = 0.2f)
                                        QrDisplayType.ASSIGNMENT -> LimeSuccess.copy(alpha = 0.2f)
                                        QrDisplayType.PROGRESS_REPORT -> Color(0xFF9C27B0).copy(alpha = 0.2f)
                                    },
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (type) {
                                    QrDisplayType.TRAINER_PROFILE -> Icons.Default.SportsScore
                                    QrDisplayType.STUDENT_PROFILE -> Icons.Default.Person
                                    QrDisplayType.ASSIGNMENT -> Icons.Default.Assignment
                                    QrDisplayType.PROGRESS_REPORT -> Icons.Default.CheckCircle
                                },
                                contentDescription = null,
                                tint = when (type) {
                                    QrDisplayType.TRAINER_PROFILE -> CyanPrimary
                                    QrDisplayType.STUDENT_PROFILE -> OrangeAccent
                                    QrDisplayType.ASSIGNMENT -> LimeSuccess
                                    QrDisplayType.PROGRESS_REPORT -> Color(0xFFBA68C8)
                                },
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = title,
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = subtitle,
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Bağla", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // The White Container with Sharp QR Code
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .size(240.dp)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                color = CyanPrimary,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(36.dp)
                            )
                        } else if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap!!.asImageBitmap(),
                                contentDescription = "QR Kod",
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = "QR Kod yaradıla bilmədi",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Instruction Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = when (type) {
                                QrDisplayType.TRAINER_PROFILE -> "Tələbə bu QR kodu öz telefonunda oxudaraq sizi şəxsi məşqçisi təyin edir."
                                QrDisplayType.STUDENT_PROFILE -> "Məşqçi bu QR kodu oxudaraq sizi birbaşa öz tələbələri siyahısına əlavə edir."
                                QrDisplayType.ASSIGNMENT -> "Tələbə bu QR kodu oxutduqda təyin etdiyiniz məşq proqramı onun telefonuna yüklənir."
                                QrDisplayType.PROGRESS_REPORT -> "Məşqçi bu QR kodu oxutduqda qaldırdığınız real çəkilər və status onun sistemində yenilənir."
                            },
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Summary details chips
                if (detailsSummary.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkBackground, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        detailsSummary.forEach { (label, value) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = label, color = TextMuted, fontSize = 12.sp)
                                Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Action Buttons: Copy, Share, Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("FitMəşq QR Məlumatı", payloadText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Məlumat kopyalandı! WhatsApp və ya mesajla göndərə bilərsiniz.", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextPrimary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(DarkSurfaceVariant)
                        )
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Kodu Kopyala", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "FitMəşq $title:\n\n$payloadText")
                                setType("text/plain")
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "FitMəşq Məlumatını Paylaş")
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = CyanOnPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Paylaş", color = CyanOnPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
