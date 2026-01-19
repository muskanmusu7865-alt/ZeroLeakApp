package com.example.safeguardapp

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class UniversalMessageListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return

        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        if (title.isNotEmpty() || text.isNotEmpty()) {

            // 🔔 BUZZER
            BuzzerHelper.play(this)

            // 📜 HISTORY SAVE
            AlertHistoryManager.save(
                this,
                "Notification",
                "$title : $text"
            )
        }
    }
}