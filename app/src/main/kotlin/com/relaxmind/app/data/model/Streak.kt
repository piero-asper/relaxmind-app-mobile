package com.relaxmind.app.data.model

data class Streak(
    val patientId: String = "",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCheckInDate: String? = null,
    val lastNoCheckinAlertDate: String? = null,
    val updatedAt: String? = null
)
