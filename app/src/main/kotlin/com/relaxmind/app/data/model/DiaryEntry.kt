package com.relaxmind.app.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class DiaryEntry(
    val id: String = "",
    val patientId: String = "",
    val category: String = "", // "Estrés" | "Familia" | "Trabajo" | "Logro" | "Otro"
    val emotion: String = "", // "Ansioso" | "Tranquilo" | "Feliz" | "Triste" | "Frustrado" | "Emocionado"
    val notes: String = "",
    val photoUrls: List<String> = emptyList(),
    val date: String = "", // YYYY-MM-DD
    @ServerTimestamp
    val createdAt: Date? = null
)
