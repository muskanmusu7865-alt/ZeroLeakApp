package com.example.safeguardapp

import android.content.Context
import android.content.pm.PackageManager

class DataLeakDetector(private val context: Context) {

    fun scanInstalledApps() {

        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        for (app in apps) {
            val perms = pm.getPackageInfo(
                app.packageName,
                PackageManager.GET_PERMISSIONS
            ).requestedPermissions ?: continue

            if (perms.any {
                    it.contains("READ_SMS") ||
                            it.contains("READ_CONTACTS") ||
                            it.contains("RECORD_AUDIO")
                }) {

                BuzzerHelper.play(context)

                AlertHistoryManager.save(
                    context,
                    type = "DATA",
                    number = app.packageName,
                    detail = "Potential data-leak permission"
                )
                return
            }
        }
    }
}
