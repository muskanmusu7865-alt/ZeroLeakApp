package com.example.safeguardapp

import android.content.Context

object AlertHistoryManager {

    private const val PREF = "alert_history"
    private const val KEY = "items"

    fun save(context: Context, type: String, number: String, detail: String) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val old = prefs.getString(KEY, "") ?: ""

        val newEntry = "$type|$number|$detail|${System.currentTimeMillis()}"
        prefs.edit().putString(KEY, "$newEntry\n$old").apply()
    }

    fun load(context: Context): List<AlertItem> {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY, "") ?: ""

        return raw.lines().mapNotNull {
            val p = it.split("|")
            if (p.size == 4)
                AlertItem(p[0], p[1], p[2], p[3].toLong())
            else null
        }
    }
}
