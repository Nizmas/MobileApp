package com.example.myapplication7

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization
import com.example.myapplication7.repository.SenderMoney
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        scoreView.text = setOnSelect.selectedNum
        amountView.text = setOnSelect.selectedAmount  + " ₽"
        spinnerNums.visibility = View.GONE
        buttonAdd.setOnClickListener {paymentMoney()}

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
                AUTH_STATUS.INCORRECT -> Toast.makeText(this, "Проверьте правильность введённых данных", Toast.LENGTH_LONG).show()
            }
        }
        SenderMoney.message.observe(this, msg)
    }

    fun paymentMoney () {
        var scoreFrom = setOnSelect.selectedNum
        var scoreTo = inputTo.text.toString()
        var takerName = inputTaker.text.toString()
        var howMuch = inputHowMuch.text.toString()

        fun String.isEmailValid(): Boolean { //проверка на почтовость
            return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        var e: Boolean = true
        when (e) {
            (howMuch == "") -> Toast.makeText(this, "Укажите сумму", Toast.LENGTH_LONG).show()
            (howMuch.toDouble() > setOnSelect.selectedAmount.toDouble()) -> Toast.makeText(this, "Недостаточно средств на счёте", Toast.LENGTH_LONG).show()
            (scoreTo.length != 10) -> Toast.makeText(this, "Укажите десятизначный номер", Toast.LENGTH_LONG).show()
            (!takerName.isEmailValid()) -> Toast.makeText(this, "Введите почтовый адрес", Toast.LENGTH_LONG).show()
            else -> e = false
        }

        if (!e) {
            SenderMoney.startSending(scoreFrom, scoreTo, takerName, howMuch)
        }
    }
}