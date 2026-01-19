package com.example.safeguardapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AlertHistoryManager {

    private const val PREF = "alert_history"
    private const val KEY = "items"

    data class AlertItem(
        val type: String,        // SMS / CALL / DATA
        val number: String?,     // phone/app/package
        val message: String?,    // full SMS only
        val detail: String?,     // OTP / Link / Unknown caller / Risk perms
        val timestamp: Long      // sort by time
    )

    fun save(
        context: Context,
        type: String,
        number: String?,
        message: String? = null,
        detail: String? = null
    ) {
        val list = load(context).toMutableList()

        list.add(
            AlertItem(
                type = type,
                number = number,
                message = message,
                detail = detail,
                timestamp = System.currentTimeMillis()
            )
        )

        val json = Gson().toJson(list)
        prefs(context).edit().putString(KEY, json).apply()
    }

    fun load(context: Context): List<AlertItem> {
        val json = prefs(context).getString(KEY, null) ?: return emptyList()

        val type = object : TypeToken<List<AlertItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
}
