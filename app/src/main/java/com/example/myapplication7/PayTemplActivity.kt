package com.example.myapplication7

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.present.selectedTempl
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization
import com.example.myapplication7.repository.SenderMoney
import com.example.myapplication7.repository.SenderTemplate
import kotlinx.android.synthetic.main.activity_payment.*
import kotlin.math.roundToInt


class PayTemplActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Authorization.userInfo.token == "") {
            val regIntent = Intent(this, MainActivity::class.java)
            startActivity(regIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication7.R.layout.activity_payment)

        @SuppressLint("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        textDetails.text = "Шаблон " + selectedTempl.templateName
        scoreView.text = selectedTempl.scoreFrom
        inputHowMuch.hint = selectedTempl.howMuch.toString()

        amountView.visibility = View.GONE
        spinnerNums.visibility = View.GONE
        inputTemplate.visibility = View.GONE
        switchTemplate.visibility = View.GONE

        val viewTo = findViewById(com.example.myapplication7.R.id.inputTo) as TextView
        val viewMail = findViewById(com.example.myapplication7.R.id.inputTaker) as TextView
        viewTo.isEnabled = false
        viewMail.isEnabled = false
        viewTo.text = selectedTempl.scoreTo
        viewMail.text = selectedTempl.takerName

        buttonAdd.setOnClickListener {payTemplate()}

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> {Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_LONG).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)}
                AUTH_STATUS.SUCCESS -> {
                    val scoreBringer = Intent(this, ScoresActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(scoreBringer)
                    Toast.makeText(this, "Выполнено", Toast.LENGTH_LONG).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)
                }
                AUTH_STATUS.INCORRECT -> {Toast.makeText(this, "Проверьте правильность введённых данных", Toast.LENGTH_LONG).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)}
            }
        }
        SenderMoney.message.observe(this, msg)
    }

    fun payTemplate () {
        var scoreFrom = selectedTempl.scoreFrom
        var scoreTo = selectedTempl.scoreTo
        var takerName = selectedTempl.takerName
        var howMuch = inputHowMuch.text.toString()

        SenderMoney.startSending(scoreFrom, scoreTo, takerName, howMuch, "true")
    }

}