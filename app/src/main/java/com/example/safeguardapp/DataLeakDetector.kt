package com.example.safeguardapp

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast

class DataLeakDetector(private val context: Context) {

    fun scanInstalledApps() {

        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        var detected = false

        for (app in apps) {

            val pkgName = app.packageName

            val permissions = pm.getPackageInfo(
                pkgName,
                PackageManager.GET_PERMISSIONS
            ).requestedPermissions ?: continue

            // list of sensitive permissions
            val riskyPerms = mutableListOf<String>()

            permissions.forEach { perm ->
                if (perm.contains("READ_SMS")) riskyPerms.add("READ_SMS")
                if (perm.contains("READ_CONTACTS")) riskyPerms.add("READ_CONTACTS")
                if (perm.contains("RECORD_AUDIO")) riskyPerms.add("RECORD_AUDIO")
            }

            if (riskyPerms.isNotEmpty()) {

                detected = true

                // 🔔 BUZZER
                BuzzerHelper.play(context)

                // SAVE HISTORY (Option-D pattern)
                AlertHistoryManager.save(
                    context,
                    type = "DATA",
                    number = pkgName,
                    message = null,
                    detail = "Risky permissions: ${riskyPerms.joinToString(", ")}"
                )

                // TOAST ALERT
                Toast.makeText(
                    context,
                    "⚠️ Risky app detected:\n$pkgName\nPermissions: ${riskyPerms.joinToString(", ")}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (!detected) {
            Toast.makeText(
                context,
                "✅ No risky apps detected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
