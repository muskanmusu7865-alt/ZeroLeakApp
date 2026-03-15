package com.example.safeguardapp

data class AlertItem(
    val type: String,      // SMS / CALL / DATA
    val number: String,
    val detail: String,
    val time: Long = System.currentTimeMillis()
)
