package com.example.safeguardapp.ai

class AiSmsClassifier {

    // 0 = Normal, 1 = Spam, 2 = OTP
    fun predict(message: String): Int {
        val text = message.lowercase()

        return when {
            text.contains("otp") || text.contains("verification") -> 2
            text.contains("win") ||
                    text.contains("prize") ||
                    text.contains("offer") ||
                    text.contains("free") ||
                    text.contains("click") ||
                    text.contains("link") -> 1
            else -> 0
        }
    }
}
