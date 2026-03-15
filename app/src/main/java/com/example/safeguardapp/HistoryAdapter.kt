package com.example.safeguardapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val list: List<AlertItem>
) : RecyclerView.Adapter<HistoryAdapter.Holder>() {

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(android.R.id.text1)
        val desc: TextView = v.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val v = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val item = list[position]

        val time = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date(item.time))

        holder.title.text = "${item.type} • $time"
        holder.desc.text = "${item.number}\n${item.detail}"
    }

    override fun getItemCount(): Int = list.size
}
