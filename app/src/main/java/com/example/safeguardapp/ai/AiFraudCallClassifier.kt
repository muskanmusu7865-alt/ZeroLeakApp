package com.example.safeguardapp.ai

class AiFraudCallClassifier {

    fun isFraud(number: String): Boolean {
        return number.startsWith("140") ||
                number.startsWith("+91140")
    }
}
