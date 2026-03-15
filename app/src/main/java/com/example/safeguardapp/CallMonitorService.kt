package com.example.safeguardapp

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

class CallMonitorService : Service() {

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var callListener: PhoneStateListener

    override fun onCreate() {
        super.onCreate()

        telephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        callListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String?) {

                if (state != TelephonyManager.CALL_STATE_RINGING) return

                val number = incomingNumber ?: return

                // ✅ Ignore saved contacts
                if (isContactSaved(this@CallMonitorService, number)) return

                // 🔔 BUZZER
                BuzzerHelper.play(this@CallMonitorService)

                // 📜 SAVE HISTORY
                AlertHistoryManager.save(
                    this@CallMonitorService,
                    type = "CALL",
                    number = number,
                    detail = "Unknown incoming SIM call"
                )
            }
        }

        telephonyManager.listen(
            callListener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    override fun onDestroy() {
        telephonyManager.listen(callListener, PhoneStateListener.LISTEN_NONE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null

    // 🔍 CONTACT CHECK
    private fun isContactSaved(context: Context, number: String): Boolean {

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        ) ?: return false

        cursor.use {
            while (it.moveToNext()) {
                val saved =
                    it.getString(
                        it.getColumnIndexOrThrow(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                    )
                if (saved.contains(number.takeLast(7))) return true
            }
        }
        return false
    }
}
