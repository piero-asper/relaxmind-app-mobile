package com.relaxmind.app.data.model

data class UserAchievement(
    val id: String = "",
    val patientId: String = "",
    val achievementKey: String = "",
    val type: String = "",
    val title: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val unlockedAt: String = "",
    val streakCount: Int? = null
)
