package com.example.safeguardapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.Telephony
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SpamSmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {

            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            for (sms in messages) {
                val message = sms.messageBody ?: ""
                val sender = sms.displayOriginatingAddress ?: ""

                val isSpam = isSpamMessage(message)
                val isOtp = isOtpMessage(message)

                if (isSpam || isOtp) {

                    // 🔊 BUZZER
                    try {
                        val mediaPlayer = MediaPlayer.create(context, R.raw.buzzer)
                        mediaPlayer.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // 🔔 NOTIFICATION
                    showNotification(
                        context,
                        if (isOtp) "🔐 OTP Message Detected" else "⚠️ Spam SMS Detected",
                        if (isOtp) message else "From: $sender"
                    )
                }
            }
        }
    }

    private fun isSpamMessage(message: String): Boolean {
        val spamKeywords = listOf(
            "win", "prize", "offer", "lottery",
            "free", "money", "click", "link"
        )
        val lower = message.lowercase()
        return spamKeywords.any { lower.contains(it) }
    }

    private fun isOtpMessage(message: String): Boolean {
        val otpRegex = Regex("\\b\\d{4,8}\\b")
        val lower = message.lowercase()
        return otpRegex.containsMatchIn(message) &&
                (lower.contains("otp") || lower.contains("verification"))
    }

    private fun showNotification(context: Context, title: String, text: String) {

        val channelId = "spam_alert_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Spam & OTP Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(title)
            .setContentText(text.take(80))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
