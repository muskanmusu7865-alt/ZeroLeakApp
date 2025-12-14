package com.example.safeguardapp.ai

import android.content.Context
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ModelLoader {
    fun loadModel(context: Context, fileName: String): ByteBuffer {
        val inputStream = context.assets.open(fileName)
        val bytes = inputStream.readBytes()
        inputStream.close()

        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.order(ByteOrder.nativeOrder())
        buffer.put(bytes)
        buffer.rewind()

        return buffer
    }
}