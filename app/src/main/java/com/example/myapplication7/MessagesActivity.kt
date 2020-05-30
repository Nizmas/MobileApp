package com.example.myapplication7

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.present.MessageAdapter
import com.example.myapplication7.repository.NewMessage
import com.example.myapplication7.repository.NewMessage.showMessages
import com.example.myapplication7.repository.Scores
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        showMessages()

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                AUTH_STATUS.SUCCESS -> {
                    viewManager = LinearLayoutManager(this)
                    viewAdapter =
                        MessageAdapter(NewMessage.messageData)
                    recyclerView = findViewById<RecyclerView>(R.id.msg_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
                AUTH_STATUS.INCORRECT -> Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_LONG).show()
            }
        }
        NewMessage.message.observe(this, msg)
    }

    fun sendMessage (view: View) {
        val msgInput = editMsg.text.toString()
        editMsg.setText("")
        val starter = NewMessage
        starter.sendingMessage(msgInput)
    }
}