package com.example.safeguardapp

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFraudCall = findViewById<Button?>(R.id.btnFraudCall)
        val btnSpamMessage = findViewById<Button?>(R.id.btnSpamMessage)
        val btnDataLeak = findViewById<Button?>(R.id.btnDataLeak)

        btnFraudCall?.setOnClickListener {
            playBuzzer()
            Toast.makeText(this, "Fraud Call Detected", Toast.LENGTH_SHORT).show()
        }

        btnSpamMessage?.setOnClickListener {
            playBuzzer()
            Toast.makeText(this, "Spam Message Detected", Toast.LENGTH_SHORT).show()
        }

        btnDataLeak?.setOnClickListener {
            playBuzzer()
            Toast.makeText(this, "Data Leak Detected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playBuzzer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.buzzer)
        }
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}