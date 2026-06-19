package com.relaxmind.app.data.model

data class MeditationExercise(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "", // "respiracion" | "mindfulness" | "relajacion"
    val durationMinutes: Int = 0,
    val lottieAnimationUrl: String = "",
    val order: Int = 0
)
