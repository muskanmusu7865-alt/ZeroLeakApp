package com.example.safeguardapp

import android.content.Context
import android.media.MediaPlayer

object BuzzerHelper {
    fun play(context: Context) {
        val mp = MediaPlayer.create(context, R.raw.buzzer)
        mp.start()
        mp.setOnCompletionListener { it.release() }
    }
}
