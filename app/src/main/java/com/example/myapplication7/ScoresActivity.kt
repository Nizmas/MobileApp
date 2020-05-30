package com.example.myapplication7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.repository.CreateScore
import com.example.myapplication7.present.ScoreAdapter
import com.example.myapplication7.repository.Scores
import com.example.myapplication7.repository.Scores.scoreData
import com.example.myapplication7.repository.Scores.scoresShow

class ScoresActivity  : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)

        scoresShow()

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                AUTH_STATUS.SUCCESS -> {
                    viewManager = LinearLayoutManager(this)
                    viewAdapter =
                        ScoreAdapter(scoreData, this)
                    recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
                AUTH_STATUS.INCORRECT -> Toast.makeText(this, "Обратитесь к модератору...", Toast.LENGTH_LONG).show()
            }
        }
        Scores.message.observe(this, msg)
    }

    fun startNewScore (view: View) {
        Toast.makeText(this, "Отправляем запрос...", Toast.LENGTH_LONG).show()
        val starter = CreateScore
        starter.newScore()

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                AUTH_STATUS.SUCCESS -> {
                    Toast.makeText(this, "Счёт открыт", Toast.LENGTH_SHORT).show()
                    scoresShow()
                    recyclerView.getRecycledViewPool().clear()
                    AUTH_STATUS.UNSIGNED
                }
                AUTH_STATUS.INCORRECT -> {
                    Toast.makeText(this, "Обратитесь к модератору...", Toast.LENGTH_LONG).show()
                    AUTH_STATUS.UNSIGNED}
            }
        }
        CreateScore.message.observe(this, msg)
    }

    fun createMsg (view: View) {
        val regIntent = Intent(this, MessagesActivity::class.java)
        startActivity(regIntent)
    }

    fun openSettings (view: View) {
        val regIntent = Intent(this, SettingsActivity::class.java)
        startActivity(regIntent)
    }
}