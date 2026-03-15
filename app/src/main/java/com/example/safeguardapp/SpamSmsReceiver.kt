package com.example.safeguardapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SpamSmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION != intent.action) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        for (sms in messages) {
            val body = sms.messageBody ?: return
            val sender = sms.displayOriginatingAddress ?: "Unknown"

            // Any digit OR link OR keyword
            val hasDigit = body.any { it.isDigit() }
            val hasLink = listOf("http", "www", "bit.ly").any { body.contains(it, true) }
            val keywords = listOf("otp","kyc","verify","click","urgent","account")
            val hasKeyword = keywords.any { body.contains(it, true) }

            if (hasDigit || hasLink || hasKeyword) {
                BuzzerHelper.play(context)

                AlertHistoryManager.save(
                    context,
                    type = "SMS",
                    number = sender,
                    detail = body
                )
                break
            }
        }
    }
}
