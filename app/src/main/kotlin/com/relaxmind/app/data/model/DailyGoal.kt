package com.relaxmind.app.data.model

data class DailyGoal(
    val id: String = "",
    val patientId: String = "",
    val date: String = "", // YYYY-MM-DD
    val exerciseId: String = "",
    val completed: Boolean = false
)
