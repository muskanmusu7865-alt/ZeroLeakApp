package com.example.safeguardapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val type = intent.getStringExtra("TYPE") // CALL / SMS / DATA

        val all = AlertHistoryManager.load(this)
        val filtered = if (type == null) all else all.filter { it.type == type }

        val rv = findViewById<RecyclerView>(R.id.recyclerHistory)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = HistoryAdapter(filtered)
    }
}
