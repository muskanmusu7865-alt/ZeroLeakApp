package com.example.safeguardapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class FraudCallReceiver : BroadcastReceiver() {

    private val fraudNumbers = listOf(
        "+911234567890",
        "+919876543210",
        "140"
    )

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {

            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber =
                intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {

                if (fraudNumbers.any { incomingNumber.contains(it) }) {

                    // 🔊 Play buzzer
                    val mp = MediaPlayer.create(context, R.raw.buzzer)
                    mp.start()

                    // 🔔 Notification
                    val notification = NotificationCompat.Builder(
                        context,
                        "fraud_alert_channel"
                    )
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Fraud Call Alert")
                        .setContentText("Fraud call from $incomingNumber")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    NotificationManagerCompat.from(context)
                        .notify(101, notification)

                    // 📢 Toast (safe)
                    Toast.makeText(
                        context,
                        "Fraud Call Detected",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
