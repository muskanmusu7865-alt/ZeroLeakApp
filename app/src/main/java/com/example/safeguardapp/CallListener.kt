package com.example.safeguardapp

import android.content.Context
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class CallListener(private val context: Context) : PhoneStateListener() {

    override fun onCallStateChanged(state: Int, incomingNumber: String?) {

        if (state != TelephonyManager.CALL_STATE_RINGING) return

        // WhatsApp / VoIP calls NEVER provide incomingNumber
        if (incomingNumber.isNullOrBlank()) return

        if (!isContactSaved(context, incomingNumber)) {

            BuzzerHelper.play(context)

            AlertHistoryManager.save(
                context = context,
                type = "CALL",
                number = incomingNumber,
                detail = "Unknown SIM call"
            )
        }
    }

    private fun isContactSaved(context: Context, number: String): Boolean {

        val normalizedIncoming = normalize(number)

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null, null, null
        ) ?: return false

        cursor.use {
            while (it.moveToNext()) {
                val saved = normalize(it.getString(0))
                if (saved.endsWith(normalizedIncoming)) return true
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
}
