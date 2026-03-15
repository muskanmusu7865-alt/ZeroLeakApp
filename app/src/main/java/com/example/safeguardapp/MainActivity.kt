package com.example.safeguardapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 📩 Spam SMS Card
        findViewById<CardView>(R.id.cardSpamMessage).setOnClickListener {
            openHistory("SMS")
        }

        // 📞 Fraud Call Card
        findViewById<CardView>(R.id.cardFraudCall).setOnClickListener {
            openHistory("CALL")
        }

        // 🔐 Data Leak Card
        findViewById<CardView>(R.id.cardDataLeak).setOnClickListener {
            val detector = DataLeakDetector(this)
            detector.scanInstalledApps()
        }
    }

    private fun openHistory(type: String) {
        val intent = Intent(this, HistoryActivity::class.java)
        intent.putExtra("TYPE", type)
        startActivity(intent)
    }
}
