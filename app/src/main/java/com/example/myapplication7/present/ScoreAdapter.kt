package com.example.myapplication7.present

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.ElementsActivity
import com.example.myapplication7.ScoresActivity
import com.example.myapplication7.entities.DataBuffer
import com.example.myapplication7.entities.ScoreList

var setOnSelect = DataBuffer("", "")

class ScoreAdapter(private val scores: List<ScoreList>, private val context: Context) :
    RecyclerView.Adapter<ScoreAdapter.MyViewHolder>() {

    class MyViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
      val scoreView: TextView = view.findViewById(com.example.myapplication7.R.id.score)
      val amountView: TextView = view.findViewById(com.example.myapplication7.R.id.amount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val newView: View = LayoutInflater.from(parent.context).inflate(com.example.myapplication7.R.layout.list_scores, parent, false)

        return MyViewHolder(
            newView
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val score = scores[position]
        val scoreString = score.number
        val amountString = score.amount.toString() + " ₽"

        holder.view.setOnClickListener {
            println("************ Got it!**********")
            setOnSelect.selectedNum = scoreString
            setOnSelect.selectedAmount = score.amount.toString()
            val regIntent = Intent(context, ElementsActivity::class.java)
            context.startActivity(regIntent)
        }
        holder.scoreView.text = scoreString
        holder.amountView.text = amountString
    }

    override fun getItemCount() = scores.size
}