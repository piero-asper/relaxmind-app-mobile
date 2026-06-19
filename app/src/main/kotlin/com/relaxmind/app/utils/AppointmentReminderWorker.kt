package com.relaxmind.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AppointmentReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val appointmentId = inputData.getString("appointmentId") ?: return Result.failure()
        val title = inputData.getString("title") ?: "Recordatorio de evento"
        val type = inputData.getString("type") ?: "recordatorio"

        val message = when (type) {
            "cita" -> "Tienes una cita médica pronto: $title"
            "medicacion" -> "Es hora de tomar tu medicación: $title"
            else -> "Recordatorio: $title"
        }

        showNotification(title, message)

        // Mark as notificationSent in Firestore
        runCatching {
            FirebaseFirestore.getInstance()
                .collection("appointments")
                .document(appointmentId)
                .update("notificationSent", true)
                .await()
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "appointments_reminder_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios de Agenda",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones para citas, medicaciones y recordatorios."
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // System standard alarm icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
