package com.example.safeguardapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeguardapp.AlertHistoryManager.AlertItem
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val list: List<AlertItem>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtType: TextView = v.findViewById(android.R.id.text1)
        val txtMsg: TextView = v.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(h: ViewHolder, p: Int) {
        val item = list[p]

        val time = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            .format(Date(item.timestamp))

        // first line
        h.txtType.text = "${item.type} • $time"

        // second line (smart logic)
        val info = buildString {
            append(item.number ?: "Unknown")
            if (!item.detail.isNullOrEmpty()) append(" — ${item.detail}")
            if (!item.message.isNullOrEmpty()) append(" — ${item.message}")
        }

        h.txtMsg.text = info
    }

    override fun getItemCount() = list.size
}
