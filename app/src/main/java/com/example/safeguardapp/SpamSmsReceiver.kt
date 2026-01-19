package com.example.safeguardapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast

class SpamSmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION != intent.action) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        for (sms in messages) {

            val body = sms.messageBody ?: ""
            val sender = sms.displayOriginatingAddress ?: "Unknown Number"

            // 1️⃣ OTP / ANY DIGITS
            val hasAnyDigit = body.any { it.isDigit() }

            // 2️⃣ LINK DETECTION
            val hasLink =
                body.contains("http://", true) ||
                        body.contains("https://", true) ||
                        body.contains("www.", true) ||
                        body.contains("bit.ly", true) ||
                        body.contains("tinyurl", true) ||
                        body.contains("url", true)

            // 3️⃣ SPAM KEYWORDS
            val spamKeywords = listOf(
                "otp", "win", "offer", "free", "reward",
                "lottery", "urgent", "kyc", "verify", "click",
                "account", "blocked", "suspended", "update",
                "bank", "alert", "upi"
            )

            val hasSpamKeyword = spamKeywords.any {
                body.contains(it, ignoreCase = true)
            }

            // 4️⃣ DETECTION REASON
            val reason = when {
                hasAnyDigit && hasLink -> "OTP + Link detected"
                hasAnyDigit -> "OTP detected"
                hasLink -> "Link detected"
                hasSpamKeyword -> "Spam keyword detected"
                else -> "Suspicious SMS"
            }

            // FINAL ALERT CONDITION
            if (hasAnyDigit || hasLink || hasSpamKeyword) {

                // 🔔 BUZZER
                BuzzerHelper.play(context)

                // 📜 SAVE HISTORY (FULL MESSAGE + REASON + NUMBER)
                AlertHistoryManager.save(
                    context,
                    type = "SMS",
                    number = sender,
                    message = body,
                    detail = reason
                )

                // ⚠️ USER ALERT
                Toast.makeText(
                    context,
                    "⚠️ Suspicious SMS from $sender",
                    Toast.LENGTH_LONG
                ).show()

                break
            }
        }
    }
}
