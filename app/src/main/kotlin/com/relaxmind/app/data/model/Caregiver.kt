package com.relaxmind.app.data.model

data class Caregiver(
    val id: String = "",
    val role: String = "caregiver",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val emailVerified: Boolean = false,
    val phone: String = "",
    val birthDate: String = "",
    val sex: String = "",
    val avatarUrl: String = "",
    val fcmToken: String = "",
    val darkMode: Boolean = false,
    val language: String = "es",
    val biometricEnabled: Boolean = false,
    val keepSessionActive: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val onboardingCompleted: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedAt: String? = null,
    val deletionReason: String? = null,
    val createdAt: String = ""
)
