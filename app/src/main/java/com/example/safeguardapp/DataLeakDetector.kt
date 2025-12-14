package com.example.safeguardapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DataLeakDetector(private val context: Context) {

    fun startMonitoring() {
        Log.d("DataLeakDetector", "Monitoring started...")

        // Simulated leak detection (replace with real logic later)
        val leakDetected = true

        if (leakDetected) {
            alertUser("Sensitive data leak suspected!")
        }
    }

    private fun alertUser(message: String) {
        // 🔊 Play buzzer sound
        try {
            val mediaPlayer = MediaPlayer.create(context, R.raw.buzzer)
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("DataLeakDetector", "Buzzer failed: ${e.message}")
        }

        // 🪧 Show Toast
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

        // 🔔 Send Notification
        showNotification(message)

        // ⚠️ Show Popup Alert (if context is activity)
        if (context is AppCompatActivity) {
            context.runOnUiThread {
                AlertDialog.Builder(context)
                    .setTitle("⚠️ Data Leak Warning")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun showNotification(message: String) {
        val channelId = "data_leak_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Data Leak Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Notification intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("⚠️ Data Leak Detected")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(1001, builder.build())
    }
}
