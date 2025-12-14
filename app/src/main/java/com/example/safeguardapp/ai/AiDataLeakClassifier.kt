package com.example.safeguardapp.ai

class AiDataLeakClassifier {

    fun detect(text: String): Boolean {
        val lower = text.lowercase()
        return lower.contains("password") ||
                lower.contains("aadhaar") ||
                lower.contains("pan") ||
                lower.contains("account")
    }
}
