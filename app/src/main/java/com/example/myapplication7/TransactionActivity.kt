package com.example.myapplication7

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.entities.AUTH_STATUS
import com.example.myapplication7.entities.ScoreList
import com.example.myapplication7.present.MessageAdapter
import com.example.myapplication7.present.setOnSelect
import com.example.myapplication7.repository.Authorization.userInfo
import com.example.myapplication7.repository.NewMessage
import com.example.myapplication7.repository.Scores.scoreData
import com.example.myapplication7.repository.SenderMoney
import com.example.myapplication7.repository.SenderTemplate
import kotlinx.android.synthetic.main.activity_payment.*

class TransactionActivity : AppCompatActivity() {
    var takerScore = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (userInfo.token == "") {
            val regIntent = Intent(this, MainActivity::class.java)
            startActivity(regIntent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        @SuppressLint("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        scoreView.text = setOnSelect.selectedNum
        amountView.text = setOnSelect.selectedAmount  + " ₽"

        buttonAdd.setOnClickListener {transactionMoney()}
        inputTaker.visibility = View.GONE
        inputTo.visibility = View.GONE

        var spinner : Spinner = findViewById(R.id.spinnerNums)
        val scoresString = ArrayList<String>()

        for (i in 0..(scoreData.size-1))
            scoresString.add(scoreData[i].number)
        scoresString.remove(setOnSelect.selectedNum)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, scoresString) // scoreData: List<ScoreList>
            spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                takerScore = scoresString[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                takerScore = scoresString[0]
            }
        }

        val msg = Observer<AUTH_STATUS> { msg ->
            println("from observer")
            when (msg) {
                AUTH_STATUS.FAILED -> {
                    Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_SHORT).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)}
                AUTH_STATUS.SUCCESS -> {
                    val scoreBringer = Intent(this, ScoresActivity::class.java) // создаём объект с описанием нового окна ява
                    startActivity(scoreBringer)
                    Toast.makeText(this, "Выполнено", Toast.LENGTH_SHORT).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)
                }
                AUTH_STATUS.INCORRECT -> {Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show()
                    SenderMoney.message.postValue(AUTH_STATUS.UNSIGNED)}
            }
        }
        SenderMoney.message.observe(this, msg)
    }

    fun transactionMoney () {
        var scoreFrom = setOnSelect.selectedNum
        var scoreTo = takerScore
        var takerName = userInfo.email
        var howMuch = inputHowMuch.text.toString()
        var tempName = inputTemplate.text.toString()

        val switch: Switch = findViewById(R.id.switchTemplate)
        if (switch.isChecked) SenderTemplate.startSending(tempName, scoreFrom, scoreTo, takerName, howMuch)

        SenderMoney.startSending(scoreFrom, scoreTo, takerName, howMuch, "false")
    }
}