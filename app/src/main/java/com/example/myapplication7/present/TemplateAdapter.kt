package com.example.myapplication7.present

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.PayTemplActivity
import com.example.myapplication7.entities.HistoryBox
import com.example.myapplication7.entities.TemplateBox

var selectedTempl = TemplateBox("", "", "", 0.0, "")

class TemplateAdapter(private val templates: List<TemplateBox>, private val context: Context) :
    RecyclerView.Adapter<TemplateAdapter.TemplViewHolder>() {

    class TemplViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
      val typeView: TextView = view.findViewById(com.example.myapplication7.R.id.type)
      val bodyView: TextView = view.findViewById(com.example.myapplication7.R.id.body)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemplViewHolder {
        val newView: View = LayoutInflater.from(parent.context).inflate(com.example.myapplication7.R.layout.list_history, parent, false)

        return TemplViewHolder(
            newView
        )
    }

    override fun onBindViewHolder(
        holder: TemplViewHolder,
        position: Int
    ) {
        val template = templates[position]
        var typeString = template.templateName + " " + template.howMuch + " ₽"
        var bodyString = "c " + template.scoreFrom + " на " + template.scoreTo
        holder.typeView.text = typeString
        holder.bodyView.text = bodyString

        holder.view.setOnClickListener {
            println("************ Got it!**********")
            selectedTempl.templateName=template.templateName
            selectedTempl.howMuch=template.howMuch
            selectedTempl.scoreFrom=template.scoreFrom
            selectedTempl.scoreTo=template.scoreTo
            selectedTempl.takerName=template.takerName

            val regIntent = Intent(context, PayTemplActivity::class.java)
            context.startActivity(regIntent)
        }
    }

    override fun getItemCount() = templates.size
}