package com.example.myapplication7.present

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.entities.DataBuffer
import com.example.myapplication7.entities.MessageBox
import com.example.myapplication7.entities.ScoreList
import java.util.*


class MessageAdapter(private val messages: List<MessageBox>) :
    RecyclerView.Adapter<MessageAdapter.MsgViewHolder>() {

    class MsgViewHolder (view: View) : RecyclerView.ViewHolder(view) {
      val authorView: TextView = view.findViewById(com.example.myapplication7.R.id.author)
      val msgView: TextView = view.findViewById(com.example.myapplication7.R.id.msg)
      val timeView: TextView = view.findViewById(com.example.myapplication7.R.id.data)
      val frameView: LinearLayout = view.findViewById(com.example.myapplication7.R.id.frame)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MsgViewHolder {
        val newView: View = LayoutInflater.from(parent.context).inflate(com.example.myapplication7.R.layout.list_messages, parent, false)

        return MsgViewHolder(
            newView
        )
    }

    override fun onBindViewHolder(
        holder: MsgViewHolder,
        position: Int
    ) {
        val message = messages[position]


        if (message.author == "Вы") {
            holder.frameView.gravity = Gravity.RIGHT
            holder.msgView.setBackgroundColor(Color.parseColor("#FFD5E7F3"))
        } else holder.frameView.gravity = Gravity.LEFT

        val authorString = message.author + ":"
        val msgString = message.msg
        val timeString = message.sentTime.toString()

        holder.authorView.text = authorString
        holder.msgView.text = msgString
        holder.timeView.text = timeString
    }

    override fun getItemCount() = messages.size
}