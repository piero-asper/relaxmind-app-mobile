package com.relaxmind.app.features.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relaxmind.app.data.model.Appointment
import com.relaxmind.app.data.model.Caregiver
import com.relaxmind.app.data.model.CheckIn
import com.relaxmind.app.data.model.DailyGoal
import com.relaxmind.app.data.model.MeditationExercise
import com.relaxmind.app.data.model.Patient
import com.relaxmind.app.data.model.Streak
import com.relaxmind.app.data.model.UserAchievement
import com.relaxmind.app.data.remote.FirebaseAuthService
import com.relaxmind.app.data.remote.FirestoreRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PatientViewModel(
    private val authService: FirebaseAuthService = FirebaseAuthService(),
    private val firestoreRepository: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _patient = MutableStateFlow<Patient?>(null)
    val patient = _patient.asStateFlow()

    private val _todayCheckIn = MutableStateFlow<CheckIn?>(null)
    val todayCheckIn = _todayCheckIn.asStateFlow()

    private val _dailyGoal = MutableStateFlow<DailyGoal?>(null)
    val dailyGoal = _dailyGoal.asStateFlow()

    private val _dailyGoalExercise = MutableStateFlow<MeditationExercise?>(null)
    val dailyGoalExercise = _dailyGoalExercise.asStateFlow()

    private val _nextAppointment = MutableStateFlow<Appointment?>(null)
    val nextAppointment = _nextAppointment.asStateFlow()

    private val _caregiver = MutableStateFlow<Caregiver?>(null)
    val caregiver = _caregiver.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _streak = MutableStateFlow<Streak?>(null)
    val streak = _streak.asStateFlow()

    private val _achievements = MutableStateFlow<List<UserAchievement>>(emptyList())
    val achievements = _achievements.asStateFlow()

    private val _allCheckIns = MutableStateFlow<List<CheckIn>>(emptyList())
    val allCheckIns = _allCheckIns.asStateFlow()

    private val _selectedYear = MutableStateFlow(LocalDate.now().year)
    val selectedYear = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonth = _selectedMonth.asStateFlow()

    fun loadDashboardData() {
        val userId = authService.getCurrentUser()?.uid ?: run {
            _error.value = "No hay sesión activa."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val patientResult = firestoreRepository.getPatientById(userId)
            if (patientResult.isFailure) {
                _error.value = patientResult.exceptionOrNull()?.localizedMessage ?: "Error al cargar datos del paciente."
                _isLoading.value = false
                return@launch
            }

            val patientData = patientResult.getOrNull()
            _patient.value = patientData
            patientData?.let {
                com.relaxmind.app.ui.themes.ThemeState.darkMode.value = it.darkMode
                com.relaxmind.app.ui.themes.ThemeState.language.value = it.language
            }

            if (patientData == null) {
                _error.value = "Los datos del paciente no existen."
                _isLoading.value = false
                return@launch
            }

            val todayDate = LocalDate.now().toString()

            coroutineScope {
                // Fetch check-in
                val checkInDeferred = async { firestoreRepository.getTodayCheckIn(userId, todayDate) }
                // Fetch daily goal
                val dailyGoalDeferred = async { firestoreRepository.getDailyGoal(userId, todayDate) }
                // Fetch appointments
                val appointmentsDeferred = async { firestoreRepository.getAppointments(userId, todayDate) }
                // Fetch caregiver (if linked)
                val caregiverDeferred = async {
                    patientData.caregiverId?.let { firestoreRepository.getCaregiverById(it) }
                }

                val checkInResult = checkInDeferred.await()
                val dailyGoalResult = dailyGoalDeferred.await()
                val appointmentsResult = appointmentsDeferred.await()
                val caregiverResult = caregiverDeferred.await()

                // Map CheckIn
                _todayCheckIn.value = checkInResult.getOrNull()

                // Map DailyGoal & Exercise
                val goal = dailyGoalResult.getOrNull()
                _dailyGoal.value = goal
                if (goal != null) {
                    val exerciseResult = firestoreRepository.getMeditationExercise(goal.exerciseId)
                    _dailyGoalExercise.value = exerciseResult.getOrNull()
                } else {
                    _dailyGoalExercise.value = null
                }

                // Map Next Appointment
                val list = appointmentsResult.getOrNull() ?: emptyList()
                _nextAppointment.value = list
                    .filter { !it.completed }
                    .minByOrNull {
                        runCatching {
                            LocalTime.parse(it.time, DateTimeFormatter.ofPattern("HH:mm"))
                        }.getOrDefault(LocalTime.MAX)
                    }

                // Map Caregiver
                _caregiver.value = caregiverResult?.getOrNull()
            }

            _isLoading.value = false
        }
    }

    fun toggleDailyGoalCompletion(completed: Boolean) {
        val goal = _dailyGoal.value ?: return
        viewModelScope.launch {
            val result = firestoreRepository.updateDailyGoalCompletion(goal.id, completed)
            if (result.isSuccess) {
                _dailyGoal.update { it?.copy(completed = completed) }
            } else {
                _error.value = result.exceptionOrNull()?.localizedMessage ?: "Error al actualizar la meta."
            }
        }
    }

    private fun updatePatientField(fieldName: String, value: Any, updateLocal: (Patient) -> Patient) {
        val userId = authService.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            _patient.update { it?.let(updateLocal) }
            val result = firestoreRepository.updatePatient(userId, mapOf(fieldName to value))
            if (result.isFailure) {
                _error.value = result.exceptionOrNull()?.localizedMessage ?: "Error al actualizar."
                loadDashboardData()
            }
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        com.relaxmind.app.ui.themes.ThemeState.darkMode.value = enabled
        updatePatientField("darkMode", enabled) { it.copy(darkMode = enabled) }
    }

    fun updateLanguage(lang: String) {
        com.relaxmind.app.ui.themes.ThemeState.language.value = lang
        updatePatientField("language", lang) { it.copy(language = lang) }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        updatePatientField("notificationsEnabled", enabled) { it.copy(notificationsEnabled = enabled) }
    }

    fun updateCheckInReminderEnabled(enabled: Boolean) {
        updatePatientField("checkInReminderEnabled", enabled) { it.copy(checkInReminderEnabled = enabled) }
    }

    fun updateBiometricEnabled(enabled: Boolean) {
        updatePatientField("biometricEnabled", enabled) { it.copy(biometricEnabled = enabled) }
    }

    fun unlinkCaregiver(passwordConfirm: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = authService.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val reauthResult = authService.reauthenticate(passwordConfirm)
            if (reauthResult.isFailure) {
                val errorMsg = reauthResult.exceptionOrNull()?.localizedMessage ?: "Contraseña incorrecta."
                onError(errorMsg)
                _isLoading.value = false
                return@launch
            }

            val updateResult = firestoreRepository.updatePatient(
                userId,
                mapOf<String, Any?>(
                    "caregiverId" to null,
                    "linkedCaregiverAt" to null
                )
            )
            if (updateResult.isSuccess) {
                _patient.update { it?.copy(caregiverId = null, linkedCaregiverAt = null) }
                _caregiver.value = null
                onSuccess()
            } else {
                val errorMsg = updateResult.exceptionOrNull()?.localizedMessage ?: "Error al desvincular."
                onError(errorMsg)
            }
            _isLoading.value = false
        }
    }

    fun deleteAccount(
        reason: String,
        passwordConfirm: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = authService.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val reauthResult = authService.reauthenticate(passwordConfirm)
            if (reauthResult.isFailure) {
                val errorMsg = reauthResult.exceptionOrNull()?.localizedMessage ?: "Contraseña incorrecta."
                onError(errorMsg)
                _isLoading.value = false
                return@launch
            }

            val todayDate = LocalDate.now().toString()
            val updateResult = firestoreRepository.updatePatient(
                userId,
                mapOf(
                    "isDeleted" to true,
                    "deletedAt" to todayDate,
                    "deletionReason" to reason
                )
            )
            if (updateResult.isSuccess) {
                authService.logout()
                onSuccess()
            } else {
                val errorMsg = updateResult.exceptionOrNull()?.localizedMessage ?: "Error al borrar la cuenta."
                onError(errorMsg)
            }
            _isLoading.value = false
        }
    }

    fun loadProgressData() {
        val userId = authService.getCurrentUser()?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val streakResult = firestoreRepository.getPatientStreak(userId)
            val achievementsResult = firestoreRepository.getPatientAchievements(userId)
            val checkInsResult = firestoreRepository.getPatientCheckIns(userId)

            _streak.value = streakResult.getOrNull()
            _achievements.value = achievementsResult.getOrNull() ?: emptyList()
            _allCheckIns.value = (checkInsResult.getOrNull() ?: emptyList()).sortedByDescending { it.date }

            _isLoading.value = false
        }
    }

    fun selectPreviousMonth() {
        if (_selectedMonth.value == 1) {
            _selectedMonth.value = 12
            _selectedYear.value -= 1
        } else {
            _selectedMonth.value -= 1
        }
    }

    fun selectNextMonth() {
        if (_selectedMonth.value == 12) {
            _selectedMonth.value = 1
            _selectedYear.value += 1
        } else {
            _selectedMonth.value += 1
        }
    }

    fun clearError() {
        _error.value = null
    }
}
