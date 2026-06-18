package com.relaxmind.app.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.relaxmind.app.data.model.Caregiver
import com.relaxmind.app.data.model.Patient
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val patients = firestore.collection(PATIENTS_COLLECTION)
    private val caregivers = firestore.collection(CAREGIVERS_COLLECTION)

    suspend fun createPatient(patient: Patient): Result<Unit> = runCatching {
        require(patient.id.isNotBlank()) { "Patient id cannot be blank." }
        patients.document(patient.id).set(patient).await()
    }

    suspend fun createCaregiver(caregiver: Caregiver): Result<Unit> = runCatching {
        require(caregiver.id.isNotBlank()) { "Caregiver id cannot be blank." }
        caregivers.document(caregiver.id).set(caregiver).await()
    }

    suspend fun getPatientById(id: String): Result<Patient?> = runCatching {
        patients.document(id).get().await().toObject(Patient::class.java)
    }

    suspend fun getCaregiverById(id: String): Result<Caregiver?> = runCatching {
        caregivers.document(id).get().await().toObject(Caregiver::class.java)
    }

    suspend fun updatePatient(
        id: String,
        fields: Map<String, Any>
    ): Result<Unit> = runCatching {
        patients.document(id).update(fields).await()
    }

    suspend fun updateCaregiver(
        id: String,
        fields: Map<String, Any>
    ): Result<Unit> = runCatching {
        caregivers.document(id).update(fields).await()
    }

    suspend fun getRoleById(id: String): Result<String> = runCatching {
        val patientSnapshot = patients.document(id).get().await()
        if (patientSnapshot.exists()) {
            return@runCatching patientSnapshot.getString("role") ?: "patient"
        }

        val caregiverSnapshot = caregivers.document(id).get().await()
        if (caregiverSnapshot.exists()) {
            return@runCatching caregiverSnapshot.getString("role") ?: "caregiver"
        }

        error("No user role found for id: $id")
    }

    private companion object {
        const val PATIENTS_COLLECTION = "patients"
        const val CAREGIVERS_COLLECTION = "caregivers"
    }
}
