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
import com.example.myapplication7.present.MessageAdapter
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization.userInfo
import com.example.myapplication7.repository.NewMessage
import com.example.myapplication7.repository.SenderMoney
import kotlinx.android.synthetic.main.activity_payment.*

class RefillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        scoreView.text = setOnSelect.selectedNum
        amountView.text = setOnSelect.selectedAmount  + " ₽"

        buttonAdd.setOnClickListener {refillMoney()}
        inputTaker.visibility = View.GONE
        inputTo.visibility = View.GONE
        spinnerNums.visibility = View.GONE

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                AUTH_STATUS.SUCCESS -> {
                    val scoreBringer = Intent(this, ScoresActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(scoreBringer)
                    Toast.makeText(this, "Выполнено", Toast.LENGTH_LONG).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)
                }
                AUTH_STATUS.INCORRECT -> Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_LONG).show()
            }
        }
        SenderMoney.message.observe(this, msg)
    }

    fun refillMoney () {
        var scoreFrom = "AddMoney"
        var scoreTo = setOnSelect.selectedNum
        var takerName = userInfo.email
        var howMuch = inputHowMuch.text.toString()
        SenderMoney.startSending(scoreFrom, scoreTo, takerName, howMuch)
    }
}