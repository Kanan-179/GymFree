package com.example.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.json.JSONArray
import org.json.JSONObject
import java.util.EnumMap

// Data classes representing the QR code payload types
sealed class QrPayload {
    data class TrainerProfile(
        val trainerId: String,
        val trainerName: String,
        val specialty: String = "Fitness & Bodibildinq",
        val phone: String = ""
    ) : QrPayload()

    data class StudentProfile(
        val studentId: String,
        val fullName: String,
        val phone: String = "",
        val currentWeightKg: Float = 75f,
        val targetWeightKg: Float = 80f,
        val heightCm: Float = 178f,
        val goal: String = "Əzələ Kütləsi",
        val notes: String = ""
    ) : QrPayload()

    data class WorkoutAssignment(
        val assignmentId: String,
        val trainerName: String,
        val studentName: String,
        val dayOfWeek: Int,
        val dayTitle: String,
        val exercises: List<AssignedExerciseItem>
    ) : QrPayload()

    data class StudentProgressReport(
        val reportId: String,
        val studentName: String,
        val dayOfWeek: Int,
        val dayTitle: String,
        val completedCount: Int,
        val results: List<ProgressExerciseItem>
    ) : QrPayload()

    data class Unknown(val rawText: String) : QrPayload()
}

data class AssignedExerciseItem(
    val exerciseId: String,
    val exerciseName: String,
    val muscleGroup: String,
    val targetWeightKg: Float,
    val targetSets: Int = 4,
    val targetReps: Int = 10,
    val trainerNotes: String = ""
)

data class ProgressExerciseItem(
    val exerciseName: String,
    val actualWeightKg: Float,
    val isCompleted: Boolean
)

object QrCodeUtils {

    // Prefix markers for quick identification
    private const val PREFIX_TRAINER = "FITMESQ_TR:"
    private const val PREFIX_STUDENT = "FITMESQ_ST:"
    private const val PREFIX_ASSIGNMENT = "FITMESQ_TASK:"
    private const val PREFIX_PROGRESS = "FITMESQ_PROG:"

    /**
     * Generates a high-contrast monochrome Bitmap from given text.
     */
    fun generateQrBitmap(content: String, sizePx: Int = 512): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
            put(EncodeHintType.MARGIN, 1)
        }
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    /**
     * Decodes QR content from a static Bitmap (e.g. from gallery).
     */
    fun decodeFromBitmap(bitmap: Bitmap): String? {
        return try {
            val intArray = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = MultiFormatReader().decode(binaryBitmap)
            result.text
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Decodes from Camera YUV frame buffer.
     */
    fun decodeFromYuv(
        yuvData: ByteArray,
        width: Int,
        height: Int,
        isRotated: Boolean = false
    ): String? {
        return try {
            val source = PlanarYUVLuminanceSource(
                yuvData,
                width,
                height,
                0,
                0,
                width,
                height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val reader = MultiFormatReader()
            val result = reader.decodeWithState(binaryBitmap)
            result.text
        } catch (e: Exception) {
            null
        }
    }

    // --- PAYLOAD SERIALIZATION ---

    fun encodeTrainerProfile(profile: QrPayload.TrainerProfile): String {
        val json = JSONObject().apply {
            put("type", "TRAINER_PROFILE")
            put("id", profile.trainerId)
            put("name", profile.trainerName)
            put("spec", profile.specialty)
            put("phone", profile.phone)
        }
        return PREFIX_TRAINER + json.toString()
    }

    fun encodeStudentProfile(profile: QrPayload.StudentProfile): String {
        val json = JSONObject().apply {
            put("type", "STUDENT_PROFILE")
            put("id", profile.studentId)
            put("name", profile.fullName)
            put("phone", profile.phone)
            put("curW", profile.currentWeightKg.toDouble())
            put("tarW", profile.targetWeightKg.toDouble())
            put("h", profile.heightCm.toDouble())
            put("goal", profile.goal)
            put("notes", profile.notes)
        }
        return PREFIX_STUDENT + json.toString()
    }

    fun encodeWorkoutAssignment(assignment: QrPayload.WorkoutAssignment): String {
        val json = JSONObject().apply {
            put("type", "ASSIGNMENT")
            put("id", assignment.assignmentId)
            put("trainer", assignment.trainerName)
            put("student", assignment.studentName)
            put("day", assignment.dayOfWeek)
            put("title", assignment.dayTitle)

            val arr = JSONArray()
            assignment.exercises.forEach { ex ->
                val exObj = JSONObject().apply {
                    put("id", ex.exerciseId)
                    put("name", ex.exerciseName)
                    put("muscle", ex.muscleGroup)
                    put("w", ex.targetWeightKg.toDouble())
                    put("s", ex.targetSets)
                    put("r", ex.targetReps)
                    put("notes", ex.trainerNotes)
                }
                arr.put(exObj)
            }
            put("items", arr)
        }
        return PREFIX_ASSIGNMENT + json.toString()
    }

    fun encodeStudentProgress(report: QrPayload.StudentProgressReport): String {
        val json = JSONObject().apply {
            put("type", "PROGRESS_REPORT")
            put("id", report.reportId)
            put("student", report.studentName)
            put("day", report.dayOfWeek)
            put("title", report.dayTitle)
            put("doneCount", report.completedCount)

            val arr = JSONArray()
            report.results.forEach { r ->
                val rObj = JSONObject().apply {
                    put("name", r.exerciseName)
                    put("w", r.actualWeightKg.toDouble())
                    put("done", r.isCompleted)
                }
                arr.put(rObj)
            }
            put("items", arr)
        }
        return PREFIX_PROGRESS + json.toString()
    }

    // --- PAYLOAD PARSING ---

    fun parsePayload(rawText: String): QrPayload {
        val text = rawText.trim()
        val jsonString = when {
            text.startsWith(PREFIX_TRAINER) -> text.removePrefix(PREFIX_TRAINER)
            text.startsWith(PREFIX_STUDENT) -> text.removePrefix(PREFIX_STUDENT)
            text.startsWith(PREFIX_ASSIGNMENT) -> text.removePrefix(PREFIX_ASSIGNMENT)
            text.startsWith(PREFIX_PROGRESS) -> text.removePrefix(PREFIX_PROGRESS)
            else -> text
        }

        return try {
            val json = JSONObject(jsonString)
            when (json.optString("type")) {
                "TRAINER_PROFILE" -> QrPayload.TrainerProfile(
                    trainerId = json.optString("id", "TR-${System.currentTimeMillis() % 10000}"),
                    trainerName = json.optString("name", "Məşqçi"),
                    specialty = json.optString("spec", "Fitness & Bodibildinq"),
                    phone = json.optString("phone", "")
                )
                "STUDENT_PROFILE" -> QrPayload.StudentProfile(
                    studentId = json.optString("id", "ST-${System.currentTimeMillis() % 10000}"),
                    fullName = json.optString("name", "Tələbə"),
                    phone = json.optString("phone", ""),
                    currentWeightKg = json.optDouble("curW", 75.0).toFloat(),
                    targetWeightKg = json.optDouble("tarW", 80.0).toFloat(),
                    heightCm = json.optDouble("h", 178.0).toFloat(),
                    goal = json.optString("goal", "Əzələ Kütləsi"),
                    notes = json.optString("notes", "")
                )
                "ASSIGNMENT" -> {
                    val arr = json.optJSONArray("items") ?: JSONArray()
                    val exercises = mutableListOf<AssignedExerciseItem>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        exercises.add(
                            AssignedExerciseItem(
                                exerciseId = item.optString("id", "ex_$i"),
                                exerciseName = item.optString("name", "Məşq Hərəkəti"),
                                muscleGroup = item.optString("muscle", "Ümumi"),
                                targetWeightKg = item.optDouble("w", 50.0).toFloat(),
                                targetSets = item.optInt("s", 4),
                                targetReps = item.optInt("r", 10),
                                trainerNotes = item.optString("notes", "")
                            )
                        )
                    }
                    QrPayload.WorkoutAssignment(
                        assignmentId = json.optString("id", "TASK-${System.currentTimeMillis()}"),
                        trainerName = json.optString("trainer", "Məşqçi"),
                        studentName = json.optString("student", "Tələbə"),
                        dayOfWeek = json.optInt("day", 1),
                        dayTitle = json.optString("title", "1-ci Gün Məşqi"),
                        exercises = exercises
                    )
                }
                "PROGRESS_REPORT" -> {
                    val arr = json.optJSONArray("items") ?: JSONArray()
                    val results = mutableListOf<ProgressExerciseItem>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        results.add(
                            ProgressExerciseItem(
                                exerciseName = item.optString("name", "Məşq Hərəkəti"),
                                actualWeightKg = item.optDouble("w", 50.0).toFloat(),
                                isCompleted = item.optBoolean("done", true)
                            )
                        )
                    }
                    QrPayload.StudentProgressReport(
                        reportId = json.optString("id", "PROG-${System.currentTimeMillis()}"),
                        studentName = json.optString("student", "Tələbə"),
                        dayOfWeek = json.optInt("day", 1),
                        dayTitle = json.optString("title", "1-ci Gün"),
                        completedCount = json.optInt("doneCount", results.count { it.isCompleted }),
                        results = results
                    )
                }
                else -> QrPayload.Unknown(rawText)
            }
        } catch (e: Exception) {
            QrPayload.Unknown(rawText)
        }
    }
}
