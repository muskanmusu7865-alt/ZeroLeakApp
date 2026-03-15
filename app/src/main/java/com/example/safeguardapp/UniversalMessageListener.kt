package com.example.safeguardapp

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.os.Bundle
import android.widget.Toast

class UniversalMessageListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        val pkg = sbn.packageName ?: return

        // 🔴 Ignore Non-SMS apps (WhatsApp / Telegram / Social media)
        if (!isSmsPackage(pkg)) return

        val extras: Bundle = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        if (text.isBlank()) return

        // 🔔 BUZZER
        BuzzerHelper.play(this)

        // 📜 SAVE HISTORY (SMS bucket only)
        AlertHistoryManager.save(
            context = this,
            type = "SMS",
            number = title,   // sender name or number
            detail = text
        )

        // ⚠️ Optional Toast
        Toast.makeText(this, "SMS detected: $title", Toast.LENGTH_SHORT).show()
    }

    private fun isSmsPackage(pkg: String): Boolean {

        // DM/Mi/Realme/Vivo/Oppo have diff package names
        val smsApps = listOf(
            "com.android.mms",              // stock AOSP
            "com.google.android.apps.messaging", // Google Messages
            "com.miui.smsextra",            // Xiaomi
            "com.oppo.messaging",           // Oppo
            "com.vivo.message",             // Vivo
            "com.samsung.android.messaging" // Samsung
        )

        return smsApps.any { pkg.contains(it, true) }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Not needed
    }
}
