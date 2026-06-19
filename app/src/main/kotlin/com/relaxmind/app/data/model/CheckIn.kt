package com.relaxmind.app.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FrequencyAnswer(
    val questionId: String = "",
    val value: Int = 0
)

data class BinaryAnswer(
    val questionId: String = "",
    val value: Int = 0
)

data class CheckIn(
    val id: String = "",
    val patientId: String = "",
    val type: String = "", // "initial_test" | "daily_checkin"
    val date: String = "", // YYYY-MM-DD
    val score: Int = 0,
    val category: String = "", // "Muy bajo" | "Bajo" | "Moderado" | "Bueno" | "Excelente"
    val emotionalState: Int = 0,
    val sleep: Int? = null,
    val energy: Int = 0,
    val stress: Int = 0,
    val frequencyAnswers: List<FrequencyAnswer> = emptyList(),
    val binaryAnswers: List<BinaryAnswer> = emptyList(),
    val notes: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)
