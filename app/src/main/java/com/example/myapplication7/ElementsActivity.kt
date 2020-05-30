package com.example.myapplication7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization
import com.example.myapplication7.repository.CloseScore
import com.example.myapplication7.repository.Scores
import kotlinx.android.synthetic.main.activity_elements.*

class ElementsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elements)

        scoreView.text = setOnSelect.selectedNum
        amountView.text = setOnSelect.selectedAmount + " ₽"
    }

    fun addMoney (view: View) {
        val regIntent = Intent(this, RefillActivity::class.java)
        startActivity(regIntent)
    }

    fun transferMoney (view: View) {
        if(setOnSelect.selectedAmount.toDouble() != 0.0)
            if (Scores.scoreData.size>1) {
                val regIntent = Intent(this, TransactionActivity::class.java)
                startActivity(regIntent)
            } else Toast.makeText(this, "Откройте новый счёт для переводов", Toast.LENGTH_SHORT).show()
         else Toast.makeText(this, "Нет денег на счету", Toast.LENGTH_SHORT).show()
    }

    fun startPayment (view: View) {
        if(setOnSelect.selectedAmount.toDouble() != 0.0) {
            val regIntent = Intent(this, PaymentActivity::class.java)
            startActivity(regIntent)
        } else Toast.makeText(this, "Нет денег на счету", Toast.LENGTH_SHORT).show()
    }

    fun showHistory (view: View) {
        val regIntent = Intent(this, HistoryActivity::class.java)
        startActivity(regIntent)
    }

    fun closeScore (view: View) {
        CloseScore.startClosing(setOnSelect.selectedNum)
        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            var tostMessage =""
            when (msg) {
                AUTH_STATUS.FAILED -> tostMessage ="Проверьте подключение к сети"
                AUTH_STATUS.SUCCESS -> { tostMessage ="Счёт успешно закрыт"
                    val enterIntent = Intent(this, ScoresActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(enterIntent)}
                AUTH_STATUS.INCORRECT -> tostMessage ="Повторите вход"
            }
            Toast.makeText(this, tostMessage, Toast.LENGTH_LONG).show()
        }
        CloseScore.message.observe(this, msg)
    }
}