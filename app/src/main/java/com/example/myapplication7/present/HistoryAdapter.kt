package com.example.myapplication7.present

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.entities.HistoryBox

class HistoryAdapter(private val histories: List<HistoryBox>) :
    RecyclerView.Adapter<HistoryAdapter.HistViewHolder>() {

    class HistViewHolder (view: View) : RecyclerView.ViewHolder(view) {
      val dateView: TextView = view.findViewById(com.example.myapplication7.R.id.date)
      val typeView: TextView = view.findViewById(com.example.myapplication7.R.id.type)
      val bodyView: TextView = view.findViewById(com.example.myapplication7.R.id.body)
      val frameView: LinearLayout = view.findViewById(com.example.myapplication7.R.id.frameHist)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistViewHolder {
        val newView: View = LayoutInflater.from(parent.context).inflate(com.example.myapplication7.R.layout.list_history, parent, false)

        return HistViewHolder(
            newView
        )
    }

    override fun onBindViewHolder(
        holder: HistViewHolder,
        position: Int
    ) {
        val history = histories[position]

        if (history.type == "Пополнение") {
            holder.frameView.setBackgroundColor(Color.parseColor("#D8F6CE"))
        }

        var dateString = history.sentTime.toString()
        var typeString = history.type
        if (history.byTemplate) typeString = typeString + "По шаблону"
        var bodyString = "c " + history.scoreFrom + " на " + history.scoreTo + " " + history.howMuch + " ₽"

        holder.dateView.text = dateString
        holder.typeView.text = typeString
        holder.bodyView.text = bodyString
    }

    override fun getItemCount() = histories.size
}