package com.example.safeguardapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardFraudCall = findViewById<MaterialCardView>(R.id.cardFraudCall)
        val cardSpamMessage = findViewById<MaterialCardView>(R.id.cardSpamMessage)
        val cardDataLeak = findViewById<MaterialCardView>(R.id.cardDataLeak)

        cardFraudCall.setOnClickListener {
            openHistory("CALL")
        }

        cardSpamMessage.setOnClickListener {
            openHistory("MESSAGE")
        }

        cardDataLeak.setOnClickListener {
            openHistory("DATA")
        }
    }

    private fun openHistory(type: String) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("TYPE", type)
        startActivity(intent)
    }
}