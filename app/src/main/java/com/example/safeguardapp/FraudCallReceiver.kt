package com.example.safeguardapp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

class FraudCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) return

        // Permissions required
        val phonePerm = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED

        val contactsPerm = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (!phonePerm || !contactsPerm) return

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        if (state != TelephonyManager.EXTRA_STATE_RINGING) return

        val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

        // VoIP / WhatsApp ignore
        val telecom = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (!telecom.isInCall) return

        // Unknown vs saved
        val isSaved = isContactSaved(context, incomingNumber)

        if (!isSaved) {
            // Only unknown → buzzer
            BuzzerHelper.play(context)

            // Save history (Option-D style)
            AlertHistoryManager.save(
                context,
                type = "CALL",
                number = incomingNumber ?: "Unknown",
                message = null,
                detail = "Unknown SIM caller"
            )
        }
    }

    private fun isContactSaved(context: Context, number: String?): Boolean {
        if (number.isNullOrBlank()) return false

        val normalizedIncoming = normalize(number)

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val savedNum = normalize(it.getString(
                    it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                ))
                if (savedNum.endsWith(normalizedIncoming)) {
                    return true
                }
            }
        }
        return false
    }

    private fun normalize(num: String): String =
        num.replace("+91", "")
            .replace(" ", "")
            .replace("-", "")
            .replace("(", "")
            .replace(")", "")
            .trim()
}
