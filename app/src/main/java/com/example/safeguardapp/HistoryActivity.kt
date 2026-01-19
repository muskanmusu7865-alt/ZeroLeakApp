package com.example.safeguardapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val filterType = intent.getStringExtra("TYPE") // "SMS" / "CALL" / "DATA"

        // Load all saved history
        val all = AlertHistoryManager.load(this)

        // Apply filter if required
        val filtered = when (filterType) {
            "SMS" -> all.filter { it.type == "SMS" }
            "CALL" -> all.filter { it.type == "CALL" }
            "DATA" -> all.filter { it.type == "DATA" }
            else -> all // Show everything if no filter
        }

        // Latest entry first (descending time)
        val sorted = filtered.sortedByDescending { it.timestamp }

        val recycler = findViewById<RecyclerView>(R.id.recyclerHistory)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = HistoryAdapter(sorted)
    }
}
